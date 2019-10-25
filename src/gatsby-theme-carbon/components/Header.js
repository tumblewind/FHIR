import React from 'react';
import Header from 'gatsby-theme-carbon/src/components/Header';

const CustomHeader = props => (
  <Header {...props} to="/FHIR">
    <span>IBM</span>&nbsp;FHIR&reg;&nbsp;Server
  </Header>
);

export default CustomHeader;
