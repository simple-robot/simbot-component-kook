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

import love.forte.gradle.common.core.repository.Repositories

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
    `simbot-kook-changelog-generator`
    `simbot-kook-dokka-multi-module`
    alias(libs.plugins.kotlinxBinaryCompatibilityValidator)
}

buildscript {
    repositories {
        mavenCentral()
    }
}

allprojects {
    repositories {
        mavenCentral()
        maven {
            url = uri(Repositories.Snapshot.URL)
            mavenContent {
                snapshotsOnly()
            }
        }
    }
}

apiValidation {
    ignoredPackages.add("*.internal.*")

    ignoredProjects.addAll(
        listOf(
            "api-reader",
            "message-element-processor",
            "internal-local-test"
        ),
    )

    // 实验性和内部API可能无法保证二进制兼容
    nonPublicMarkers.addAll(
        listOf(
            "love.forte.simbot.annotations.ExperimentalSimbotAPI",
            "love.forte.simbot.annotations.InternalSimbotAPI",
            "love.forte.simbot.kook.ExperimentalKookApi",
            "love.forte.simbot.kook.InternalKookApi",
        ),
    )

    apiDumpDirectory = "api"
}
