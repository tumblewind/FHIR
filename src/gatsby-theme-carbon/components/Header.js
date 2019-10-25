import React from 'react';
import Header from 'gatsby-theme-carbon/src/components/Header';

const CustomHeader = props => (
  <Header {...props} href="https://ibm.github.io/FHIR/">
    <span>IBM</span>&nbsp;FHIR&reg;&nbsp;Server
  </Header>
);

export default CustomHeader;
