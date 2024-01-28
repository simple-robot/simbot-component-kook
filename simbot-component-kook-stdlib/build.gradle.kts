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

import love.forte.gradle.common.core.project.setup
import love.forte.gradle.common.kotlin.multiplatform.applyTier1
import love.forte.gradle.common.kotlin.multiplatform.applyTier2
import love.forte.gradle.common.kotlin.multiplatform.applyTier3

plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    `kook-dokka-partial-configure`
    `simbot-kook-suspend-transform`
}

setup(P)

useK2()
configJavaCompileWithModule("simbot.component.kook.stdlib")
apply(plugin = "kook-multiplatform-maven-publish")

configJsTestTasks()

repositories {
    mavenCentral()
}

kotlin {
    explicitApi()
    applyDefaultHierarchyTemplate()

//    sourceSets.configureEach {
//        languageSettings {
//            optIn("love.forte.simbot.InternalSimbotApi")
//            optIn("love.forte.simbot.ExperimentalSimbotApi")
//        }
//    }

    configKotlinJvm()

    js(IR) {
        configJs()
    }

    applyTier1()
    applyTier2()
    applyTier3(supportKtorClient = true)


    sourceSets {
        commonMain.dependencies {
            compileOnly(libs.simbot.common.annotations)
            api(project(":simbot-component-kook-api"))
            api(libs.simbot.common.loop)
            api(libs.simbot.common.atomic)
            api(libs.kotlinx.coroutines.core)
            api(libs.ktor.client.ws)
            api(libs.ktor.client.contentNegotiation)
            api(libs.ktor.serialization.kotlinx.json)
            api(libs.kotlinx.serialization.json)
        }

        commonTest.dependencies {
            implementation(kotlin("test"))
            implementation(libs.kotlinx.coroutines.test)
        }

        jvmTest.dependencies {
            runtimeOnly(libs.ktor.client.cio)
            implementation(libs.simbot.logger.slf4jimpl)
        }

//        jvmTest.dependencies {
//            implementation(libs.ktor.client.cio)
//            implementation(simbotApi)
//            implementation(simbotLogger)
//            implementation(simbotLoggerSlf4j)
//        }

        jsMain.dependencies {
            api(libs.ktor.client.js)
            implementation(libs.simbot.common.annotations)
        }

        nativeMain.dependencies {
            implementation(libs.simbot.common.annotations)
        }

        mingwTest.dependencies {
            implementation(libs.ktor.client.winhttp)
        }

    }

}

