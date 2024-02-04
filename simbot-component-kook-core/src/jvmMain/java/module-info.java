/*
 *     Copyright (c) 2024. ForteScarlet.
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

import love.forte.simbot.component.ComponentFactoryProvider;
import love.forte.simbot.component.kook.KookComponentFactoryProvider;
import love.forte.simbot.component.kook.bot.KookBotManagerFactoryConfigurerProvider;
import love.forte.simbot.component.kook.bot.KookBotManagerFactoryProvider;
import love.forte.simbot.plugin.PluginFactoryProvider;

module simbot.component.kook.core {
    requires kotlin.stdlib;
    requires transitive simbot.component.kook.stdlib;
    requires static simbot.api;
    requires static simbot.common.annotations;
    requires io.ktor.client.websockets;

    exports love.forte.simbot.component.kook;
    exports love.forte.simbot.component.kook.bot;
    exports love.forte.simbot.component.kook.event;
    exports love.forte.simbot.component.kook.message;
    exports love.forte.simbot.component.kook.role;
    exports love.forte.simbot.component.kook.util;

    // provider
    provides ComponentFactoryProvider with KookComponentFactoryProvider;
    provides PluginFactoryProvider with KookBotManagerFactoryProvider;

    uses KookBotManagerFactoryConfigurerProvider;
}
