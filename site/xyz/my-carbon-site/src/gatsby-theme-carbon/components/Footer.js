import React from 'react';
import Footer from 'gatsby-theme-carbon/src/components/Footer';

const Content = () => (
  <>
    <p>
      The IBM FHIR Server is licensed under the Apache 2.0 license. Full license text is available at LICENSE.
    </p>
    <p>
      <a href="https://www.hl7.org/fhir/index.html">
        FHIR&reg;
      </a> is the registered trademark of HL7 and is used with the permission of HL7.
    </p>
  </>
);

const links = {
  firstCol: [
    { href: 'https://github.com/ibm/fhir', linkText: 'IBM FHIR Server on GitHub' },
    { href: 'https://www.hl7.org/fhir/index.html', linkText: 'HL7 FHIR' },
  ],
  
};

const CustomFooter = () => <Footer links={links} Content={Content} />;

export default CustomFooter;
