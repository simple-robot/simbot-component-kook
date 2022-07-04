/*
 *  Copyright (c) 2022 ForteScarlet <ForteScarlet@163.com>
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

import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import love.forte.simbot.kook.objects.*
import kotlin.test.Test

/**
 *
 * @author ForteScarlet
 */
class SerializationTest {

    private val json = Json {
        isLenient = true
        ignoreUnknownKeys = true
        prettyPrint = true
        encodeDefaults = true
    }


    @Test
    fun jsonTest() {
        val user = User("forte")
        val newUser = json.decodeFromString(User.serializer(), encode(json, user))
        assert(user == newUser)
    }

    @Suppress("UNCHECKED_CAST")
    @OptIn(InternalSerializationApi::class)
    fun encode(json: Json, any: Any): String {
        val serializer = any::class.serializer() as KSerializer<Any>
        return json.encodeToString(serializer, any)
    }

    @Test
    fun serializerEqualsTest() {
        assert(User.serializer() == User.serializer())
        assert(User.serializer() === User.serializer())
    }


    @Serializable
    private data class User(val name: String)


    @Test
    fun cardImageGroupsTest() {
        val groups = CardModule.ImageGroup(listOf(
            CardElement.Image("A", "A"),
            CardElement.Image("B", "B"),
        ))

        val card = Card(modules = listOf(groups))

        val jsonString = json.encodeToString(CardMessageSerializer, CardMessage(listOf(card)))

        println(jsonString)
    }

}