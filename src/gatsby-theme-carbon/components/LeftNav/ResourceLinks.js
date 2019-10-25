import React from 'react';
import ResourceLinks from 'gatsby-theme-carbon/src/components/LeftNav/ResourceLinks';


// Figure out how to add [![Build
// Status](https://travis-ci.com/IBM/FHIR.svg?branch=master)](https://travis-ci.com/IBM/FHIR)
const links = [
  {
    title: 'GitHub: Releases',
    href: 'https://github.com/ibm/fhir/releases',
  },
  {
    title: 'GitHub: Source',
    href: 'https://github.com/IBM/FHIR/',
  },
  {
    title: 'GitHub: Issues',
    href: 'https://github.com/IBM/FHIR/issues',
  },
  {
    title: 'BinTray: Snapshots',
    href: 'https://bintray.com/ibm-watson-health/ibm-fhir-server-snapshots',
  },
  {
    title: 'HL7 FHIR',
    href: 'https://www.hl7.org/fhir/index.html',
  },
];

// shouldOpenNewTabs: true if outbound links should open in a new tab
const CustomResources = () => <ResourceLinks shouldOpenNewTabs links={links} />;

export default CustomResources;
