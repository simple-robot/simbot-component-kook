/*
 * Copyright (c) 2022-2023. ForteScarlet.
 *
 * This file is part of simbot-component-kook.
 *
 * simbot-component-kook is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * simbot-component-kook is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with simbot-component-kook, If not, see <https://www.gnu.org/licenses/>.
 */

import org.gradle.api.plugins.JavaBasePlugin
import java.io.File
import java.time.Year
import org.jetbrains.dokka.base.DokkaBase
import org.jetbrains.dokka.base.DokkaBaseConfiguration


/*
 使用在根配置，配置dokka多模块
 */

plugins {
    id("org.jetbrains.dokka")
}

repositories {
    mavenCentral()
}

fun org.jetbrains.dokka.gradle.AbstractDokkaTask.configOutput(format: String) {
    moduleName.set("Simple Robot Component KOOK")
    outputDirectory.set(rootProject.file("build/dokka/$format"))
}

tasks.named<org.jetbrains.dokka.gradle.DokkaMultiModuleTask>("dokkaHtmlMultiModule") {
    configOutput("html")
    
//    includes.from(rootProject.file("docs/DocHome.md"))
    includes.from(rootProject.file("README.md"))
    
    pluginConfiguration<DokkaBase, DokkaBaseConfiguration> {
        customAssets = listOf(rootProject.file(".simbot/dokka-assets/logo-icon.svg"))
        customStyleSheets = listOf(rootProject.file(".simbot/dokka-assets/css/kdoc-style.css"))
        footerMessage = "© 2021-${Year.now().value} <a href='https://github.com/simple-robot'>Simple Robot</a>, <a href='https://github.com/ForteScarlet'>ForteScarlet</a>. All rights reserved."
        separateInheritedMembers = true
    }
}

