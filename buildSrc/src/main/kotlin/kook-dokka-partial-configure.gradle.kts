/*
 * Copyright (c) 2023. ForteScarlet.
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

import org.jetbrains.dokka.DokkaConfiguration
import org.jetbrains.dokka.gradle.DokkaTaskPartial
import java.net.URL

plugins {
    id("org.jetbrains.dokka")
}


// dokka config
tasks.withType<DokkaTaskPartial>().configureEach {
    dokkaSourceSets.configureEach {
        version = P.version.toString()
        documentedVisibilities.set(listOf(DokkaConfiguration.Visibility.PUBLIC, DokkaConfiguration.Visibility.PROTECTED))
        fun checkModule(projectFileName: String): Boolean {
            val moduleMdFile = project.file(projectFileName)
            if (moduleMdFile.exists()) {
                moduleMdFile.useLines { lines ->
                    val head = lines.first { it.isNotBlank() }.trim()
                    if (head == "# Module ${project.name}") {
                        includes.from(projectFileName)
                        return true
                    }
                }
            }

            return false
        }

        if (!checkModule("Module.md")) {
            checkModule("README.md")
        }

        // samples
        samples.from(
            project.files(),
            project.files("src/samples"),
        )

        sourceLink {
            localDirectory.set(projectDir.resolve("src"))
            val relativeTo = projectDir.relativeTo(rootProject.projectDir)
            remoteUrl.set(URL("${P.HOMEPAGE}/tree/main/$relativeTo/src"))
            remoteLineSuffix.set("#L")
        }

        perPackageOption {
            matchingRegex.set(".*internal.*") // will match all .internal packages and sub-packages
            suppress.set(true)
        }


        fun externalDocumentation(docUrl: URL) {
            externalDocumentationLink {
                url.set(docUrl)
                packageListUrl.set(URL(docUrl, "${docUrl.path}/package-list"))
            }
        }

        // kotlin-coroutines doc
        externalDocumentation(URL("https://kotlinlang.org/api/kotlinx.coroutines"))

        // kotlin-serialization doc
        externalDocumentation(URL("https://kotlinlang.org/api/kotlinx.serialization"))

        // ktor
        externalDocumentation(URL("https://api.ktor.io"))

        // simbot doc
        externalDocumentation(URL("https://docs.simbot.forte.love/main"))

    }
}
