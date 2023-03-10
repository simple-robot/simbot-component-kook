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


tasks.create("createChangelog") {
    group = "build"
    doFirst {
        val realVersion = P.version.toString()
        val version = "v$realVersion"
        println("Generate change log for $version ...")
        // configurations.runtimeClasspath
        val changelogDir = rootProject.file(".changelog").also {
            it.mkdirs()
        }
        val file = File(changelogDir, "$version.md")
        if (!file.exists()) {
            file.createNewFile()
            val coreVersion = simbotVersion.toString()
            val autoGenerateText = """
                > 对应核心版本: [**v$coreVersion**](https://github.com/ForteScarlet/simpler-robot/releases/tag/v$coreVersion)
                
                **⚠️注意:**

                当前版本处于 **`ALPHA`**阶段，仍旧有很多[**已知问题**](https://github.com/simple-robot/simbot-component-kook/issues/)和可能存在的**潜在问题**，
                如有发现问题请积极[反馈](https://github.com/simple-robot/simbot-component-kook/issues/)或 [协助我们解决](https://github.com/simple-robot/simbot-component-kook/pulls)，非常感谢！

                
                **仓库参考:**
                
                | **模块** | **repo1.maven** | **search.maven** |
                |---------|-----------------|------------------|
                ${repoRow("simbot-kook-api", "love.forte.simbot.component", "simbot-component-kook-api", realVersion)}
                ${repoRow("simbot-kook-stdlib", "love.forte.simbot.component", "simbot-component-kook-stdlib", realVersion)}
                ${repoRow("simbot-kook-core", "love.forte.simbot.component", "simbot-component-kook-core", realVersion)}
                
            """.trimIndent()


            file.writeText(autoGenerateText)
        }
    }
}


fun repoRow(moduleName: String, group: String, id: String, version: String): String {
    return "| $moduleName | [$moduleName: v$version](https://repo1.maven.org/maven2/${group.replace(".", "/")}/${id.replace(".", "/")}/$version) | [$moduleName: v$version](https://search.maven.org/artifact/$group/$id/$version/jar)  |"
}
