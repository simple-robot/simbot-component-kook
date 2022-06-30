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

import util.getProp

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

plugins {
    // `java-library`
    kotlin("jvm") version "1.6.21" apply false
    kotlin("plugin.serialization") version "1.6.21" apply false
    id("org.jetbrains.dokka") version "1.6.21"
    `maven-publish`
    signing
    // see https://github.com/gradle-nexus/publish-plugin
    id("io.github.gradle-nexus.publish-plugin") version "1.1.0"
}

group = P.ComponentKaiheila.GROUP
version = P.ComponentKaiheila.VERSION
description = "Simple Robot框架下针对开黑啦(Kook)平台的组件实现"

println("=== Current version: $version ===")



repositories {
    mavenLocal()
    mavenCentral()
    maven {
        url = uri(Sonatype.`snapshot-oss`.URL)
        mavenContent {
            snapshotsOnly()
        }
    }
}


val isSnapshotOnly = System.getProperty("snapshotOnly") != null
val isReleaseOnly = System.getProperty("releaseOnly") != null

val isPublishConfigurable = when {
    isSnapshotOnly -> P.ComponentKaiheila.isSnapshot
    isReleaseOnly -> !P.ComponentKaiheila.isSnapshot
    else -> true
}


println("isSnapshotOnly: $isSnapshotOnly")
println("isReleaseOnly: $isReleaseOnly")
println("isPublishConfigurable: $isPublishConfigurable")


subprojects {
    group = P.ComponentKaiheila.GROUP
    version = P.ComponentKaiheila.VERSION

    apply(plugin = "java")

    repositories {
        mavenLocal()
        mavenCentral()
        maven {
            url = uri(Sonatype.`snapshot-oss`.URL)
            mavenContent {
                snapshotsOnly()
            }
        }
    }

    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
        kotlinOptions {
            freeCompilerArgs = freeCompilerArgs + listOf("-Xjvm-default=all")
        }
    }

    if (isPublishConfigurable) {
        apply(plugin = "maven-publish")
        apply(plugin = "signing")
        configurePublishing(name)
        println("[publishing-configure] - [$name] configured.")
        // set gpg file path to root
        val secretKeyRingFileKey = "signing.secretKeyRingFile"
        val secretRingFile = File(project.rootDir, getProp(secretKeyRingFileKey)?.toString() ?: "ForteScarlet.gpg")
        extra[secretKeyRingFileKey] = secretRingFile
        setProperty(secretKeyRingFileKey, secretRingFile)

        signing {
            sign(publishing.publications)
        }

    }

    configurations.all {
        // check for updates every build
        resolutionStrategy.cacheChangingModulesFor(0, "seconds")
    }
}


tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}



if (isPublishConfigurable) {
    val sonatypeUsername: String? = getProp("sonatype.username")?.toString()
    val sonatypePassword: String? = getProp("sonatype.password")?.toString()

    if (sonatypeUsername != null && sonatypePassword != null) {
        nexusPublishing {
            packageGroup.set(P.ComponentKaiheila.GROUP)

            useStaging.set(
                project.provider { !project.version.toString().endsWith("SNAPSHOT", ignoreCase = true) }
            )

            transitionCheckOptions {
                maxRetries.set(20)
                delayBetween.set(java.time.Duration.ofSeconds(5))
            }

            repositories {
                sonatype {
                    snapshotRepositoryUrl.set(uri(Sonatype.`snapshot-oss`.URL))
                    username.set(sonatypeUsername)
                    password.set(sonatypePassword)
                }

            }
        }
    } else {
        println("[WARN] - sonatype.username or sonatype.password is null, cannot config nexus publishing.")
    }
}


fun org.jetbrains.dokka.gradle.AbstractDokkaTask.configOutput(format: String) {
    outputDirectory.set(rootProject.file("dokka/$format/v$version"))
}

tasks.dokkaHtmlMultiModule.configure {
    configOutput("html")
}
tasks.dokkaGfmMultiModule.configure {
    configOutput("gfm")
}

tasks.register("dokkaHtmlMultiModuleAndPost") {
    group = JavaBasePlugin.DOCUMENTATION_GROUP
    dependsOn("dokkaHtmlMultiModule")
    doLast {
        val outDir = rootProject.file("dokka/html")
        val indexFile = File(outDir, "index.html")
        indexFile.createNewFile()
        indexFile.writeText(
            """
            <html xmlns="http://www.w3.org/1999/xhtml">
            <head>
                <meta http-equiv="refresh" content="0;URL='v$version'" />
            </head>
            <body>
            </body>
            </html>
        """.trimIndent()
        )

        // TODO readme
    }
}

tasks.create("createChangelog") {
    group = "build"
    doFirst {
        val version = "v${P.ComponentKaiheila.version.fullVersion(false)}"
        println("Generate change log for $version ...")
        // configurations.runtimeClasspath
        val changelogDir = rootProject.file(".changelog").also {
            it.mkdirs()
        }
        val file = File(changelogDir, "$version.md")
        if (!file.exists()) {
            file.createNewFile()
            val coreVersion = P.Simbot.version.fullVersion(false)
            val autoGenerateText = """
                > 对应核心版本: [v$coreVersion](https://github.com/ForteScarlet/simpler-robot/releases/tag/v$coreVersion)

                
            """.trimIndent()


            file.writeText(autoGenerateText)
        }


    }
}




configurations.all {
    // check for updates every build
    resolutionStrategy.cacheChangingModulesFor(0, "seconds")
}