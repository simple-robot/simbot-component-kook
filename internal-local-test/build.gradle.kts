/*
 *     Copyright (c) 2025. ForteScarlet.
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


plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")
}

dependencies {
    implementation(libs.simbot.core)
    runtimeOnly(libs.ktor.client.java)
    runtimeOnly(libs.simbot.logger.slf4jimpl)
    implementation(project(":simbot-component-kook-core"))
}

kotlin {
    compilerOptions {
        optIn.addAll(
            "love.forte.simbot.kook.ExperimentalKookApi",
            "love.forte.simbot.kook.InternalKookApi",
        )
    }
}

