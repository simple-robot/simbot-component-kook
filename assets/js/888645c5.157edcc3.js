"use strict";(self.webpackChunkwebsite=self.webpackChunkwebsite||[]).push([[479],{3905:(e,t,r)=>{r.d(t,{Zo:()=>p,kt:()=>f});var n=r(7294);function i(e,t,r){return t in e?Object.defineProperty(e,t,{value:r,enumerable:!0,configurable:!0,writable:!0}):e[t]=r,e}function a(e,t){var r=Object.keys(e);if(Object.getOwnPropertySymbols){var n=Object.getOwnPropertySymbols(e);t&&(n=n.filter((function(t){return Object.getOwnPropertyDescriptor(e,t).enumerable}))),r.push.apply(r,n)}return r}function c(e){for(var t=1;t<arguments.length;t++){var r=null!=arguments[t]?arguments[t]:{};t%2?a(Object(r),!0).forEach((function(t){i(e,t,r[t])})):Object.getOwnPropertyDescriptors?Object.defineProperties(e,Object.getOwnPropertyDescriptors(r)):a(Object(r)).forEach((function(t){Object.defineProperty(e,t,Object.getOwnPropertyDescriptor(r,t))}))}return e}function o(e,t){if(null==e)return{};var r,n,i=function(e,t){if(null==e)return{};var r,n,i={},a=Object.keys(e);for(n=0;n<a.length;n++)r=a[n],t.indexOf(r)>=0||(i[r]=e[r]);return i}(e,t);if(Object.getOwnPropertySymbols){var a=Object.getOwnPropertySymbols(e);for(n=0;n<a.length;n++)r=a[n],t.indexOf(r)>=0||Object.prototype.propertyIsEnumerable.call(e,r)&&(i[r]=e[r])}return i}var l=n.createContext({}),s=function(e){var t=n.useContext(l),r=t;return e&&(r="function"==typeof e?e(t):c(c({},t),e)),r},p=function(e){var t=s(e.components);return n.createElement(l.Provider,{value:t},e.children)},u="mdxType",d={inlineCode:"code",wrapper:function(e){var t=e.children;return n.createElement(n.Fragment,{},t)}},m=n.forwardRef((function(e,t){var r=e.components,i=e.mdxType,a=e.originalType,l=e.parentName,p=o(e,["components","mdxType","originalType","parentName"]),u=s(r),m=i,f=u["".concat(l,".").concat(m)]||u[m]||d[m]||a;return r?n.createElement(f,c(c({ref:t},p),{},{components:r})):n.createElement(f,c({ref:t},p))}));function f(e,t){var r=arguments,i=t&&t.mdxType;if("string"==typeof e||i){var a=r.length,c=new Array(a);c[0]=m;var o={};for(var l in t)hasOwnProperty.call(t,l)&&(o[l]=t[l]);o.originalType=e,o[u]="string"==typeof e?e:i,c[1]=o;for(var s=2;s<a;s++)c[s]=r[s];return n.createElement.apply(null,c)}return n.createElement.apply(null,r)}m.displayName="MDXCreateElement"},2991:(e,t,r)=>{r.d(t,{Z:()=>h});var n=r(7294),i=r(6010),a=r(3438),c=r(9960),o=r(3919),l=r(5999);const s={cardContainer:"cardContainer_fWXF",cardTitle:"cardTitle_rnsV",cardDescription:"cardDescription_PWke"};function p(e){let{href:t,children:r}=e;return n.createElement(c.Z,{href:t,className:(0,i.Z)("card padding--lg",s.cardContainer)},r)}function u(e){let{href:t,icon:r,title:a,description:c}=e;return n.createElement(p,{href:t},n.createElement("h2",{className:(0,i.Z)("text--truncate",s.cardTitle),title:a},r," ",a),c&&n.createElement("p",{className:(0,i.Z)("text--truncate",s.cardDescription),title:c},c))}function d(e){let{item:t}=e;const r=(0,a.Wl)(t);return r?n.createElement(u,{href:r,icon:"\ud83d\uddc3\ufe0f",title:t.label,description:t.description??(0,l.I)({message:"{count} items",id:"theme.docs.DocCard.categoryDescription",description:"The default description for a category card in the generated index about how many items this category includes"},{count:t.items.length})}):null}function m(e){let{item:t}=e;const r=(0,o.Z)(t.href)?"\ud83d\udcc4\ufe0f":"\ud83d\udd17",i=(0,a.xz)(t.docId??void 0);return n.createElement(u,{href:t.href,icon:r,title:t.label,description:t.description??i?.description})}function f(e){let{item:t}=e;switch(t.type){case"link":return n.createElement(m,{item:t});case"category":return n.createElement(d,{item:t});default:throw new Error(`unknown item type ${JSON.stringify(t)}`)}}function y(e){let{className:t}=e;const r=(0,a.jA)();return n.createElement(h,{items:r.items,className:t})}function h(e){const{items:t,className:r}=e;if(!t)return n.createElement(y,e);const c=(0,a.MN)(t);return n.createElement("section",{className:(0,i.Z)("row",r)},c.map(((e,t)=>n.createElement("article",{key:t,className:"col col--6 margin-bottom--lg"},n.createElement(f,{item:e})))))}},3561:(e,t,r)=>{r.r(t),r.d(t,{assets:()=>p,contentTitle:()=>l,default:()=>f,frontMatter:()=>o,metadata:()=>s,toc:()=>u});var n=r(7462),i=(r(7294),r(3905)),a=r(2991),c=r(3438);const o={title:"\u5feb\u901f\u5f00\u59cb"},l=void 0,s={unversionedId:"quick-start/index",id:"version-3.0.0.0-alpha.7/quick-start/index",title:"\u5feb\u901f\u5f00\u59cb",description:"\u6b64\u76ee\u5f55\u4e2d\u63d0\u4f9b\u4e00\u4e9b\u4f7f\u7528KOOK\u7ec4\u4ef6\u7684\u7b80\u5355\u793a\u4f8b\u3002",source:"@site/versioned_docs/version-3.0.0.0-alpha.7/quick-start/index.md",sourceDirName:"quick-start",slug:"/quick-start/",permalink:"/docs/3.0.0.0-alpha.7/quick-start/",draft:!1,editUrl:"https://github.com/simple-robot/simbot-component-kook/tree/dev/main/website/versioned_docs/version-3.0.0.0-alpha.7/quick-start/index.md",tags:[],version:"3.0.0.0-alpha.7",lastUpdatedAt:1694779895,formattedLastUpdatedAt:"2023\u5e749\u670815\u65e5",frontMatter:{title:"\u5feb\u901f\u5f00\u59cb"},sidebar:"tutorialSidebar",previous:{title:"\u9996\u9875",permalink:"/docs/3.0.0.0-alpha.7/"},next:{title:"\u4f7f\u7528API",permalink:"/docs/3.0.0.0-alpha.7/quick-start/api"}},p={},u=[{value:"\u6587\u6863\u5217\u8868",id:"\u6587\u6863\u5217\u8868",level:2}],d={toc:u},m="wrapper";function f(e){let{components:t,...r}=e;return(0,i.kt)(m,(0,n.Z)({},d,r,{components:t,mdxType:"MDXLayout"}),(0,i.kt)("p",null,"\u6b64\u76ee\u5f55\u4e2d\u63d0\u4f9b\u4e00\u4e9b\u4f7f\u7528",(0,i.kt)("strong",{parentName:"p"},"KOOK\u7ec4\u4ef6"),"\u7684\u7b80\u5355\u793a\u4f8b\u3002"),(0,i.kt)("h2",{id:"\u6587\u6863\u5217\u8868"},"\u6587\u6863\u5217\u8868"),(0,i.kt)(a.Z,{items:(0,c.jA)().items,mdxType:"DocCardList"}))}f.isMDXComponent=!0}}]);