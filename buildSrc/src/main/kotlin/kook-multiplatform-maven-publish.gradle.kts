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

import com.vanniktech.maven.publish.SonatypeHost
import love.forte.gradle.common.core.project.setup
import love.forte.gradle.common.core.property.ofIf
import love.forte.gradle.common.core.property.systemProp
import org.gradle.internal.impldep.org.bouncycastle.asn1.x509.X509ObjectIdentifiers.organization

plugins {
    kotlin("multiplatform")
    signing
    // https://www.jetbrains.com/help/kotlin-multiplatform-dev/multiplatform-publish-libraries.html#configure-the-project
    id("com.vanniktech.maven.publish")
    id("org.jetbrains.dokka")
}


setup(P)

val p = project
val isSnapshot = project.version.toString().contains("SNAPSHOT", true)

mavenPublishing {
    publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL)
    if (!isSimbotLocal()) {
        signAllPublications()
    }
    coordinates(groupId = p.group.toString(), artifactId = p.name, version = p.version.toString())

    pom {
        name = p.name
        description = p.description
        url = P.HOMEPAGE
        licenses {
            P.licenses.forEach { license ->
                license {
                    name ofIf license.name
                    url ofIf license.url
                    distribution ofIf license.distribution
                    comments ofIf license.comments
                }
            }
        }

        val scm = P.scm
        scm {
            url ofIf scm.url
            connection ofIf scm.connection
            developerConnection ofIf scm.developerConnection
            tag ofIf scm.tag
        }

        developers {
            P.developers.forEach { developer ->
                developer {
                    id ofIf developer.id
                    name ofIf developer.name
                    email ofIf developer.email
                    url ofIf developer.url
                    organization ofIf developer.organization
                    organizationUrl ofIf developer.organizationUrl
                    timezone ofIf developer.timezone
                    roles.addAll(developer.roles)
                    properties.putAll(developer.properties)
                }
            }
        }
    }
}

//val jarJavadoc by tasks.registering(Jar::class) {
//    group = "documentation"
//    archiveClassifier.set("javadoc")
//    if (!(isSnapshot || isSnapshot() || isSimbotLocal())) {
//        dependsOn(tasks.dokkaGeneratePublicationHtml)
//        from(tasks.dokkaGeneratePublicationHtml.flatMap { it.outputDirectory })
//    }
//}

//publishing {
//    repositories {
//        mavenLocal()
//        if (isSnapshot) {
//            configPublishMaven(SnapshotRepository)
//        } else {
//            configPublishMaven(ReleaseRepository)
//        }
//    }
//
//    publications {
//        withType<MavenPublication> {
//            artifacts {
//                artifact(jarJavadoc)
//            }
//
//            setupPom(project.name, P)
//        }
//    }
//}
//
//signing {
//    val gpg = Gpg.ofSystemPropOrNull() ?: return@signing
//    val (keyId, secretKey, password) = gpg
//    useInMemoryPgpKeys(keyId, secretKey, password)
//    sign(publishingExtension.publications)
//}

// TODO see https://github.com/gradle-nexus/publish-plugin/issues/208#issuecomment-1465029831
//val signingTasks: TaskCollection<Sign> = tasks.withType<Sign>()
//tasks.withType<PublishToMavenRepository>().configureEach {
//    mustRunAfter(signingTasks)
//}

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
