/*
 *     Copyright (c) 2023-2024. ForteScarlet.
 *
 *     This file is part of simbot-component-kook.
 *
 *     simbot-component-kook is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Lesser General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     simbot-component-kook is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *     GNU Lesser General Public License for more details.
 *
 *     You should have received a copy of the GNU Lesser General Public License
 *     along with simbot-component-kook,
 *     If not, see <https://www.gnu.org/licenses/>.
 */

import love.forte.gradle.common.core.Gpg
import love.forte.gradle.common.core.project.setup
import love.forte.gradle.common.core.property.systemProp
import love.forte.gradle.common.publication.configure.configPublishMaven
import love.forte.gradle.common.publication.configure.publishingExtension
import love.forte.gradle.common.publication.configure.setupPom

plugins {
    kotlin("multiplatform")
    signing
    `maven-publish`
    id("org.jetbrains.dokka")
}


setup(P)

val p = project
val isSnapshot = project.version.toString().contains("SNAPSHOT", true)

val jarJavadoc by tasks.registering(Jar::class) {
    group = "documentation"
    archiveClassifier.set("javadoc")
    if (!(isSnapshot || isSnapshot() || isSimbotLocal())) {
        dependsOn(tasks.dokkaGeneratePublicationHtml)
        from(tasks.dokkaGeneratePublicationHtml.flatMap { it.outputDirectory })
    }
}

publishing {
    repositories {
        mavenLocal()
        if (isSnapshot) {
            configPublishMaven(SnapshotRepository)
        } else {
            configPublishMaven(ReleaseRepository)
        }
    }

    publications {
        withType<MavenPublication> {
            artifacts {
                artifact(jarJavadoc)
            }

            setupPom(project.name, P)
        }
    }
}

signing {
    val gpg = Gpg.ofSystemPropOrNull() ?: return@signing
    val (keyId, secretKey, password) = gpg
    useInMemoryPgpKeys(keyId, secretKey, password)
    sign(publishingExtension.publications)
}

// TODO see https://github.com/gradle-nexus/publish-plugin/issues/208#issuecomment-1465029831
val signingTasks: TaskCollection<Sign> = tasks.withType<Sign>()
tasks.withType<PublishToMavenRepository>().configureEach {
    mustRunAfter(signingTasks)
}

show()

fun show() {
    //// show project info
    logger.info(
        """
        |=======================================================
        |= project.group:       {}
        |= project.name:        {}
        |= project.version:     {}
        |= project.description: {}
        |= os.name:             {}
        |=======================================================
    """.trimIndent(),
        group, name, version, description, systemProp("os.name")
    )
}


inline val Project.sourceSets: SourceSetContainer
    get() = extensions.getByName("sourceSets") as SourceSetContainer
