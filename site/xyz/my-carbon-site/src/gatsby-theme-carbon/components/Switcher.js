import React from 'react';
import Switcher from 'gatsby-theme-carbon/src/components/Switcher';
import SwitcherLink from 'gatsby-theme-carbon/src/components/Switcher';
import SwitcherDivider from 'gatsby-theme-carbon/src/components/Switcher';

// Reference https://github.com/carbon-design-system/gatsby-theme-carbon/blob/master/packages/gatsby-theme-carbon/src/components/Switcher/Switcher.js 
const CustomChildren = () => (
  <>
  </>
);

const CustomSwitcher = ({ children }) => {
  return (
    <Switcher>
        {children}
    </Switcher>
  );
};

CustomSwitcher.defaultProps = {
  children: <CustomChildren />,
};

export default CustomSwitcher;