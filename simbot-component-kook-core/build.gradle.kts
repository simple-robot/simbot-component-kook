/*
 *     Copyright (c) 2022-2024. ForteScarlet.
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

import love.forte.gradle.common.core.project.setup
import love.forte.gradle.common.kotlin.multiplatform.applyTier1
import love.forte.gradle.common.kotlin.multiplatform.applyTier2
import love.forte.gradle.common.kotlin.multiplatform.applyTier3
import util.isCi

plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    `kook-dokka-partial-configure`
    `simbot-kook-suspend-transform`
    alias(libs.plugins.ksp)
}

setup(P)

useK2()
configJavaCompileWithModule("simbot.component.kook.core")
apply(plugin = "kook-multiplatform-maven-publish")

configJsTestTasks()

kotlin {
    explicitApi()
    applyDefaultHierarchyTemplate()

    sourceSets.configureEach {
        languageSettings {
            optIn("love.forte.simbot.kook.ExperimentalKookApi")
            optIn("love.forte.simbot.kook.InternalKookApi")
        }
    }

    configKotlinJvm()

    js(IR) {
        configJs()
    }

    applyTier1()
    applyTier2()
    applyTier3(supportKtorClient = true)

    sourceSets {
        commonMain.dependencies {
            compileOnly(libs.simbot.api)
            api(project(":simbot-component-kook-stdlib"))
            compileOnly(libs.simbot.common.annotations)
            // ktor
            api(libs.ktor.client.contentNegotiation)
//            api(libs.ktor.serialization.kotlinx.json)
            api(libs.ktor.client.ws)
        }

        commonTest.dependencies {
            implementation(kotlin("test"))
            implementation(kotlin("reflect"))
            implementation(libs.kotlinx.coroutines.test)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.simbot.api)
            implementation(libs.simbot.core)
            implementation(libs.simbot.common.core)
        }

        jvmTest.dependencies {
            runtimeOnly(libs.ktor.client.cio)
//            runtimeOnly(libs.kotlinx.coroutines.reactor)
//            implementation(libs.reactor.core)

            implementation(libs.log4j.api)
            implementation(libs.log4j.core)
            implementation(libs.log4j.slf4j2Impl)
        }

        jsMain.dependencies {
            implementation(libs.simbot.api)
            api(libs.simbot.common.annotations)
        }

        nativeMain.dependencies {
            implementation(libs.simbot.api)
            api(libs.simbot.common.annotations)
        }

        mingwTest.dependencies {
            implementation(libs.ktor.client.winhttp)
        }
    }
}

dependencies {
    add("kspJvm", project(":internal-processors:api-reader"))
}

ksp {
    arg("kook.api.reader.enable", (!isCi).toString())
//    arg("kook.api.finder.api.output", rootDir.resolve("generated-docs/api-list.md").absolutePath)
    arg("kook.api.finder.event.output", rootDir.resolve("generated-docs/core-event-list.md").absolutePath)
    arg("kook.api.finder.event.class", "love.forte.simbot.component.kook.event.KookEvent")
}
