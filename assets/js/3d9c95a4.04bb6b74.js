"use strict";(self.webpackChunkwebsite=self.webpackChunkwebsite||[]).push([[125],{3905:(e,t,r)=>{r.d(t,{Zo:()=>p,kt:()=>b});var n=r(7294);function a(e,t,r){return t in e?Object.defineProperty(e,t,{value:r,enumerable:!0,configurable:!0,writable:!0}):e[t]=r,e}function o(e,t){var r=Object.keys(e);if(Object.getOwnPropertySymbols){var n=Object.getOwnPropertySymbols(e);t&&(n=n.filter((function(t){return Object.getOwnPropertyDescriptor(e,t).enumerable}))),r.push.apply(r,n)}return r}function l(e){for(var t=1;t<arguments.length;t++){var r=null!=arguments[t]?arguments[t]:{};t%2?o(Object(r),!0).forEach((function(t){a(e,t,r[t])})):Object.getOwnPropertyDescriptors?Object.defineProperties(e,Object.getOwnPropertyDescriptors(r)):o(Object(r)).forEach((function(t){Object.defineProperty(e,t,Object.getOwnPropertyDescriptor(r,t))}))}return e}function i(e,t){if(null==e)return{};var r,n,a=function(e,t){if(null==e)return{};var r,n,a={},o=Object.keys(e);for(n=0;n<o.length;n++)r=o[n],t.indexOf(r)>=0||(a[r]=e[r]);return a}(e,t);if(Object.getOwnPropertySymbols){var o=Object.getOwnPropertySymbols(e);for(n=0;n<o.length;n++)r=o[n],t.indexOf(r)>=0||Object.prototype.propertyIsEnumerable.call(e,r)&&(a[r]=e[r])}return a}var s=n.createContext({}),u=function(e){var t=n.useContext(s),r=t;return e&&(r="function"==typeof e?e(t):l(l({},t),e)),r},p=function(e){var t=u(e.components);return n.createElement(s.Provider,{value:t},e.children)},c="mdxType",m={inlineCode:"code",wrapper:function(e){var t=e.children;return n.createElement(n.Fragment,{},t)}},d=n.forwardRef((function(e,t){var r=e.components,a=e.mdxType,o=e.originalType,s=e.parentName,p=i(e,["components","mdxType","originalType","parentName"]),c=u(r),d=a,b=c["".concat(s,".").concat(d)]||c[d]||m[d]||o;return r?n.createElement(b,l(l({ref:t},p),{},{components:r})):n.createElement(b,l({ref:t},p))}));function b(e,t){var r=arguments,a=t&&t.mdxType;if("string"==typeof e||a){var o=r.length,l=new Array(o);l[0]=d;var i={};for(var s in t)hasOwnProperty.call(t,s)&&(i[s]=t[s]);i.originalType=e,i[c]="string"==typeof e?e:a,l[1]=i;for(var u=2;u<o;u++)l[u]=r[u];return n.createElement.apply(null,l)}return n.createElement.apply(null,r)}d.displayName="MDXCreateElement"},5162:(e,t,r)=>{r.d(t,{Z:()=>l});var n=r(7294),a=r(6010);const o={tabItem:"tabItem_Ymn6"};function l(e){let{children:t,hidden:r,className:l}=e;return n.createElement("div",{role:"tabpanel",className:(0,a.Z)(o.tabItem,l),hidden:r},t)}},4866:(e,t,r)=>{r.d(t,{Z:()=>N});var n=r(7462),a=r(7294),o=r(6010),l=r(2466),i=r(6550),s=r(1980),u=r(7392),p=r(12);function c(e){return function(e){return a.Children.map(e,(e=>{if(!e||(0,a.isValidElement)(e)&&function(e){const{props:t}=e;return!!t&&"object"==typeof t&&"value"in t}(e))return e;throw new Error(`Docusaurus error: Bad <Tabs> child <${"string"==typeof e.type?e.type:e.type.name}>: all children of the <Tabs> component should be <TabItem>, and every <TabItem> should have a unique "value" prop.`)}))?.filter(Boolean)??[]}(e).map((e=>{let{props:{value:t,label:r,attributes:n,default:a}}=e;return{value:t,label:r,attributes:n,default:a}}))}function m(e){const{values:t,children:r}=e;return(0,a.useMemo)((()=>{const e=t??c(r);return function(e){const t=(0,u.l)(e,((e,t)=>e.value===t.value));if(t.length>0)throw new Error(`Docusaurus error: Duplicate values "${t.map((e=>e.value)).join(", ")}" found in <Tabs>. Every value needs to be unique.`)}(e),e}),[t,r])}function d(e){let{value:t,tabValues:r}=e;return r.some((e=>e.value===t))}function b(e){let{queryString:t=!1,groupId:r}=e;const n=(0,i.k6)(),o=function(e){let{queryString:t=!1,groupId:r}=e;if("string"==typeof t)return t;if(!1===t)return null;if(!0===t&&!r)throw new Error('Docusaurus error: The <Tabs> component groupId prop is required if queryString=true, because this value is used as the search param name. You can also provide an explicit value such as queryString="my-search-param".');return r??null}({queryString:t,groupId:r});return[(0,s._X)(o),(0,a.useCallback)((e=>{if(!o)return;const t=new URLSearchParams(n.location.search);t.set(o,e),n.replace({...n.location,search:t.toString()})}),[o,n])]}function f(e){const{defaultValue:t,queryString:r=!1,groupId:n}=e,o=m(e),[l,i]=(0,a.useState)((()=>function(e){let{defaultValue:t,tabValues:r}=e;if(0===r.length)throw new Error("Docusaurus error: the <Tabs> component requires at least one <TabItem> children component");if(t){if(!d({value:t,tabValues:r}))throw new Error(`Docusaurus error: The <Tabs> has a defaultValue "${t}" but none of its children has the corresponding value. Available values are: ${r.map((e=>e.value)).join(", ")}. If you intend to show no default tab, use defaultValue={null} instead.`);return t}const n=r.find((e=>e.default))??r[0];if(!n)throw new Error("Unexpected error: 0 tabValues");return n.value}({defaultValue:t,tabValues:o}))),[s,u]=b({queryString:r,groupId:n}),[c,f]=function(e){let{groupId:t}=e;const r=function(e){return e?`docusaurus.tab.${e}`:null}(t),[n,o]=(0,p.Nk)(r);return[n,(0,a.useCallback)((e=>{r&&o.set(e)}),[r,o])]}({groupId:n}),k=(()=>{const e=s??c;return d({value:e,tabValues:o})?e:null})();(0,a.useLayoutEffect)((()=>{k&&i(k)}),[k]);return{selectedValue:l,selectValue:(0,a.useCallback)((e=>{if(!d({value:e,tabValues:o}))throw new Error(`Can't select invalid tab value=${e}`);i(e),u(e),f(e)}),[u,f,o]),tabValues:o}}var k=r(2389);const h={tabList:"tabList__CuJ",tabItem:"tabItem_LNqP"};function g(e){let{className:t,block:r,selectedValue:i,selectValue:s,tabValues:u}=e;const p=[],{blockElementScrollPositionUntilNextRender:c}=(0,l.o5)(),m=e=>{const t=e.currentTarget,r=p.indexOf(t),n=u[r].value;n!==i&&(c(t),s(n))},d=e=>{let t=null;switch(e.key){case"Enter":m(e);break;case"ArrowRight":{const r=p.indexOf(e.currentTarget)+1;t=p[r]??p[0];break}case"ArrowLeft":{const r=p.indexOf(e.currentTarget)-1;t=p[r]??p[p.length-1];break}}t?.focus()};return a.createElement("ul",{role:"tablist","aria-orientation":"horizontal",className:(0,o.Z)("tabs",{"tabs--block":r},t)},u.map((e=>{let{value:t,label:r,attributes:l}=e;return a.createElement("li",(0,n.Z)({role:"tab",tabIndex:i===t?0:-1,"aria-selected":i===t,key:t,ref:e=>p.push(e),onKeyDown:d,onClick:m},l,{className:(0,o.Z)("tabs__item",h.tabItem,l?.className,{"tabs__item--active":i===t})}),r??t)})))}function v(e){let{lazy:t,children:r,selectedValue:n}=e;const o=(Array.isArray(r)?r:[r]).filter(Boolean);if(t){const e=o.find((e=>e.props.value===n));return e?(0,a.cloneElement)(e,{className:"margin-top--md"}):null}return a.createElement("div",{className:"margin-top--md"},o.map(((e,t)=>(0,a.cloneElement)(e,{key:t,hidden:e.props.value!==n}))))}function y(e){const t=f(e);return a.createElement("div",{className:(0,o.Z)("tabs-container",h.tabList)},a.createElement(g,(0,n.Z)({},e,t)),a.createElement(v,(0,n.Z)({},e,t)))}function N(e){const t=(0,k.Z)();return a.createElement(y,(0,n.Z)({key:String(t)},e))}},3191:(e,t,r)=>{r.r(t),r.d(t,{assets:()=>s,contentTitle:()=>l,default:()=>m,frontMatter:()=>o,metadata:()=>i,toc:()=>u});var n=r(7462),a=(r(7294),r(3905));r(4866),r(5162);const o={slug:"/",sidebar_position:1},l="\u9996\u9875",i={unversionedId:"home",id:"home",title:"\u9996\u9875",description:"\u6b22\u8fce\u6765\u5230 Simple Robot \uff08\u4e0b\u6587\u7b80\u79f0 simbot\uff09  \u7684KOOK\u673a\u5668\u4eba\u7ec4\u4ef6 \uff08\u4e0b\u6587\u7b80\u79f0 KOOK\u7ec4\u4ef6\uff09 \u6587\u6863\u3002",source:"@site/docs/home.md",sourceDirName:".",slug:"/",permalink:"/simbot-component-kook/docs/",draft:!1,editUrl:"https://github.com/simple-robot/simbot-component-kook/tree/dev/main/website/docs/home.md",tags:[],version:"current",lastUpdatedAt:1694010938,formattedLastUpdatedAt:"2023\u5e749\u67086\u65e5",sidebarPosition:1,frontMatter:{slug:"/",sidebar_position:1},sidebar:"tutorialSidebar",next:{title:"\u5feb\u901f\u5f00\u59cb",permalink:"/simbot-component-kook/docs/quick-start/"}},s={},u=[{value:"\u6a21\u5757\u7b80\u4ecb",id:"\u6a21\u5757\u7b80\u4ecb",level:2},{value:"API\u6a21\u5757",id:"api\u6a21\u5757",level:3},{value:"stdlib \u6807\u51c6\u5e93\u6a21\u5757",id:"stdlib-\u6807\u51c6\u5e93\u6a21\u5757",level:3},{value:"core \u6838\u5fc3\u5e93",id:"core-\u6838\u5fc3\u5e93",level:3}],p={toc:u},c="wrapper";function m(e){let{components:t,...r}=e;return(0,a.kt)(c,(0,n.Z)({},p,r,{components:t,mdxType:"MDXLayout"}),(0,a.kt)("h1",{id:"\u9996\u9875"},"\u9996\u9875"),(0,a.kt)("p",null,"\u6b22\u8fce\u6765\u5230 ",(0,a.kt)("a",{parentName:"p",href:"https://github.com/simple-robot/simpler-robot"},"Simple Robot")," ",(0,a.kt)("small",null,"\uff08\u4e0b\u6587\u7b80\u79f0 ",(0,a.kt)("i",null,(0,a.kt)("inlineCode",{parentName:"p"},"simbot")),"\uff09"),"  \u7684KOOK\u673a\u5668\u4eba\u7ec4\u4ef6 ",(0,a.kt)("small",null,"\uff08\u4e0b\u6587\u7b80\u79f0 ",(0,a.kt)("i",null,(0,a.kt)("inlineCode",{parentName:"p"},"KOOK\u7ec4\u4ef6")),"\uff09")," \u6587\u6863\u3002"),(0,a.kt)("p",null,"KOOK\u7ec4\u4ef6\u662f\u57fa\u4e8e ",(0,a.kt)("a",{parentName:"p",href:"https://github.com/simple-robot/simpler-robot"},"simbot\u6838\u5fc3\u5e93")," \u5bf9 ",(0,a.kt)("a",{parentName:"p",href:"https://developer.kookapp.cn/doc/reference"},"KOOK\u673a\u5668\u4eba")," \u7684\u5b9e\u73b0\u3002"),(0,a.kt)("p",null,"KOOK\u7ec4\u4ef6\u7531Kotlin\u8bed\u8a00\u7f16\u5199\uff0c\u4e0d\u540c\u7684\u6a21\u5757\u5206\u522b\u57fa\u4e8e ",(0,a.kt)("strong",{parentName:"p"},"KMP (Kotlin Multiplatform)")," \u6216 ",(0,a.kt)("strong",{parentName:"p"},"Kotlin/JVM")," \u6784\u5efa\u3002\n\u5728JVM\u5e73\u53f0\u4e0a\u5bf9 Java \u53cb\u597d\uff0c\u5e76\u57fa\u4e8e KMP \u63d0\u4f9b\u66f4\u591a\u5e73\u53f0\u7684\u53ef\u80fd\u6027\u3002"),(0,a.kt)("admonition",{title:"\u57fa\u672c\u529f\u80fd",type:"info"},(0,a.kt)("p",{parentName:"admonition"},"\u5bf9\u4e8esimbot\u7edd\u5927\u591a\u6570\u7684\u6807\u51c6\u3001\u57fa\u672c\u529f\u80fd\u7684\u4ecb\u7ecd\u90fd\u5728 ",(0,a.kt)("a",{parentName:"p",href:"https://simbot.forte.love/"},"simbot\u5b98\u7f51")," \u4e2d\u3002")),(0,a.kt)("h2",{id:"\u6a21\u5757\u7b80\u4ecb"},"\u6a21\u5757\u7b80\u4ecb"),(0,a.kt)("p",null,"simbot\u7684KOOK\u673a\u5668\u4eba\u7ec4\u4ef6\u6574\u4e2a\u9879\u76ee\u5206\u4e3a\u4e09\u4e2a\u4e3b\u8981\u6a21\u5757\u3002\u5b83\u4eec\u5206\u522b\u662f ",(0,a.kt)("strong",{parentName:"p"},"API\u6a21\u5757"),"\u3001",(0,a.kt)("strong",{parentName:"p"},"stdlib\uff08\u6807\u51c6\u5e93\uff09\u6a21\u5757"),"\u548c",(0,a.kt)("strong",{parentName:"p"},"core\uff08\u6838\u5fc3\u5e93\uff09\u6a21\u5757"),"\u3002"),(0,a.kt)("h3",{id:"api\u6a21\u5757"},"API\u6a21\u5757"),(0,a.kt)("admonition",{title:"\u6982\u8981",type:"note"},(0,a.kt)("p",{parentName:"admonition"},"API\u6a21\u5757\u57fa\u4e8e ",(0,a.kt)("strong",{parentName:"p"},"KMP")," \u6784\u5efa\u9879\u76ee\uff0c\u652f\u6301 ",(0,a.kt)("strong",{parentName:"p"},"JVM\u3001JS\u3001Native")," \u5e73\u53f0\uff0c\u4f7f\u7528 ",(0,a.kt)("a",{parentName:"p",href:"https://ktor.io/"},(0,a.kt)("strong",{parentName:"a"},"Ktor"))," \u4f5c\u4e3aAPI\u8bf7\u6c42\uff08http\u8bf7\u6c42\uff09\u7684\u89e3\u51b3\u65b9\u6848\u3002")),(0,a.kt)("p",null,"API\u6a21\u5757\u7684\u4e3b\u8981\u4f5c\u7528\u662f\u63d0\u4f9b\u9488\u5bf9KOOK\u673a\u5668\u4eba\u5f00\u53d1\u5e73\u53f0\u4e2d\u7684\u5404API\u548c\u4e8b\u4ef6\u7c7b\u578b\u7684\u5e95\u5c42\u5c01\u88c5\u3002\u6b64\u6a21\u5757",(0,a.kt)("strong",{parentName:"p"},"\u4e0d\u63d0\u4f9b"),"\u8fc7\u5ea6\u7684\u529f\u80fd\u6027\u5c01\u88c5\uff0c\n\u4e3b\u8981\u5b97\u65e8\u4e3a\u5728\u98ce\u683c\u7edf\u4e00\u7684\u60c5\u51b5\u4e0b\u5c06API\u548c\u4e8b\u4ef6",(0,a.kt)("strong",{parentName:"p"},"\u63cf\u8ff0"),"\u4e3a\u53ef\u4f9b\u4f7f\u7528\u7684\u4f9d\u8d56\u5e93\u3002"),(0,a.kt)("h3",{id:"stdlib-\u6807\u51c6\u5e93\u6a21\u5757"},"stdlib \u6807\u51c6\u5e93\u6a21\u5757"),(0,a.kt)("admonition",{title:"\u6982\u8981",type:"note"},(0,a.kt)("p",{parentName:"admonition"},"\u6807\u51c6\u5e93\u6a21\u5757\u57fa\u4e8e ",(0,a.kt)("strong",{parentName:"p"},"KMP")," \u6784\u5efa\u9879\u76ee\uff0c\u652f\u6301 ",(0,a.kt)("strong",{parentName:"p"},"JVM\u3001JS\u3001Native")," \u5e73\u53f0\uff0c\u4f7f\u7528 ",(0,a.kt)("a",{parentName:"p",href:"https://ktor.io/"},(0,a.kt)("strong",{parentName:"a"},"Ktor"))," \u4f5c\u4e3aAPI\u8bf7\u6c42\uff08http\u8bf7\u6c42\uff09\u548c ws \u4e8b\u4ef6\u8ba2\u9605\u7684\u89e3\u51b3\u65b9\u6848\u3002")),(0,a.kt)("p",null,"\u6807\u51c6\u5e93\u6a21\u5757\u4f9d\u8d56API\u6a21\u5757\uff0c\u5728\u6b64\u57fa\u7840\u4e0a\u989d\u5916\u63d0\u4f9bKOOK\u4e2d ",(0,a.kt)("strong",{parentName:"p"},"Bot")," \u6982\u5ff5\u7684\u5c01\u88c5\u4e0e\u80fd\u529b\u5b9e\u73b0\uff0c\u8fbe\u5230\u5bf9\u4e00\u4e2a Bot \u7684\u4e8b\u4ef6\u8ba2\u9605\u3001\u6d88\u606f\u53d1\u9001\u7b49\u80fd\u529b\u3002\n\u4e0eAPI\u6a21\u5757\u7c7b\u578b\uff0c\u6807\u51c6\u5e93\u6a21\u5757\u7684\u4e3b\u8981\u5b97\u65e8\u540c\u6837\u662f\u5728\u98ce\u683c\u7edf\u4e00\u7684\u60c5\u51b5\u4e0b\u5c06Bot\u4e0e\u4e8b\u4ef6\u8ba2\u9605\u7684\u80fd\u529b",(0,a.kt)("strong",{parentName:"p"},"\u63cf\u8ff0"),"\u4e3a\u53ef\u4f9b\u4f7f\u7528\u7684\u4f9d\u8d56\u5e93\u3002"),(0,a.kt)("hr",null),(0,a.kt)("admonition",{title:"\u72ec\u7acb ",type:"tip"},(0,a.kt)("p",{parentName:"admonition"},(0,a.kt)("strong",{parentName:"p"},"API\u6a21\u5757"),"\u548c",(0,a.kt)("strong",{parentName:"p"},"\u6807\u51c6\u5e93\u6a21\u5757"),"\u4e0e",(0,a.kt)("strong",{parentName:"p"},"simbot"),"\u7684\u5173\u7cfb\u4e3b\u8981\u4f53\u73b0\u5728\u8f83\u4e3a\u7edf\u4e00\u7684",(0,a.kt)("strong",{parentName:"p"},"\u98ce\u683c"),"\u4e0a\u3002\u5b9e\u8d28\u4e0a\u8fd9\u4e24\u4e2a\u6a21\u5757",(0,a.kt)("strong",{parentName:"p"},"\u4e0d\u76f4\u63a5\u4f9d\u8d56"),"\u4e0esimbot\u76f8\u5173\u7684\u5e93\u3002\uff08\u53ef\u80fd\u5b58\u5728\u90e8\u5206\u4ec5\u7f16\u8bd1\u4f9d\u8d56\u6216\u7f16\u8bd1\u5668\u63d2\u4ef6\u4f9d\u8d56\uff09"),(0,a.kt)("p",{parentName:"admonition"},"\u5b83\u4eec\u4e24\u4e2a\u662f\u53ef\u4ee5\u5b8c\u5168\u4f5c\u4e3a\u72ec\u7acb\u7684\u5e95\u5c42API\u4f9d\u8d56\u5e93\u4f7f\u7528\u7684\u3002")),(0,a.kt)("h3",{id:"core-\u6838\u5fc3\u5e93"},"core \u6838\u5fc3\u5e93"),(0,a.kt)("admonition",{title:"\u6982\u8981",type:"note"},(0,a.kt)("p",{parentName:"admonition"},"\u6838\u5fc3\u5e93\u6a21\u5757\u57fa\u4e8e ",(0,a.kt)("strong",{parentName:"p"},"Kotlin/JVM")," \u6784\u5efa\u9879\u76ee\uff0c\u652f\u6301 ",(0,a.kt)("strong",{parentName:"p"},"JVM")," \u5e73\u53f0\uff0c\u517c\u5bb9\u5e76\u63d0\u4f9b\u53cb\u597d\u7684Java API\u3002")),(0,a.kt)("p",null,"\u6838\u5fc3\u5e93\u6a21\u5757\u662f\u5bf9",(0,a.kt)("a",{parentName:"p",href:"https://github.com/simple-robot/simpler-robot"},"simbot\u6838\u5fc3\u5e93"),"\u7684KOOK\u673a\u5668\u4eba\u5b9e\u73b0\uff0c\u4e5f\u662f\u6b64\u9879\u76ee\u4f5c\u4e3a",(0,a.kt)("strong",{parentName:"p"},"\u201csimbot\u7ec4\u4ef6\u201d"),"\u7684\u4e3b\u8981\u4f53\u73b0\u3002"),(0,a.kt)("p",null,"\u6838\u5fc3\u5e93\u6a21\u5757\u4f9d\u8d56\u5e76\u5b9e\u73b0 ",(0,a.kt)("strong",{parentName:"p"},"simbot API"),"\uff0c\u9488\u5bf9\u5176\u5b9a\u4e49\u7684\u5404\u7c7b\u578b\u6765\u63d0\u4f9bsimbot\u98ce\u683c\u7684 KOOK API \u5b9e\u73b0\u3002\u4f8b\u5982\u5b9e\u73b0 simbot \u63d0\u4f9b\u7684 ",(0,a.kt)("inlineCode",{parentName:"p"},"Bot")," \u7c7b\u578b\u4e3a ",(0,a.kt)("inlineCode",{parentName:"p"},"KookBot")," \u5e76\u63d0\u4f9bKOOK\u7ec4\u4ef6\u4e0b\u7684\u5404\u79cd\u72ec\u7279\u80fd\u529b\u3002"),(0,a.kt)("p",null,"\u6838\u5fc3\u5e93\u6a21\u5757\u662f\u4e00\u79cd\u9ad8\u7ea7\u5c01\u88c5\uff0c\u5b83\u4f1a\u501f\u52a9 simbot API \u5f3a\u5927\u7684\u80fd\u529b\u6765\u63d0\u4f9b\u5927\u91cf\u9ad8\u7ea7\u529f\u80fd\uff0c\u4f8b\u5982\u5bf9\u4e8b\u4ef6\u7684\u8ba2\u9605\u548c\u66f4\u4fbf\u6377\u7684\u6d88\u606f\u53d1\u9001\u3001\u5bf9 Spring Boot \u7684\u652f\u6301\u7b49\u3002"),(0,a.kt)("p",null,"\u6838\u5fc3\u5e93\u6a21\u5757\u4f1a\u5c3d\u53ef\u80fd\u5c4f\u853d\u6389",(0,a.kt)("strong",{parentName:"p"},"\u5e95\u5c42API"),"\uff08\u4e0a\u8ff0\u4e24\u4e2a\u6a21\u5757\uff09\uff0c\u4f7f\u5176\u5bf9\u5f00\u53d1\u8005\u900f\u660e\uff0c\u53d6\u800c\u4ee3\u4e4b\u7684\u662f\u66f4\u52a0\u6e05\u6670\u660e\u4e86\u7684API\u3002"),(0,a.kt)("admonition",{title:"\u6982\u8981",type:"note"},(0,a.kt)("p",{parentName:"admonition"},"\u5f53\u7136\uff0c\u5bf9\u4e8e\u4e00\u4e9b\u7279\u6b8a\u573a\u666f\u6216\u4e0d\u5f97\u5df2\u7684\u60c5\u51b5\uff0c\u5f00\u53d1\u8005\u4f9d\u65e7\u53ef\u4ee5\u5f88\u8f7b\u677e\u7684\u4f7f\u7528\u5e95\u5c42API\u6765\u8fbe\u6210\u6240\u6c42\u76ee\u7684\u3002")))}m.isMDXComponent=!0}}]);