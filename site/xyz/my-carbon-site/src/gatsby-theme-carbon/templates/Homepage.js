import React from 'react';
import { HomepageBanner, HomepageCallout } from 'gatsby-theme-carbon';
import HomepageTemplate from 'gatsby-theme-carbon/src/templates/Homepage';
import { calloutLink } from './Homepage.module.scss';

const FirstLeftText = () => <p>IBM FHIR&reg; Server</p>;

const FirstRightText = () => (
  <p>
    The IBM FHIR Server is a modular Java implementation of version 4 of the HL7 FHIR specification with a focus on performance and configurability.
  </p>
);

const BannerText = () => <h1>IBM FHIR Server</h1>;

const customProps = {
  Banner: <HomepageBanner renderText={BannerText}  />,
  FirstCallout: (
    <HomepageCallout
      backgroundColor="#030303"
      color="white"
      leftText={FirstLeftText}
      rightText={FirstRightText}
    />
  ),
  SecondCallout: "&nbsp;",
};

// spreading the original props gives us props.children (mdx content)
function ShadowedHomepage(props) {
  return <HomepageTemplate {...props} {...customProps} />;
}

export default ShadowedHomepage;
