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
import gradle.kotlin.dsl.accessors._962842af322993b246b9354775ec3900.mavenPublishing
import love.forte.gradle.common.core.project.setup
import love.forte.gradle.common.core.property.ofIf
import love.forte.gradle.common.core.property.systemProp
import org.gradle.internal.impldep.org.bouncycastle.asn1.x509.X509ObjectIdentifiers.organization

plugins {
    signing
    // https://www.jetbrains.com/help/kotlin-multiplatform-dev/multiplatform-publish-libraries.html#configure-the-project
    id("com.vanniktech.maven.publish")
    id("org.jetbrains.dokka")
}

setup(P)

val p = project

mavenPublishing {
    publishToMavenCentral()
    if (!isSimbotLocal()) {
        signAllPublications()
    }

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

inline val Project.sourceSets: SourceSetContainer
    get() = extensions.getByName("sourceSets") as SourceSetContainer
