@file:Suppress("ObjectPropertyName")

import org.gradle.kotlin.dsl.PluginDependenciesSpecScope
import org.gradle.plugin.use.PluginDependencySpec

//region Dokka
const val DOKKA_PLUGIN_ID = "org.jetbrains.dokka"

inline val PluginDependenciesSpecScope.dokka: PluginDependencySpec
    get() = id(DOKKA_PLUGIN_ID)
//endregion

//region publish-plugin
const val NEXUS_PUBLISH = "io.github.gradle-nexus.publish-plugin"

inline val PluginDependenciesSpecScope.`nexus-publish`: PluginDependencySpec
    get() = id(NEXUS_PUBLISH)
//endregion