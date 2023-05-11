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

package love.forte.simbot.kook.api.user

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.builtins.serializer
import love.forte.simbot.kook.api.KookPostApi

/**
 * [下线机器人](https://developer.kookapp.cn/doc/http/user#%E4%B8%8B%E7%BA%BF%E6%9C%BA%E5%99%A8%E4%BA%BA)
 *
 * @author ForteScarlet
 */
public object OfflineApi : KookPostApi<Unit>() {
    override val resultDeserializer: DeserializationStrategy<Unit>
        get() = Unit.serializer()

    override val apiPath: ApiPath = ApiPath.create("user", "offline")
}
