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

import love.forte.gradle.common.core.project.setup
import love.forte.gradle.common.core.repository.Repositories
import org.jetbrains.dokka.DokkaConfiguration
import org.jetbrains.dokka.gradle.DokkaTaskPartial
import java.net.URL

/*
 *  Copyright (c) 2022-2022 ForteScarlet <ForteScarlet@163.com>
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

plugins {
    `java-library`
    kotlin("jvm")
    kotlin("plugin.serialization")
    id("org.jetbrains.dokka")
    idea
}

setup(P)
if (isSnapshot()) {
    version = P.snapshotVersion.toString()
}

repositories {
    mavenCentral()
    maven {
        url = uri(Repositories.Snapshot.URL)
        mavenContent {
            snapshotsOnly()
        }
    }
}

dependencies {
    testImplementation(kotlin("test-junit5"))
}


tasks.getByName<Test>("test") {
    useJUnitPlatform()
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    kotlinOptions {
        javaParameters = true
        jvmTarget = "1.8"
        freeCompilerArgs = freeCompilerArgs + listOf("-Xjvm-default=all")
    }
}

kotlin {
    explicitApi()
    this.sourceSets.configureEach {
        languageSettings {
            optIn("kotlin.RequiresOptIn")
            // optIn("love.forte.simbot.ExperimentalSimbotApi")
        }
    }
}

tasks.withType<JavaCompile> {
    sourceCompatibility = "1.8"
    targetCompatibility = "1.8"
    options.encoding = "UTF-8"
}

configurations.all {
    // check for updates every build
    resolutionStrategy.cacheChangingModulesFor(0, "seconds")
}


//// show project info
logger.info("========================================================")
logger.info("== project.group:   ${group}")
logger.info("== project.name:    ${name}")
logger.info("== project.version: ${version}")
logger.info("========================================================")

idea {
    module {
        isDownloadSources = true
        isDownloadJavadoc = true
    }
}

//// dokka

tasks.withType<DokkaTaskPartial>().configureEach {
    dokkaSourceSets.configureEach {
        version = P.version.toString()
        documentedVisibilities.set(listOf(DokkaConfiguration.Visibility.PUBLIC, DokkaConfiguration.Visibility.PROTECTED))
        reportUndocumented.set(true)
        if (project.file("Module.md").exists()) {
            includes.from("Module.md")
        }
        
        
        // samples
        samples.from(
            project.files(),
            project.files("src/samples/samples"),
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
