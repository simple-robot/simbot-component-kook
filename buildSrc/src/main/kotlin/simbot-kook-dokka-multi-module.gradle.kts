/*
 *  Copyright (c) 2022 ForteScarlet <ForteScarlet@163.com>
 *
 *  本文件是 simbot-component-kook 的一部分。
 *
 *  simbot-component-kook 是自由软件：你可以再分发之和/或依照由自由软件基金会发布的 GNU 通用公共许可证修改之，无论是版本 3 许可证，还是（按你的决定）任何以后版都可以。
 *
 *  发布 simbot-component-kook 是希望它能有用，但是并无保障;甚至连可销售和符合某个特定的目的都不保证。请参看 GNU 通用公共许可证，了解详情。
 *
 *  你应该随程序获得一份 GNU 通用公共许可证的复本。如果没有，请看:
 *  https://www.gnu.org/licenses
 *  https://www.gnu.org/licenses/gpl-3.0-standalone.html
 *  https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 *
 *
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

