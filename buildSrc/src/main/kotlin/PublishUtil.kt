/*
 *  Copyright (c) 2021-2022 ForteScarlet <ForteScarlet@163.com>
 *
 *  本文件是 simbot-component-kaiheila 的一部分。
 *
 *  simbot-component-kaiheila 是自由软件：你可以再分发之和/或依照由自由软件基金会发布的 GNU 通用公共许可证修改之，无论是版本 3 许可证，还是（按你的决定）任何以后版都可以。
 *
 *  发布 simbot-component-kaiheila 是希望它能有用，但是并无保障;甚至连可销售和符合某个特定的目的都不保证。请参看 GNU 通用公共许可证，了解详情。
 *
 *  你应该随程序获得一份 GNU 通用公共许可证的复本。如果没有，请看:
 *  https://www.gnu.org/licenses
 *  https://www.gnu.org/licenses/gpl-3.0-standalone.html
 *  https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 *
 *
 */

@file:Suppress("NOTHING_TO_INLINE")

import org.gradle.api.*
import org.gradle.api.plugins.*
import org.gradle.api.publish.*
import org.gradle.api.publish.maven.*
import org.gradle.api.tasks.*
import org.gradle.api.tasks.bundling.*
import org.gradle.kotlin.dsl.*
import util.*


inline fun Project.configurePublishing(artifactId: String) {

    val sourcesJar by tasks.registering(Jar::class) {
        archiveClassifier.set("sources")
        from(sourceSets["main"].allSource)
    }
    // val sourcesJar = tasks["sourcesJar"]
    val javadocJar = tasks.register("javadocJar", Jar::class) {
        @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
        archiveClassifier.set("javadoc")
    }

    publishing {
        publications {
            register("mavenJava", MavenPublication::class) {
                from(components["java"])

                groupId = project.group.toString()
                setArtifactId(artifactId)
                version = project.version.toString()

                setupPom(project = project)

                artifact(sourcesJar)
                artifact(javadocJar.get())
            }
        }

        repositories {
            mavenLocal()
            maven {
                if (version.toString().contains("SNAPSHOT", true)) {
                    // snapshot
                    name = Sonatype.`snapshot-oss`.NAME
                    url = uri(Sonatype.`snapshot-oss`.URL)
                } else {
                    name = Sonatype.oss.NAME
                    url = uri(Sonatype.oss.URL)
                }
                println("Publish repository name: $name")
                println("Publish repository url:  $url")


                val username0 = getProp("sonatype.username")?.toString() ?: run {
                    println("[WARN] Cannot found sonatype.username from extra for $artifactId")
                    return@maven
                }
                val password0 = getProp("sonatype.password")?.toString()
                    ?: throw NullPointerException("snapshots-sonatype-password")

                credentials {
                    username = username0
                    password = password0
                }
            }
        }

    }

}



fun MavenPublication.setupPom(project: Project) {
    val vcs = "https://github.com/simple-robot/simbot-component-kaiheila"
    pom {
        scm {
            url.set(vcs)
            connection.set("scm:$vcs.git")
            developerConnection.set("scm:${vcs.replace("https:", "git:")}.git")
        }

        issueManagement {
            system.set("GitHub Issue Management")
            url.set("$vcs/issues")
        }

        licenses {
            license {
                name.set("GNU GENERAL PUBLIC LICENSE, Version 3")
                url.set("https://www.gnu.org/licenses/gpl-3.0-standalone.html")
            }
            license {
                name.set("GNU LESSER GENERAL PUBLIC LICENSE, Version 3")
                url.set("https://www.gnu.org/licenses/lgpl-3.0-standalone.html")
            }
        }

        developers {
            developer {
                id.set("forte")
                name.set("ForteScarlet")
                email.set("ForteScarlet@163.com")
            }
        }

        withXml {
            val root = asNode()
            root.appendNode("description", project.description ?: "")
            root.appendNode("name", project.name)
            root.appendNode("url", vcs)
        }

    }

}


inline val Project.sourceSets: SourceSetContainer
    get() = extensions.getByName("sourceSets") as SourceSetContainer


fun Project.publishing(configure: PublishingExtension.() -> Unit) {
    (this as ExtensionAware).extensions.configure("publishing", configure)
}
