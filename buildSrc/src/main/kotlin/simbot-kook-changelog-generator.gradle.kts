/*
 * Copyright (c) 2022-2023. ForteScarlet.
 *
 * This file is part of simbot-component-kook.
 *
 * simbot-component-kook is free software: you can redistribute it and/or modify it under the terms of
 * the GNU Lesser General Public License as published by the Free Software Foundation,
 * either version 3 of the License, or (at your option) any later version.
 *
 * simbot-component-kook is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with simbot-component-kook,
 * If not, see <https://www.gnu.org/licenses/>.
 */

import changelog.generateChangelog


tasks.create("createChangelog") {
    group = "documentation"
    doFirst {
        generateChangelog("v${P.version}")
//        println("Generate change log for $version ...")
//        // configurations.runtimeClasspath
//        val changelogDir = rootProject.file(".changelog").also {
//            it.mkdirs()
//        }
//        val file = File(changelogDir, "$version.md")
//        if (!file.exists()) {
//            file.createNewFile()
//            val coreVersion = simbotVersion.toString()
//            val autoGenerateText = """
//
//                **部分依赖库版本参考**
//
//                | **库** | **版本** |
//                |---------|--------|
//                | 核心库 | [**v$coreVersion**](https://github.com/ForteScarlet/simpler-robot/releases/tag/v$coreVersion) |
//
//                > **Warning**
//                当前版本处于 **`ALPHA`**阶段，仍旧有很多[**已知问题**](https://github.com/simple-robot/simbot-component-kook/issues/)和可能存在的**潜在问题**，
//                如有发现问题请积极[反馈](https://github.com/simple-robot/simbot-component-kook/issues/)或 [协助我们解决](https://github.com/simple-robot/simbot-component-kook/pulls)，非常感谢！
//
//
//            """.trimIndent()
//
//
//            file.writeText(autoGenerateText)
//        }
    }
}

tasks.create("updateWebsiteVersionJson") {
    group = "documentation"
    doFirst {
        val version = P.version.toString()

        val websiteVersionJsonDir = rootProject.file("website/static")
        if (!websiteVersionJsonDir.exists()) {
            websiteVersionJsonDir.mkdirs()
        }
        val websiteVersionJsonFile = File(websiteVersionJsonDir, "version.json")
        if (!websiteVersionJsonFile.exists()) {
            websiteVersionJsonFile.createNewFile()
        }

        websiteVersionJsonFile.writeText(
            """
            {
              "version": "$version"
            }
        """.trimIndent()
        )
    }
}

//fun repoRow(moduleName: String, group: String, id: String, version: String): String {
//    return "| $moduleName | [$moduleName: v$version](https://repo1.maven.org/maven2/${group.replace(".", "/")}/${id.replace(".", "/")}/$version) | [$moduleName: v$version](https://search.maven.org/artifact/$group/$id/$version/jar)  |"
//}
