(window.webpackJsonp=window.webpackJsonp||[]).push([[5],{371:function(e,t,a){"use strict";a.r(t),a.d(t,"_frontmatter",function(){return c}),a.d(t,"default",function(){return p});a(10),a(5),a(6),a(3),a(8),a(2),a(1);var n=a(104),r=a(393);function i(){return(i=Object.assign||function(e){for(var t=1;t<arguments.length;t++){var a=arguments[t];for(var n in a)Object.prototype.hasOwnProperty.call(a,n)&&(e[n]=a[n])}return e}).apply(this,arguments)}var l,c={},o=(l="PageDescription",function(e){return console.warn("Component "+l+" was not imported, exported, or provided by MDXProvider as global scope"),Object(n.b)("div",e)}),b={_frontmatter:c},s=r.a;function p(e){var t=e.components,a=function(e,t){if(null==e)return{};var a,n,r={},i=Object.keys(e);for(n=0;n<i.length;n++)a=i[n],t.indexOf(a)>=0||(r[a]=e[a]);return r}(e,["components"]);return Object(n.b)(s,i({},b,a,{components:t,mdxType:"MDXLayout"}),Object(n.b)(o,{mdxType:"PageDescription"},"This article outlines the build process from a local machine and from the build server (Travis, GitHub actions)."),Object(n.b)("p",null,"There are two "),Object(n.b)("h1",{id:"build-github-actions"},"Build: GitHub actions"),Object(n.b)("h1",{id:"build-travis"},"Build: Travis"),Object(n.b)("p",null,"The build with ",Object(n.b)("a",i({parentName:"p"},{href:"https://travis-ci.com/IBM/FHIR"}),"Travis")," is triggered through a ",Object(n.b)("inlineCode",{parentName:"p"},"push")," or ",Object(n.b)("inlineCode",{parentName:"p"},"pull_request")," or ",Object(n.b)("inlineCode",{parentName:"p"},"cron")," event.  The ",Object(n.b)("inlineCode",{parentName:"p"},"push")," is the only event which is enabled in the ",Object(n.b)("strong",{parentName:"p"},"IBM FHIR Server")," build. "),Object(n.b)("h1",{id:"build-local"},"Build: Local"),Object(n.b)("h2",{id:"build-and-deploy"},"Build and Deploy"),Object(n.b)("h3",{id:"examples"},"examples"),Object(n.b)("ul",null,Object(n.b)("li",{parentName:"ul"},"set the version with rc=NUMBER where the NUMBER is version of the release. ")),Object(n.b)("pre",null,Object(n.b)("code",i({parentName:"pre"},{className:"language-shell"}),"mvn clean -Drc=1 -Pdeploy-version-rc -f fhir-examples/pom.xml -N\n")),Object(n.b)("ul",null,Object(n.b)("li",{parentName:"ul"},"build and deploy ")),Object(n.b)("pre",null,Object(n.b)("code",i({parentName:"pre"},{className:"language-shell"}),"mvn clean install javadoc:jar javadoc:test-jar -Pdeploy-bintray,fhir-javadocs deploy -f fhir-examples/pom.xml  \n")),Object(n.b)("h3",{id:"tools"},"tools"),Object(n.b)("ul",null,Object(n.b)("li",{parentName:"ul"},"set the version ")),Object(n.b)("pre",null,Object(n.b)("code",i({parentName:"pre"},{className:"language-shell"}),"mvn clean -Drc=1 -Pdeploy-version-rc -f fhir-tools/pom.xml -N\n")),Object(n.b)("ul",null,Object(n.b)("li",{parentName:"ul"},"build and deploy ")),Object(n.b)("pre",null,Object(n.b)("code",i({parentName:"pre"},{className:"language-shell"}),"mvn clean install javadoc:jar javadoc:test-jar -Pdeploy-bintray,fhir-javadocs deploy -f fhir-tools/pom.xml  \n")),Object(n.b)("h3",{id:"parent"},"parent"),Object(n.b)("ul",null,Object(n.b)("li",{parentName:"ul"},"set the version ")),Object(n.b)("pre",null,Object(n.b)("code",i({parentName:"pre"},{className:"language-shell"}),"mvn clean -Drc=1 -Pdeploy-version-rc -f fhir-parent/pom.xml -N\n")),Object(n.b)("ul",null,Object(n.b)("li",{parentName:"ul"},"build and deploy ")),Object(n.b)("pre",null,Object(n.b)("code",i({parentName:"pre"},{className:"language-shell"}),"mvn clean install javadoc:jar javadoc:test-jar -Pdeploy-bintray,fhir-javadocs deploy -f fhir-parent/pom.xml  \n")),Object(n.b)("ul",null,Object(n.b)("li",{parentName:"ul"},"Locally * The local connection to bintray takes around 30-40 minutes to build, test and deploy.  ")),Object(n.b)("h1",{id:"tips"},"Tips"),Object(n.b)("p",null,"If the build appears to randomly stop complaining about a 400 error, the issue might be one of the following: "),Object(n.b)("h2",{id:"1-naming-of-the-artifactid-might-include-a-space"},"1 Naming of the artifactId might include a space."),Object(n.b)("p",null,"The artifactId might have a space in it. "),Object(n.b)("p",null,Object(n.b)("inlineCode",{parentName:"p"},"<artifactId>fhir-notification-websocket </artifactId>")),Object(n.b)("p",null," convert to "),Object(n.b)("p",null," ",Object(n.b)("inlineCode",{parentName:"p"},"<artifactId>fhir-notification-websocket</artifactId>")),Object(n.b)("h2",{id:"2-the-bintray-key-might-need-to-be-checked"},"2 The BinTray Key might need to be checked."),Object(n.b)("p",null,"If you are able to successfully upload, there may be a need to check BinTray via commandline. "),Object(n.b)("pre",null,Object(n.b)("code",i({parentName:"pre"},{}),"curl -v  --user <user>:<apiKey> -T /Users/<username>/.m2/repository/com/ibm/fhir/fhir-notification-websocket/4.0.0-rc1-20191027/fhir-notification-websocket-4.0.0-rc1-20191027.pom https://api.bintray.com/maven/ibm-watson-health/ibm-fhir-server-snapshots/fhir-notification-websocket/com/ibm/fhir/fhir-notification-websocket/4.0.0-rc1-20191027/fhir-notification-websocket-4.0.0-rc1-20191027.pom\n")))}p.isMDXComponent=!0},391:function(e){e.exports={data:{site:{pathPrefix:"/FHIR"}}}},392:function(e){e.exports={data:{site:{siteMetadata:{repository:{baseUrl:"https://github.com/IBM/FHIR",subDirectory:"/docs"}}}}}},393:function(e,t,a){"use strict";a(34),a(23);var n=a(2),r=a(391),i=a(1),l=a.n(i),c=a(192),o=a(90),b=a.n(o),s=a(91),p=a(123),d=a(4),u=a.n(d),m=a(366),h=function(e){var t,a=e.children,r=e.title,i=e.tabs,l=void 0===i?[]:i,c=e.shouldHideHeader;return Object(n.b)("div",{className:u()((t={},t[m.pageHeader]=m.pageHeader,t[m.pageHeaderSticky]=l.length,t[m.pageHeaderShifted]=c,t))},Object(n.b)("div",{className:"bx--grid"},Object(n.b)("div",{className:"bx--row"},Object(n.b)("div",{className:"bx--col-lg-12"},Object(n.b)("h1",{id:"page-title",className:m.text},r)))),a)},j=a(392),O=a(367),f=function(e){var t=e.relativePagePath,a=e.repository,r=j.data.site.siteMetadata.repository,i=a||r,l=i.baseUrl,c=l+"/tree/master"+i.subDirectory+"/src/pages"+t;return l?Object(n.b)("div",{className:"bx--row "+O.row},Object(n.b)("div",{className:"bx--col"},Object(n.b)("a",{className:O.link,href:c},"Edit this page on GitHub"))):null},v=a(193),g=a(28),y=a(368);var N=function(e){var t,a;function r(){return e.apply(this,arguments)||this}return a=e,(t=r).prototype=Object.create(a.prototype),t.prototype.constructor=t,t.__proto__=a,r.prototype.render=function(){var e=this.props,t=e.tabs,a=e.slug,r=a.split("/").filter(Boolean).slice(-1)[0],i=t.map(function(e){var t,i=b()(e,{lower:!0}),l=i===r,c=a.replace(r,i);return Object(n.b)("li",{key:e,className:u()((t={},t[y.selectedItem]=l,t),y.listItem)},Object(n.b)(g.Link,{className:y.link,to:""+c},e))});return Object(n.b)("div",{className:y.tabsContainer},Object(n.b)("div",{className:"bx--grid"},Object(n.b)("div",{className:"bx--row"},Object(n.b)("div",{className:"bx--col-lg-12 bx--col-no-gutter"},Object(n.b)("nav",null,Object(n.b)("ul",{className:y.list},i))))))},r}(l.a.Component),w=a(194);t.a=function(e){var t=e.pageContext,a=e.children,i=e.location,l=t.frontmatter,o=void 0===l?{}:l,d=t.relativePagePath,u=o.tabs,m=o.title,j=Object(s.c)(),O=!!u&&"down"===j,g=r.data.site.pathPrefix,y=g?i.pathname.replace(g,""):i.pathname,x=u?y.split("/").slice(-1)[0]||b()(u[0],{lower:!0}):"";return Object(n.b)(p.a,{shouldHideHeader:O,homepage:!1},Object(n.b)(h,{shouldHideHeader:O,title:m,label:"label",tabs:u},u&&Object(n.b)(N,{slug:y,tabs:u,currentTab:x})),Object(n.b)(w.a,{padded:!0},a,Object(n.b)(f,{relativePagePath:d})),Object(n.b)(v.a,{pageContext:t,location:i,slug:y,tabs:u,currentTab:x}),Object(n.b)(c.a,null))}}}]);
//# sourceMappingURL=component---src-pages-build-mdx-435c48a627e47de62f47.js.map