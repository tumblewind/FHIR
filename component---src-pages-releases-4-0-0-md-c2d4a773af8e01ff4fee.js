(window.webpackJsonp=window.webpackJsonp||[]).push([[17],{378:function(e,t,a){"use strict";a.r(t),a.d(t,"_frontmatter",function(){return o}),a.d(t,"default",function(){return s});a(10),a(5),a(6),a(3),a(8),a(2),a(1);var r=a(104),n=a(393);function c(){return(c=Object.assign||function(e){for(var t=1;t<arguments.length;t++){var a=arguments[t];for(var r in a)Object.prototype.hasOwnProperty.call(a,r)&&(e[r]=a[r])}return e}).apply(this,arguments)}var o={},b={_frontmatter:o},i=n.a;function s(e){var t=e.components,a=function(e,t){if(null==e)return{};var a,r,n={},c=Object.keys(e);for(r=0;r<c.length;r++)a=c[r],t.indexOf(a)>=0||(n[a]=e[a]);return n}(e,["components"]);return Object(r.b)(i,c({},b,a,{components:t,mdxType:"MDXLayout"}),Object(r.b)("h1",{id:"release-number"},"Release Number"),Object(r.b)("ul",null,Object(r.b)("li",{parentName:"ul"},"0.0.1"),Object(r.b)("li",{parentName:"ul"},"JDK: ")),Object(r.b)("p",null,"Link to Download on GitHub releases"),Object(r.b)("p",null,"Maven Dependencies "),Object(r.b)("h1",{id:"change-log"},"Change Log"))}s.isMDXComponent=!0},391:function(e){e.exports={data:{site:{pathPrefix:"/FHIR"}}}},392:function(e){e.exports={data:{site:{siteMetadata:{repository:{baseUrl:"https://github.com/IBM/FHIR",subDirectory:"/docs"}}}}}},393:function(e,t,a){"use strict";a(34),a(23);var r=a(2),n=a(391),c=a(1),o=a.n(c),b=a(192),i=a(90),s=a.n(i),l=a(91),u=a(123),p=a(4),d=a.n(p),O=a(366),j=function(e){var t,a=e.children,n=e.title,c=e.tabs,o=void 0===c?[]:c,b=e.shouldHideHeader;return Object(r.b)("div",{className:d()((t={},t[O.pageHeader]=O.pageHeader,t[O.pageHeaderSticky]=o.length,t[O.pageHeaderShifted]=b,t))},Object(r.b)("div",{className:"bx--grid"},Object(r.b)("div",{className:"bx--row"},Object(r.b)("div",{className:"bx--col-lg-12"},Object(r.b)("h1",{id:"page-title",className:O.text},n)))),a)},m=a(392),h=a(367),v=function(e){var t=e.relativePagePath,a=e.repository,n=m.data.site.siteMetadata.repository,c=a||n,o=c.baseUrl,b=o+"/tree/master"+c.subDirectory+"/src/pages"+t;return o?Object(r.b)("div",{className:"bx--row "+h.row},Object(r.b)("div",{className:"bx--col"},Object(r.b)("a",{className:h.link,href:b},"Edit this page on GitHub"))):null},f=a(193),g=a(28),x=a(368);var y=function(e){var t,a;function n(){return e.apply(this,arguments)||this}return a=e,(t=n).prototype=Object.create(a.prototype),t.prototype.constructor=t,t.__proto__=a,n.prototype.render=function(){var e=this.props,t=e.tabs,a=e.slug,n=a.split("/").filter(Boolean).slice(-1)[0],c=t.map(function(e){var t,c=s()(e,{lower:!0}),o=c===n,b=a.replace(n,c);return Object(r.b)("li",{key:e,className:d()((t={},t[x.selectedItem]=o,t),x.listItem)},Object(r.b)(g.Link,{className:x.link,to:""+b},e))});return Object(r.b)("div",{className:x.tabsContainer},Object(r.b)("div",{className:"bx--grid"},Object(r.b)("div",{className:"bx--row"},Object(r.b)("div",{className:"bx--col-lg-12 bx--col-no-gutter"},Object(r.b)("nav",null,Object(r.b)("ul",{className:x.list},c))))))},n}(o.a.Component),N=a(194);t.a=function(e){var t=e.pageContext,a=e.children,c=e.location,o=t.frontmatter,i=void 0===o?{}:o,p=t.relativePagePath,d=i.tabs,O=i.title,m=Object(l.c)(),h=!!d&&"down"===m,g=n.data.site.pathPrefix,x=g?c.pathname.replace(g,""):c.pathname,w=d?x.split("/").slice(-1)[0]||s()(d[0],{lower:!0}):"";return Object(r.b)(u.a,{shouldHideHeader:h,homepage:!1},Object(r.b)(j,{shouldHideHeader:h,title:O,label:"label",tabs:d},d&&Object(r.b)(y,{slug:x,tabs:d,currentTab:w})),Object(r.b)(N.a,{padded:!0},a,Object(r.b)(v,{relativePagePath:p})),Object(r.b)(f.a,{pageContext:t,location:c,slug:x,tabs:d,currentTab:w}),Object(r.b)(b.a,null))}}}]);
//# sourceMappingURL=component---src-pages-releases-4-0-0-md-c2d4a773af8e01ff4fee.js.map