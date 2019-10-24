module.exports = {
  siteMetadata: {
    title: 'IBM FHIR Server',
    description: 'The IBM FHIR Server is a modular Java implementation of version 4 of the HL7 FHIR specification with a focus on performance and configurability.',
    keywords: 'ibm,fhir,server',
  },
  pathPrefix: '/FHIR',
  plugins: [
    {
      resolve : 'gatsby-theme-carbon', 
      options : { 
        mdxExtensions: ['.mdx', 'md'],
        titleType: 'append',
        isSearchEnabled: false,
        repository: {
          baseUrl: 'https://github.com/IBM/FHIR',
          subDirectory: '/docs',
          branch: 'master',
        },
      }
    }, 
    {
      resolve: 'gatsby-plugin-manifest',
      options: {
        name: 'IBM FHIR Server',
        short_name: 'IBM FHIR Server',
        start_url: '/',
        background_color: '#ffffff',
        theme_color: '#0062ff',
        display: 'browser',
      },
    }
  ],
  mapping: {
    "MarkdownRemark.frontmatter.author": `AuthorYaml`,
  },
};
