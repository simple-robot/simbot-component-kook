/*
 *  Copyright (c) 2022-2022 ForteScarlet <ForteScarlet@163.com>
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

@file:Suppress("MemberVisibilityCanBePrivate", "unused")

import org.gradle.api.Project

/*
*  Copyright (c) 2022-2022 ForteScarlet <ForteScarlet@163.com>
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


object P {
    object Simbot {
        const val GROUP = "love.forte.simbot"
        
        val version = Version(
            major = "3", minor = 0, patch = 0,
            status = preview(18, 0),
            isSnapshot = isSnapshot().also {
                println("isSnapshot: $it")
            }
        )
        
        
        val isSnapshot: Boolean get() = version.isSnapshot
        
        // val REAL_VERSION = version.fullVersion(false)
        // "3.0.0.preview.6.0"
        val VERSION = version.fullVersion(true)
        // = if (isSnapshot) "$REAL_VERSION-SNAPSHOT" else REAL_VERSION
    }
    
    object ComponentKaiheila {
        val isSnapshot: Boolean get() = Simbot.isSnapshot
        const val GROUP = "${Simbot.GROUP}.component"
        
        val version = Version(
            major = "${Simbot.version.major}.${Simbot.version.minor}",
            minor = 0,
            patch = 0,
            status = preview(13, 0),
            isSnapshot = isSnapshot
        )
        
        // 0: v3 api
        // const val REAL_VERSION = "3.0.1.0"
        
        val VERSION = version.fullVersion(true)
    }
}


/**
 * **P**roject **V**ersion。
 */
@Suppress("SpellCheckingInspection")
data class Version(
    /**
     * 主版号
     */
    val major: String,
    /**
     * 次版号
     */
    val minor: Int,
    /**
     * 修订号
     */
    val patch: Int,
    
    /**
     * 状态号。状态号会追加在 [major].[minor].[patch] 之后，由 `.` 拼接，
     * 变为 [major].[minor].[patch].[PVS.status].[PVS.minor].[PVS.patch].
     *
     * 例如：
     * ```
     * 3.0.0.preview.0.1
     * ```
     *
     */
    val status: PVS? = null,
    
    /**
     * 是否快照。如果是，将会在版本号结尾处拼接 `-SNAPSHOT`。
     */
    val isSnapshot: Boolean = false,
) {
    companion object {
        const val SNAPSHOT_SUFFIX = "-SNAPSHOT"
    }
    
    /**
     * 没有任何后缀的版本号。
     */
    val standardVersion: String = "$major.$minor.$patch"
    
    
    /**
     * 完整的版本号。
     */
    fun fullVersion(checkSnapshot: Boolean): String {
        return buildString {
            append(major).append('.').append(minor).append('.').append(patch)
            if (status != null) {
                append('.').append(status.status).append('.').append(status.minor).append('.').append(status.patch)
            }
            if (checkSnapshot && isSnapshot) {
                append(SNAPSHOT_SUFFIX)
            }
        }
    }
    
}

/**
 * **P**roject **V**ersion **S**tatus.
 */
@Suppress("SpellCheckingInspection")
data class PVS(
    val status: String,
    /**
     * 次版号
     */
    val minor: Int,
    /**
     * 修订号
     */
    val patch: Int,
) {
    companion object {
        const val PREVIEW_STATUS = "preview"
        const val BETA_STATUS = "beta"
    }
}


internal fun preview(minor: Int, patch: Int) = PVS(PVS.PREVIEW_STATUS, minor, patch)


private fun isSnapshot(): Boolean {
    println("property: ${System.getProperty("simbot.snapshot")}")
    println("env: ${System.getenv("simbot.snapshot")}")
    
    return System.getProperty("simbot.snapshot")?.toBoolean()
        ?: System.getenv("simbot.snapshot")?.toBoolean()
        ?: false
    
}