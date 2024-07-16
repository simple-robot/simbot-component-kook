[//]: # (Ktor 引擎选择)
<snippet id="engine-choose">

<deflist>
<def title="Ktor引擎">

你可以前往 [Ktor文档](https://ktor.io/docs/http-client-engines.html)
处选择一个对应所用平台下合适的 `Client Engine`。
这里会根据不同平台提供几个示例，你可以选择其他可用目标。

<tabs group="Platform">
<tab title="JVM" group-key="JVM">
<br />

[CIO](https://ktor.io/docs/http-client-engines.html#cio) 是一个比较通用的引擎。
在不知道选什么的情况下，可以考虑使用它。

<tabs group="Build">
<tab title="Gradle Kotlin DSL" group-key="kts">

```kotlin
runtimeOnly("io.ktor:ktor-client-cio-jvm:$ktor_version")
```

</tab>
<tab title="Gradle Groovy" group-key="groovy">

```groovy
runtimeOnly 'io.ktor:ktor-client-cio-jvm:$ktor_version'
```

</tab>
<tab title="Maven" group-key="maven">

```xml
<dependency>
    <groupId>io.ktor</groupId>
    <artifactId>ktor-client-cio-jvm</artifactId>
    <version>${ktor_version}</version>
    <scope>runtime</scope>
</dependency>
```

</tab>
</tabs>

<br />

如果你打算使用 Java11+，也可以选择 [Java](https://ktor.io/docs/http-client-engines.html#java) 引擎。

<br />

<tabs group="Build">
<tab title="Gradle Kotlin DSL" group-key="kts">

```kotlin
runtimeOnly("io.ktor:ktor-client-java:$ktor_version")
```

<br />

> 如果你想要显式配置引擎，那么就不能使用 `runtimeOnly` 了。

</tab>
<tab title="Gradle Groovy" group-key="groovy">

```groovy
runtimeOnly 'io.ktor:ktor-client-java:$ktor_version'
```

<br />

> 如果你想要显式配置引擎，那么就不能使用 `runtimeOnly` 了。

</tab>
<tab title="Maven" group-key="maven">

```xml
<dependency>
    <groupId>io.ktor</groupId>
    <artifactId>ktor-client-java-jvm</artifactId>
    <version>${ktor_version}</version>
    <scope>runtime</scope>
</dependency>
```

<br />

> 如果你想要显式配置引擎，那么就不能使用 `runtime scope` 了。

</tab>
</tabs>

</tab>

<tab title="JS" group-key="JS">
<br />

JavaScript 平台下可以选择 [Js](https://ktor.io/docs/http-client-engines.html#js) 引擎。

<tabs group="Build">
<tab title="Gradle Kotlin DSL" group-key="kts">

```kotlin
implementation("io.ktor:ktor-client-js:$ktor_version")
```

</tab>
<tab title="Gradle Groovy" group-key="groovy">

```groovy
implementation 'io.ktor:ktor-client-js:$ktor_version'
```

</tab>
</tabs>

</tab>

<tab title="Native" group-key="Native">
<br />

native 平台目标下，可能需要根据不同的平台类型选择不同的引擎。

<tabs group="NativePlatform">
<tab title="Mingw">
<br />

可以选择 [WinHttp](https://ktor.io/docs/http-client-engines.html#winhttp) 引擎。

<tabs group="Build">
<tab title="Gradle Kotlin DSL" group-key="kts">

```kotlin
implementation("io.ktor:ktor-client-winhttp:$ktor_version")
```

</tab>
<tab title="Gradle Groovy" group-key="groovy">

```groovy
implementation 'io.ktor:ktor-client-winhttp:$ktor_version'
```

</tab>
</tabs>

</tab>
<tab title="Linux">
<br />

Linux 下依旧可以选择 [CIO](https://ktor.io/docs/http-client-engines.html#cio) 引擎。

<tabs group="Build">
<tab title="Gradle Kotlin DSL" group-key="kts">

```kotlin
implementation("io.ktor:ktor-client-cio:$ktor_version")
```

</tab>
<tab title="Gradle Groovy" group-key="groovy">

```groovy
implementation 'io.ktor:ktor-client-cio:$ktor_version'
```

</tab>
</tabs>

</tab>
<tab title="MacOS">
<br />

可以选择 [Darwin](https://ktor.io/docs/http-client-engines.html#darwin) 引擎。

<tabs group="Build">
<tab title="Gradle Kotlin DSL" group-key="kts">

```kotlin
implementation("io.ktor:ktor-client-darwin:$ktor_version")
```

</tab>
<tab title="Gradle Groovy" group-key="groovy">

```groovy
implementation 'io.ktor:ktor-client-darwin:$ktor_version'
```

</tab>
</tabs>

</tab>
</tabs>

</tab>
</tabs>

</def>
</deflist>


</snippet>


<snippet id="need-help">

<warning>

文档可能有所疏漏或存在一些待更新的内容。如有需要，欢迎前往[**社群**](https://simbot.forte.love/communities.html)
交流讨论、寻求帮助~

</warning>

</snippet>

<procedure title="公告" id="to-main-doc">

<h2>站点迁移啦~！</h2>

<warning>

KOOK组件的文档内容已经迁移合并到了[**核心库手册**](https://simbot.forte.love/component-kook.html)中！
本站点将不再更新，直接前往核心库手册的[**KOOK组件**](https://simbot.forte.love/component-kook.html)部分吧~！

</warning>

<procedure title="为什么迁移?" id="Why" collapsible="true">

作为由我们官方维护的组件库，分散在各自的文档站点中的确有好处：它们可以各自维护自己所需的东西、互不干扰。

但是缺点也很明显：**太过分散。**

组件库与核心库之间的关系是比较**紧密**的，
我们希望你能够在一个站点内就可以查阅或搜索到所有你想要得知的信息。

</procedure>

</procedure>
