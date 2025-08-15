/*
 *     Copyright (c) 2022-2025. ForteScarlet.
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

@file:Suppress("MemberVisibilityCanBePrivate", "unused")

import love.forte.gradle.common.core.project.ProjectDetail
import love.forte.gradle.common.core.property.systemProp

// Publish your library to Maven Central – tutorial
// https://www.jetbrains.com/help/kotlin-multiplatform-dev/multiplatform-publish-libraries.html#prepare-accounts-and-credentials

object P : ProjectDetail() {
    const val GROUP = "love.forte.simbot.component"
    const val DESCRIPTION = "Simple Robot框架下针对开黑啦(KOOK)平台的组件实现"
    const val HOMEPAGE = "https://github.com/simple-robot/simbot-component-kook"

    override val homepage: String
        get() = HOMEPAGE

    const val VERSION = "4.1.7"
    const val NEXT_VERSION = "4.1.7"

    override val snapshotVersion = "$NEXT_VERSION-SNAPSHOT"
    override val version = if (isSnapshot()) snapshotVersion else VERSION

    override val group: String get() = GROUP
    override val description: String get() = DESCRIPTION
    override val developers: List<Developer> = developers {
        developer {
            id = "forte"
            name = "ForteScarlet"
            email = "ForteScarlet@163.com"
            url = "https://github.com/ForteScarlet"
        }
        developer {
            id = "forliy"
            name = "ForliyScarlet"
            email = "ForliyScarlet@163.com"
            url = "https://github.com/ForliyScarlet"
        }
    }

    override val licenses: List<License> = licenses {
        license {
            name = "GNU GENERAL PUBLIC LICENSE, Version 3"
            url = "https://www.gnu.org/licenses/gpl-3.0-standalone.html"
        }
        license {
            name = "GNU LESSER GENERAL PUBLIC LICENSE, Version 3"
            url = "https://www.gnu.org/licenses/lgpl-3.0-standalone.html"
        }
    }

    override val scm: Scm = scm {
        url = HOMEPAGE
        connection = "scm:git:$HOMEPAGE.git"
        developerConnection = "scm:git:ssh://git@github.com/simple-robot/simbot-component-kook.git"
    }


}

private val _isSnapshot by lazy { initIsSnapshot() }

private fun initIsSnapshot(): Boolean {
    val property = System.getProperty("simbot.snapshot").toBoolean()
    val env = System.getenv(Env.IS_SNAPSHOT).toBoolean()

    return property || env
}

fun isSnapshot(): Boolean = _isSnapshot

fun isSimbotLocal(): Boolean = systemProp("SIMBOT_LOCAL").toBoolean()
