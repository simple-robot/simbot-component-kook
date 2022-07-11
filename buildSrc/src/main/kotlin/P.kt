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


object P {
    object Simbot {
        const val GROUP = "love.forte.simbot"
        
        val version = Version(
            major = "3", minor = 0, patch = 0,
            status = VersionStatus.beta(null, null, "M3"),
            isSnapshot = isSnapshot().also {
                println("isSnapshot: $it")
            }
        )
        
        
        val isSnapshot: Boolean get() = version.isSnapshot
        
        val VERSION: String get() = version.fullVersion(true)
    }
    
    object ComponentKook {
        val isSnapshot: Boolean get() = Simbot.isSnapshot
        const val GROUP = "${Simbot.GROUP}.component"
        const val DESCRIPTION = "Simple Robot框架下针对开黑啦(Kook)平台的组件实现"
        
        val version = Version(
            major = "${Simbot.version.major}.${Simbot.version.minor}",
            minor = 0,
            patch = 0,
            status = VersionStatus.alpha(1, null),
            isSnapshot = isSnapshot
        )
        
        val VERSION get() = version.fullVersion(true)
    }
}


private fun isSnapshot(): Boolean {
    println("property: ${System.getProperty("simbot.snapshot")}")
    println("env: ${System.getenv(Env.IS_SNAPSHOT)}")
    
    val property = System.getProperty("simbot.snapshot")?.toBoolean() ?: false
    val env = System.getenv(Env.IS_SNAPSHOT)?.toBoolean() ?: false
    
    return property || env
}