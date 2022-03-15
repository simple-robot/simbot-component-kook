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

package example

import kotlinx.serialization.json.*
import kotlinx.serialization.modules.*
import love.forte.simbot.*
import love.forte.simbot.component.kaihieila.*
import love.forte.simbot.component.kaihieila.message.*
import love.forte.simbot.kaiheila.api.*
import love.forte.simbot.kaiheila.api.asset.*
import love.forte.simbot.kaiheila.objects.*
import love.forte.simbot.message.*
import kotlin.test.*

/**
 *
 * @author ForteScarlet
 */
class MessageSerializerTest {

    @OptIn(ExperimentalSimbotApi::class, ApiResultType::class)
    @Test
    fun encodeTest() {
        val messages = buildMessages {
            +AtAllHere
            +KMarkdownMessage(buildKMarkdown {
                at("1234")
                link("Simbot Home", "http://simbot.forte.love")
            })
            +AssetImage(AssetCreated("https://baidu.com"))
        }

        println(messages)

        val json = Json {
            isLenient = true
            ignoreUnknownKeys = true
            prettyPrint = true
            serializersModule += KaiheilaComponent.messageSerializersModule
        }

        val str = json.encodeToString(Messages.serializer, messages)

        println(str)

        val messages2 = json.decodeFromString(Messages.serializer, str)

        println(messages == messages2)
        messages2.forEach {
            println(it)
        }


    }
}