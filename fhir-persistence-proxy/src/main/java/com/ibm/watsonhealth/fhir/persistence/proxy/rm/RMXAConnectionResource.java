/**
 * (C) Copyright IBM Corp. 2017,2018,2019
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.watsonhealth.fhir.persistence.proxy.rm;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.sql.ConnectionEventListener;
import javax.sql.StatementEventListener;
import javax.sql.XAConnection;
import javax.sql.XADataSource;
import javax.transaction.xa.XAException;
import javax.transaction.xa.XAResource;
import javax.transaction.xa.Xid;

import com.ibm.watsonhealth.fhir.core.FHIRUtilities;
import com.ibm.watsonhealth.fhir.persistence.proxy.FHIRProxyXADataSource;

/**
 * This class is used during XA recovery operations to represent an XAConnection and its associated XAResource, but
 * in reality it serves as a proxy for ALL of the XAConnections and their associated XAResources related to XADataSources
 * cached by the proxy datasource.
 * 
 * This class is only used during the XA Resource recovery operations triggered by the Liberty Recovery Manager.
 * 
 * @author padams
 */
public class RMXAConnectionResource implements XAConnection, XAResource {
    private static final Logger log = Logger.getLogger(RMXAConnectionResource.class.getName());

    private List<XADataSource> proxiedXADataSources;
    private List<XAConnection> proxiedXAConnections;
    private List<XAResource> proxiedXAResources;
    private Map<Xid, List<XAResource>> proxiedXids;

    /**
     * This ctor is invoked by the FHIRProxyXADataSource class when the Liberty Recovery Manager has triggered XA
     * recovery operations.
     * 
     * @param dataSource
     *            the parent FHIRProxyXADataSource instance
     * @throws SQLException
     */
    public RMXAConnectionResource(FHIRProxyXADataSource parentDS) throws SQLException {
        log.entering(this.getClass().getName(), "RMXAConnectionResource ctor");
        log.fine(FHIRUtilities.getCurrentStacktrace());
        try {
            setProxiedXADataSources(parentDS.getCachedDataSources());
            buildProxiedXAConnections();
        } finally {
            log.exiting(this.getClass().getName(), "RMXAConnectionResource ctor");
        }
    }

    //
    // Getter/setter methods
    //

    public List<XADataSource> getProxiedXADataSources() {
        return proxiedXADataSources;
    }

    public void setProxiedXADataSources(List<XADataSource> proxiedXADataSources) {
        this.proxiedXADataSources = proxiedXADataSources;
    }

    public List<XAConnection> getProxiedXAConnections() {
        return proxiedXAConnections;
    }

    public void setProxiedXAConnections(List<XAConnection> proxiedXAConnections) {
        this.proxiedXAConnections = proxiedXAConnections;
    }

    public List<XAResource> getProxiedXAResources() {
        return proxiedXAResources;
    }

    public void setProxiedXAResources(List<XAResource> proxiedXAResources) {
        this.proxiedXAResources = proxiedXAResources;
    }

    public Map<Xid, List<XAResource>> getProxiedXids() {
        return proxiedXids;
    }

    public void setProxiedXids(Map<Xid, List<XAResource>> proxiedXids) {
        this.proxiedXids = proxiedXids;
    }

    //
    // XAConnection methods
    //

    /*
     * (non-Javadoc)
     * @see javax.sql.XAConnection#getXAResource()
     */
    @Override
    public XAResource getXAResource() throws SQLException {
        log.entering(this.getClass().getName(), "getXAResource");
        log.fine(FHIRUtilities.getCurrentStacktrace());
        try {
            buildProxiedXAResources();
            try {
                buildProxiedXids(0);
            } catch (XAException e) {
                throw new SQLException("Error gathering Xids for XAResources", e);
            }
            return this;
        } finally {
            log.exiting(this.getClass().getName(), "getXAResource");
        }
    }

    /*
     * (non-Javadoc)
     * @see javax.sql.PooledConnection#getConnection()
     */
    @Override
    public Connection getConnection() throws SQLException {
        //
        // Note: there's really no good answer for how to implement this method in
        // the context of the RMXAConnectionResource class.   This class's job is to 
        // act as a proxy for all the XAConnections associated with the proxy datasource's
        // cached XADataSource instances.   Most of the methods in this class will simply
        // delegate calls to each of the proxied XAConnections.
        // However, this method must return a Connection associated with our proxy XAConnection,
        // and it wouldn't make sense to try to delete each of those method calls to EACH of the 
        // proxied XAConnections.   Our assumption is that the Liberty Recovery Manager will use this
        // Connection to gather metadata about the datasource (database) and will not actually try to
        // execute any SQL statements, etc.   So, we'll grab the first proxied XAConnection and then
        // call it's getConnection() method, then wrap that with our TestConnection wrapper class
        // and hope for the best :)
        //
        log.entering(this.getClass().getName(), "getConnection");
        log.fine(FHIRUtilities.getCurrentStacktrace());
        try {
            Connection conn = null;
            if (getProxiedXAConnections() != null && getProxiedXAConnections().size() > 0) {
                conn = getProxiedXAConnections().get(0).getConnection();
            }
            return conn;
        } finally {
            log.exiting(this.getClass().getName(), "getConnection");
        }
    }

    /*
     * (non-Javadoc)
     * @see javax.sql.PooledConnection#close()
     */
    @Override
    public void close() throws SQLException {
        log.entering(this.getClass().getName(), "close");
        log.fine(FHIRUtilities.getCurrentStacktrace());
        try {
            if (getProxiedXAConnections() != null) {
                // Call close on each proxied XAConnection.
                for (XAConnection connection : getProxiedXAConnections()) {
                    connection.close();
                }

                // Clear out all our state data now.
                setProxiedXids(null);
                setProxiedXAResources(null);
                setProxiedXAConnections(null);
            }
        } finally {
            log.exiting(this.getClass().getName(), "close");
        }
    }

    /*
     * (non-Javadoc)
     * @see javax.sql.PooledConnection#addConnectionEventListener(javax.sql.ConnectionEventListener)
     */
    @Override
    public void addConnectionEventListener(ConnectionEventListener listener) {
        log.entering(this.getClass().getName(), "addConnectionEventListener");
        try {
            // Drive the method calls to each of the proxied XAConnections.
            List<XAConnection> connections = getProxiedXAConnections();
            for (XAConnection connection : connections) {
                connection.addConnectionEventListener(listener);
            }
        } finally {
            log.exiting(this.getClass().getName(), "addConnectionEventListener");
        }
    }

    /*
     * (non-Javadoc)
     * @see javax.sql.PooledConnection#removeConnectionEventListener(javax.sql.ConnectionEventListener)
     */
    @Override
    public void removeConnectionEventListener(ConnectionEventListener listener) {
        log.entering(this.getClass().getName(), "removeConnectionEventListener");
        try {
            // Drive the method calls to each of the proxied XAConnections.
            List<XAConnection> connections = getProxiedXAConnections();
            for (XAConnection connection : connections) {
                connection.removeConnectionEventListener(listener);
            }
        } finally {
            log.exiting(this.getClass().getName(), "removeConnectionEventListener");
        }
    }

    /*
     * (non-Javadoc)
     * @see javax.sql.PooledConnection#addStatementEventListener(javax.sql.StatementEventListener)
     */
    @Override
    public void addStatementEventListener(StatementEventListener listener) {
        log.entering(this.getClass().getName(), "addStatementEventListener");
        try {
            // Drive the method calls to each of the proxied XAConnections.
            List<XAConnection> connections = getProxiedXAConnections();
            for (XAConnection connection : connections) {
                connection.addStatementEventListener(listener);
            }
        } finally {
            log.exiting(this.getClass().getName(), "addStatementEventListener");
        }
    }

    /*
     * (non-Javadoc)
     * @see javax.sql.PooledConnection#removeStatementEventListener(javax.sql.StatementEventListener)
     */
    @Override
    public void removeStatementEventListener(StatementEventListener listener) {
        log.entering(this.getClass().getName(), "removeStatementEventListener");
        try {
            // Drive the method calls to each of the proxied XAConnections.
            List<XAConnection> connections = getProxiedXAConnections();
            for (XAConnection connection : connections) {
                connection.removeStatementEventListener(listener);
            }
        } finally {
            log.exiting(this.getClass().getName(), "removeStatementEventListener");
        }
    }

    //
    // XAResource methods
    //

    /*
     * (non-Javadoc)
     * @see javax.transaction.xa.XAResource#recover(int)
     */
    @Override
    public Xid[] recover(int flag) throws XAException {
        log.entering(this.getClass().getName(), "recover(int)", new Object[] {
                "flag", flag
        });
        log.fine(FHIRUtilities.getCurrentStacktrace());
        try {
            // We'll need to visit each proxied XAResource and call its recover() method,
            // then store each of the returned Xid's in a map keyed by Xid and holding the
            // list of XAResources associated with that Xid.
            buildProxiedXids(flag);

            // Return an array containing all the Xid keys from the map.
            Xid[] result = buildXidArrayFromMap();

            return result;
        } finally {
            log.exiting(this.getClass().getName(), "recover(int)");
        }
    }

    /*
     * (non-Javadoc)
     * @see javax.transaction.xa.XAResource#commit(javax.transaction.xa.Xid, boolean)
     */
    @Override
    public void commit(Xid xid, boolean onePhase) throws XAException {
        log.entering(this.getClass().getName(), "commit", new Object[] {
                "onePhase", Boolean.toString(onePhase), "Xid", displayXid(xid)
        });
        log.fine(FHIRUtilities.getCurrentStacktrace());
        try {
            // Retrieve the XAResource(s) associated with this Xid.
            List<XAResource> resources = getXAResourcesForXid(xid);
            if (resources == null) {
                throw new XAException("commit: Unknown Xid");
            }

            // Drive the method calls to each of the XAResource instances.
            for (XAResource resource : resources) {
                resource.commit(xid, onePhase);
            }
        } finally {
            log.exiting(this.getClass().getName(), "commit");
        }
    }
    
    private String displayXid(Xid xid) {
        StringBuilder sb = new StringBuilder();
        sb.append("Xid[");
        sb.append("formatId=").append(xid.getFormatId());
        sb.append(",globalTxnId=").append(bytesToHex(xid.getGlobalTransactionId()));
        sb.append(",branchQualifier=").append(bytesToHex(xid.getBranchQualifier()));
        sb.append("]");
        return sb.toString();
    }
    
    private final static char[] hexArray = "0123456789ABCDEF".toCharArray();
    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for ( int j = 0; j < bytes.length; j++ ) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    /*
     * (non-Javadoc)
     * @see javax.transaction.xa.XAResource#end(javax.transaction.xa.Xid, int)
     */
    @Override
    public void end(Xid xid, int flag) throws XAException {
        log.entering(this.getClass().getName(), "end");
        log.fine(FHIRUtilities.getCurrentStacktrace());
        try {
            // Retrieve the XAResource(s) associated with this Xid.
            List<XAResource> resources = getXAResourcesForXid(xid);
            if (resources == null) {
                throw new XAException("end: Unknown Xid");
            }

            // Drive the method calls to each of the XAResource instances.
            for (XAResource resource : resources) {
                resource.end(xid, flag);
            }
        } finally {
            log.exiting(this.getClass().getName(), "end");
        }
    }

    /*
     * (non-Javadoc)
     * @see javax.transaction.xa.XAResource#forget(javax.transaction.xa.Xid)
     */
    @Override
    public void forget(Xid xid) throws XAException {
        log.entering(this.getClass().getName(), "forget");
        log.fine(FHIRUtilities.getCurrentStacktrace());
        try {
            // Retrieve the XAResource(s) associated with this Xid.
            List<XAResource> resources = getXAResourcesForXid(xid);
            if (resources == null) {
                throw new XAException("forget: Unknown Xid");
            }

            // Drive the method calls to each of the XAResource instances.
            for (XAResource resource : resources) {
                resource.forget(xid);
            }
        } finally {
            log.exiting(this.getClass().getName(), "forget");
        }

    }

    /*
     * (non-Javadoc)
     * @see javax.transaction.xa.XAResource#getTransactionTimeout()
     */
    @Override
    public int getTransactionTimeout() throws XAException {
        log.entering(this.getClass().getName(), "getTransactionTimeout");
        int timeoutSecs = 0;
        try {
            // We'll return the min(timeout) across all our proxied XAResource instances.
            if (getProxiedXAResources() != null) {
                timeoutSecs = Integer.MAX_VALUE;
                for (XAResource res : getProxiedXAResources()) {
                    int resourceTimeout = res.getTransactionTimeout();
                    if (resourceTimeout < timeoutSecs) {
                        timeoutSecs = resourceTimeout;
                    }
                }
            }
            return timeoutSecs;
        } finally {
            log.exiting(this.getClass().getName(), "getTransactionTimeout", new Object[] {
                    "timeout", timeoutSecs
            });
        }
    }

    /*
     * (non-Javadoc)
     * @see javax.transaction.xa.XAResource#isSameRM(javax.transaction.xa.XAResource)
     */
    @Override
    public boolean isSameRM(XAResource otherResource) throws XAException {
        log.entering(this.getClass().getName(), "isSameRM");
        boolean isSame = false;
        try {
            // Drive the method calls to each of the proxied XAResource instances,
            // and return the logically ANDed value received from those method calls.
            if (getProxiedXAResources() != null) {
                isSame = true;
                for (XAResource resource : getProxiedXAResources()) {
                    isSame = isSame && resource.isSameRM(otherResource);
                }
            }
            return isSame;
        } finally {
            log.exiting(this.getClass().getName(), "isSameRM", new Object[] {
                    "isSame", Boolean.toString(isSame)
            });
        }
    }

    /*
     * (non-Javadoc)
     * @see javax.transaction.xa.XAResource#prepare(javax.transaction.xa.Xid)
     */
    @Override
    public int prepare(Xid xid) throws XAException {
        log.entering(this.getClass().getName(), "prepare");
        log.fine(FHIRUtilities.getCurrentStacktrace());
        int vote = XAResource.XA_OK;
        try {
            // Retrieve the XAResource(s) associated with this Xid.
            List<XAResource> resources = getXAResourcesForXid(xid);
            if (resources == null) {
                throw new XAException("prepare: Unknown Xid");
            }

            // Drive the method calls to each of the XAResource instances.
            for (XAResource resource : resources) {
                int resourceVote = resource.prepare(xid);
                if (resourceVote > vote) {
                    vote = resourceVote;
                }
            }
            return vote;
        } finally {
            log.exiting(this.getClass().getName(), "prepare", new Object[] {
                    "vote", vote
            });
        }
    }

    /*
     * (non-Javadoc)
     * @see javax.transaction.xa.XAResource#rollback(javax.transaction.xa.Xid)
     */
    @Override
    public void rollback(Xid xid) throws XAException {
        log.entering(this.getClass().getName(), "rollback");
        log.fine(FHIRUtilities.getCurrentStacktrace());
        try {
            // Retrieve the XAResource(s) associated with this Xid.
            List<XAResource> resources = getXAResourcesForXid(xid);
            if (resources == null) {
                throw new XAException("rollback: Unknown Xid");
            }

            // Drive the method calls to each of the XAResource instances.
            for (XAResource resource : resources) {
                resource.rollback(xid);
            }
        } finally {
            log.exiting(this.getClass().getName(), "rollback");
        }
    }

    /*
     * (non-Javadoc)
     * @see javax.transaction.xa.XAResource#setTransactionTimeout(int)
     */
    @Override
    public boolean setTransactionTimeout(int timeoutSecs) throws XAException {
        log.entering(this.getClass().getName(), "setTransactionTimeout");
        boolean result = false;
        try {
            // Drive the method calls to each of the proxied XAResource instances.
            if (getProxiedXAResources() != null) {
                result = true;
                for (XAResource res : getProxiedXAResources()) {
                    result = result && res.setTransactionTimeout(timeoutSecs);
                }
            }
            return result;
        } finally {
            log.exiting(this.getClass().getName(), "setTransactionTimeout");
        }
    }

    /*
     * (non-Javadoc)
     * @see javax.transaction.xa.XAResource#start(javax.transaction.xa.Xid, int)
     */
    @Override
    public void start(Xid xid, int flag) throws XAException {
        log.entering(this.getClass().getName(), "start");
        log.fine(FHIRUtilities.getCurrentStacktrace());
        try {
            // Retrieve the XAResource(s) associated with this Xid.
            List<XAResource> resources = getXAResourcesForXid(xid);
            if (resources == null) {
                throw new XAException("start: Unknown Xid");
            }

            // Drive the method calls to each of the XAResource instances.
            for (XAResource resource : resources) {
                resource.start(xid, flag);
            }
        } finally {
            log.exiting(this.getClass().getName(), "start");
        }
    }

    //
    // Private methods
    //

    /**
     * Retrieves the list of XAResources associated with the specified transaction id (xid)
     * 
     * @param xid
     *            the transaction identifier
     * @return a list of XAResources associated with the transaction, or null if mapping is found
     */
    private List<XAResource> getXAResourcesForXid(Xid xid) {
        List<XAResource> result = null;
        if (getProxiedXids() != null) {
            result = getProxiedXids().get(xid);
        }
        return result;
    }

    /**
     * This method will walk the list of proxied XADataSources and obtain a connection for each one.
     */
    private void buildProxiedXAConnections() throws SQLException {
        log.entering(this.getClass().getName(), "buildProxiedXAConnections");
        try {
            List<XAConnection> connections = new ArrayList<>();
            if (getProxiedXADataSources() != null) {
                for (XADataSource ds : getProxiedXADataSources()) {
                    XAConnection connection = ds.getXAConnection();
                    connections.add(connection);
                }
            }
            setProxiedXAConnections(connections);
        } finally {
            log.exiting(this.getClass().getName(), "buildProxiedXAConnections");
        }
    }

    /**
     * This method will walk the list of proxied XAConnections and obtain an XAResource for each one.
     */
    private void buildProxiedXAResources() throws SQLException {
        log.entering(this.getClass().getName(), "buildProxiedXAResources");
        try {
            List<XAResource> resources = new ArrayList<>();
            if (getProxiedXAConnections() != null) {
                for (XAConnection connection : getProxiedXAConnections()) {
                    XAResource resource = connection.getXAResource();
                    resources.add(resource);
                }
            }
            setProxiedXAResources(resources);
        } finally {
            log.exiting(this.getClass().getName(), "buildProxiedXAResources");
        }
    }

    /**
     * This method builds our map of Xid -> List<XAResource> for the collection of proxied XAResource instances.
     */
    private void buildProxiedXids(int flag) throws XAException {
        log.entering(this.getClass().getName(), "buildProxiedXids(int)");
        try {
            Map<Xid, List<XAResource>> xidMap = new HashMap<>();
            if (getProxiedXAResources() != null) {
                // Visit each XAResource, and add it to the correct map entry(ies).
                for (XAResource resource : getProxiedXAResources()) {

                    // Ask the XAResource which transactions it is involved with.
                    Xid[] xids = resource.recover(flag);
                    if (xids != null) {
                        // Make sure the XAResource instance is associated with each of
                        // its transactions (Xids).
                        for (int i = 0; i < xids.length; i++) {
                            // First, make sure we have a map entry for this xid.
                            List<XAResource> resourceList = xidMap.get(xids[i]);
                            if (resourceList == null) {
                                resourceList = new ArrayList<>();
                                xidMap.put(xids[i], resourceList);
                            }

                            // Next, add this XAResource to the map entry's list.
                            resourceList.add(resource);
                        }
                    }
                }
            }
            setProxiedXids(xidMap);
            if (log.isLoggable(Level.FINER)) {
                Xid[] xids = buildXidArrayFromMap();
                log.finer("Built the following Xids:");
                for (int i = 0; i < xids.length; i++) {
                    log.finer("Xid[" + i + "]: " + displayXid(xids[i]));
                }
            }

        } finally {
            log.exiting(this.getClass().getName(), "buildProxiedXids(int)");
        }
    }

    private Xid[] buildXidArrayFromMap() {
        log.entering(this.getClass().getName(), "buildXidArrayFromMap");
        try {
            Xid[] result = null;
            if (getProxiedXids() != null) {
                Set<Xid> keyset = getProxiedXids().keySet();
                int numXids = keyset.size();
                result = new Xid[numXids];
                int i = 0;
                for (Xid xid : keyset) {
                    result[i] = xid;
                    i++;
                }
            }

            log.fine("Returning Xid[] of size: " + result.length);

            return result;
        } finally {
            log.exiting(this.getClass().getName(), "buildXidArrayFromMap");
        }
    }
}
