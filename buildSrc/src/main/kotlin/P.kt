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

@file:Suppress("MemberVisibilityCanBePrivate", "unused")

import love.forte.gradle.common.core.project.ProjectDetail
import love.forte.gradle.common.core.project.minus
import love.forte.gradle.common.core.project.version

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

// 3.0.0-M1
val simbotVersion = version(3, 0, 0) - version("M1")

val simbotApi = "love.forte.simbot:simbot-api:$simbotVersion"
val simbotCore = "love.forte.simbot:simbot-core:$simbotVersion"


object P : ProjectDetail() {
    const val GROUP = "love.forte.simbot.component"
    const val DESCRIPTION = "Simple Robot框架下针对开黑啦(Kook)平台的组件实现"
    const val HOMEPAGE = "https://github.com/simple-robot/simbot-component-kook"
    
    override val version: love.forte.gradle.common.core.project.Version = version(
        "${simbotVersion.major}.${simbotVersion.minor}",
        0, 0
    ) - version("alpha", 2)
    
    val snapshotVersion: love.forte.gradle.common.core.project.Version =
        version - love.forte.gradle.common.core.project.Version.SNAPSHOT
    
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


fun isSnapshot(): Boolean {
    println("property: ${System.getProperty("simbot.snapshot")}")
    println("env: ${System.getenv(Env.IS_SNAPSHOT)}")
    
    val property = System.getProperty("simbot.snapshot")?.toBoolean() ?: false
    val env = System.getenv(Env.IS_SNAPSHOT)?.toBoolean() ?: false
    
    return property || env
}