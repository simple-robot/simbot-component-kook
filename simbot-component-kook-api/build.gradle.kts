/*
 * Copyright (c) 2022-2023. ForteScarlet.
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


plugins {
    `simbot-kook-module-conventions`
    `simbot-kook-maven-publish`
    `simbot-kook-suspend-transform`
    `kook-dokka-partial-configure`
}


dependencies {
    api(simbotApi)
    api(simbotRequestorCore)
    api(kotlin("reflect"))

    api(libs.ktor.client.core)
    api(libs.ktor.client.cio)
    api(libs.ktor.client.serialization)
    api(libs.kotlinx.serialization.json)
    compileOnly(libs.jetbrains.annotations)
}
