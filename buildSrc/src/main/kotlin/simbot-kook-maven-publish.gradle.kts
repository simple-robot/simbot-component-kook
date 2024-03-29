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

import love.forte.gradle.common.core.Gpg
import love.forte.gradle.common.publication.configure.jvmConfigPublishing
import util.checkPublishConfigurable
import util.isCi
import util.isLinux

/*
 *  Copyright (c) 2022 ForteScarlet <ForteScarlet@163.com>
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

plugins {
    id("signing")
    id("maven-publish")
}

val (isSnapshotOnly, isReleaseOnly, isPublishConfigurable) = checkPublishConfigurable()


logger.info("isSnapshotOnly: {}", isSnapshotOnly)
logger.info("isReleaseOnly: {}", isReleaseOnly)
logger.info("isPublishConfigurable: {}", isPublishConfigurable)

if (!isCi || isLinux) {
    checkPublishConfigurable {
        jvmConfigPublishing {
            project = P
            publicationName = "kookDist"
            isSnapshot = isSnapshot().also {
                logger.info("jvmConfigPublishing.isSnapshot: {}", it)
            }
            val jarSources by tasks.registering(Jar::class) {
                archiveClassifier.set("sources")
                from(sourceSets["main"].allSource)
            }

            val jarJavadoc by tasks.registering(Jar::class) {
                if (!(isSnapshot || isSnapshot())) {
                    dependsOn(tasks.dokkaHtml)
                    from(tasks.dokkaHtml.flatMap { it.outputDirectory })
                }
                archiveClassifier.set("javadoc")
            }

            artifact(jarSources)
            artifact(jarJavadoc)

            releasesRepository = ReleaseRepository
            snapshotRepository = SnapshotRepository
            gpg = if (isSnapshot()) null else Gpg.ofSystemPropOrNull()
        }

        publishing {
            publications.withType<MavenPublication> {
                show()
            }
        }


    }
}

// TODO see https://github.com/gradle-nexus/publish-plugin/issues/208#issuecomment-1465029831
val signingTasks: TaskCollection<Sign> = tasks.withType<Sign>()
tasks.withType<PublishToMavenRepository>().configureEach {
    mustRunAfter(signingTasks)
}

fun MavenPublication.show() {
    //// show project info
    println("========================================================")
    println("== MavenPublication for ${project.name}")
    println("== maven.pub.group:       $group")
    println("== maven.pub.name:        $name")
    println("== project.version:       ${project.version}")
    println("== maven.pub.version:     $version")
    println("== maven.pub.description: $description")
    println("========================================================")
}


inline val Project.sourceSets: SourceSetContainer
    get() = extensions.getByName("sourceSets") as SourceSetContainer

internal val TaskContainer.dokkaHtml: TaskProvider<org.jetbrains.dokka.gradle.DokkaTask>
    get() = named<org.jetbrains.dokka.gradle.DokkaTask>("dokkaHtml")
