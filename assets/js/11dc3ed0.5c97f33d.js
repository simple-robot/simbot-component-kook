"use strict";(self.webpackChunkwebsite=self.webpackChunkwebsite||[]).push([[994],{3905:(e,t,r)=>{r.d(t,{Zo:()=>m,kt:()=>f});var n=r(7294);function i(e,t,r){return t in e?Object.defineProperty(e,t,{value:r,enumerable:!0,configurable:!0,writable:!0}):e[t]=r,e}function o(e,t){var r=Object.keys(e);if(Object.getOwnPropertySymbols){var n=Object.getOwnPropertySymbols(e);t&&(n=n.filter((function(t){return Object.getOwnPropertyDescriptor(e,t).enumerable}))),r.push.apply(r,n)}return r}function c(e){for(var t=1;t<arguments.length;t++){var r=null!=arguments[t]?arguments[t]:{};t%2?o(Object(r),!0).forEach((function(t){i(e,t,r[t])})):Object.getOwnPropertyDescriptors?Object.defineProperties(e,Object.getOwnPropertyDescriptors(r)):o(Object(r)).forEach((function(t){Object.defineProperty(e,t,Object.getOwnPropertyDescriptor(r,t))}))}return e}function a(e,t){if(null==e)return{};var r,n,i=function(e,t){if(null==e)return{};var r,n,i={},o=Object.keys(e);for(n=0;n<o.length;n++)r=o[n],t.indexOf(r)>=0||(i[r]=e[r]);return i}(e,t);if(Object.getOwnPropertySymbols){var o=Object.getOwnPropertySymbols(e);for(n=0;n<o.length;n++)r=o[n],t.indexOf(r)>=0||Object.prototype.propertyIsEnumerable.call(e,r)&&(i[r]=e[r])}return i}var s=n.createContext({}),l=function(e){var t=n.useContext(s),r=t;return e&&(r="function"==typeof e?e(t):c(c({},t),e)),r},m=function(e){var t=l(e.components);return n.createElement(s.Provider,{value:t},e.children)},u="mdxType",p={inlineCode:"code",wrapper:function(e){var t=e.children;return n.createElement(n.Fragment,{},t)}},d=n.forwardRef((function(e,t){var r=e.components,i=e.mdxType,o=e.originalType,s=e.parentName,m=a(e,["components","mdxType","originalType","parentName"]),u=l(r),d=i,f=u["".concat(s,".").concat(d)]||u[d]||p[d]||o;return r?n.createElement(f,c(c({ref:t},m),{},{components:r})):n.createElement(f,c({ref:t},m))}));function f(e,t){var r=arguments,i=t&&t.mdxType;if("string"==typeof e||i){var o=r.length,c=new Array(o);c[0]=d;var a={};for(var s in t)hasOwnProperty.call(t,s)&&(a[s]=t[s]);a.originalType=e,a[u]="string"==typeof e?e:i,c[1]=a;for(var l=2;l<o;l++)c[l]=r[l];return n.createElement.apply(null,c)}return n.createElement.apply(null,r)}d.displayName="MDXCreateElement"},8565:(e,t,r)=>{r.r(t),r.d(t,{assets:()=>w,contentTitle:()=>O,default:()=>N,frontMatter:()=>h,metadata:()=>v,toc:()=>E});var n=r(7462),i=r(7294),o=r(3905),c=r(6010),a=r(3438),s=r(9960),l=r(3919),m=r(5999);const u={cardContainer:"cardContainer_fWXF",cardTitle:"cardTitle_rnsV",cardDescription:"cardDescription_PWke"};function p(e){let{href:t,children:r}=e;return i.createElement(s.Z,{href:t,className:(0,c.Z)("card padding--lg",u.cardContainer)},r)}function d(e){let{href:t,icon:r,title:n,description:o}=e;return i.createElement(p,{href:t},i.createElement("h2",{className:(0,c.Z)("text--truncate",u.cardTitle),title:n},r," ",n),o&&i.createElement("p",{className:(0,c.Z)("text--truncate",u.cardDescription),title:o},o))}function f(e){let{item:t}=e;const r=(0,a.Wl)(t);return r?i.createElement(d,{href:r,icon:"\ud83d\uddc3\ufe0f",title:t.label,description:t.description??(0,m.I)({message:"{count} items",id:"theme.docs.DocCard.categoryDescription",description:"The default description for a category card in the generated index about how many items this category includes"},{count:t.items.length})}):null}function y(e){let{item:t}=e;const r=(0,l.Z)(t.href)?"\ud83d\udcc4\ufe0f":"\ud83d\udd17",n=(0,a.xz)(t.docId??void 0);return i.createElement(d,{href:t.href,icon:r,title:t.label,description:t.description??n?.description})}function b(e){let{item:t}=e;switch(t.type){case"link":return i.createElement(y,{item:t});case"category":return i.createElement(f,{item:t});default:throw new Error(`unknown item type ${JSON.stringify(t)}`)}}function g(e){let{className:t}=e;const r=(0,a.jA)();return i.createElement(k,{items:r.items,className:t})}function k(e){const{items:t,className:r}=e;if(!t)return i.createElement(g,e);const n=(0,a.MN)(t);return i.createElement("section",{className:(0,c.Z)("row",r)},n.map(((e,t)=>i.createElement("article",{key:t,className:"col col--6 margin-bottom--lg"},i.createElement(b,{item:e})))))}const h={title:"\u5feb\u901f\u5f00\u59cb"},O=void 0,v={unversionedId:"quick-start/index",id:"quick-start/index",title:"\u5feb\u901f\u5f00\u59cb",description:"\u6b64\u76ee\u5f55\u4e2d\u63d0\u4f9b\u4e00\u4e9b\u4f7f\u7528KOOK\u7ec4\u4ef6\u7684\u7b80\u5355\u793a\u4f8b\u3002",source:"@site/docs/quick-start/index.md",sourceDirName:"quick-start",slug:"/quick-start/",permalink:"/simbot-component-kook/docs/quick-start/",draft:!1,editUrl:"https://github.com/simple-robot/simbot-component-kook/tree/dev/main/website/docs/quick-start/index.md",tags:[],version:"current",lastUpdatedAt:1682001260,formattedLastUpdatedAt:"2023\u5e744\u670820\u65e5",frontMatter:{title:"\u5feb\u901f\u5f00\u59cb"},sidebar:"tutorialSidebar",previous:{title:"\u9996\u9875",permalink:"/simbot-component-kook/docs/"},next:{title:"\u4f7f\u7528API",permalink:"/simbot-component-kook/docs/quick-start/api"}},w={},E=[{value:"\u6587\u6863\u5217\u8868",id:"\u6587\u6863\u5217\u8868",level:2}],x={toc:E},j="wrapper";function N(e){let{components:t,...r}=e;return(0,o.kt)(j,(0,n.Z)({},x,r,{components:t,mdxType:"MDXLayout"}),(0,o.kt)("p",null,"\u6b64\u76ee\u5f55\u4e2d\u63d0\u4f9b\u4e00\u4e9b\u4f7f\u7528",(0,o.kt)("strong",{parentName:"p"},"KOOK\u7ec4\u4ef6"),"\u7684\u7b80\u5355\u793a\u4f8b\u3002"),(0,o.kt)("h2",{id:"\u6587\u6863\u5217\u8868"},"\u6587\u6863\u5217\u8868"),(0,o.kt)(k,{items:(0,a.jA)().items,mdxType:"DocCardList"}))}N.isMDXComponent=!0}}]);