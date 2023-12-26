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

import love.forte.gradle.common.core.Gpg
import love.forte.gradle.common.core.project.setup
import love.forte.gradle.common.core.property.systemProp
import love.forte.gradle.common.publication.configure.multiplatformConfigPublishing
import org.jetbrains.kotlin.konan.target.HostManager
import org.jetbrains.kotlin.konan.target.KonanTarget

plugins {
    kotlin("multiplatform")
    signing
    `maven-publish`
}

tasks.withType<JavaCompile> {
    sourceCompatibility = "1.8"
    targetCompatibility = "1.8"
    options.encoding = "UTF-8"
}

setup(P)
if (isSnapshot()) {
    version = P.snapshotVersion.toString()
}

val p = project
multiplatformConfigPublishing {
    project = P
    
    val jarJavadoc by tasks.registering(Jar::class) {
        archiveClassifier.set("javadoc")
        from(tasks.findByName("dokkaHtml"))
    }
    artifact(jarJavadoc)
    isSnapshot = project.version.toString().contains("SNAPSHOT", true)
    releasesRepository = ReleaseRepository
    snapshotRepository = SnapshotRepository
    gpg = Gpg.ofSystemPropOrNull()

    if (systemProp("SIMBOT_LOCAL").toBoolean()) {
        mainHost = null
    }
}

// TODO see https://github.com/gradle-nexus/publish-plugin/issues/208#issuecomment-1465029831
val signingTasks: TaskCollection<Sign> = tasks.withType<Sign>()
tasks.withType<PublishToMavenRepository>().configureEach {
    mustRunAfter(signingTasks)
}

fun KonanTarget.supports(hostManager: HostManager): Set<String> {
    return hostManager.enabledByHost[this]?.mapTo(mutableSetOf()) { target -> target.name } ?: emptySet()
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
