/*
 *     Copyright (c) 2023-2024. ForteScarlet.
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
    alias(libs.plugins.ksp)
}

setup(P)

configJavaCompileWithModule("simbot.component.kook.api")
apply(plugin = "kook-multiplatform-maven-publish")


repositories {
    mavenCentral()
}

kotlin {
    explicitApi()
    applyDefaultHierarchyTemplate()

    compilerOptions {
        optIn.addAll(
            "love.forte.simbot.kook.ExperimentalKookApi",
            "love.forte.simbot.kook.InternalKookApi"
        )
    }

    configKotlinJvm()

    js(IR) {
        configJs()
    }


    applyTier1()
    applyTier2()
    applyTier3(supportKtorClient = true)

    sourceSets {
        commonMain {
            dependencies {
                api(libs.kotlinx.coroutines.core)

                api(libs.simbot.logger)
                api(libs.simbot.common.apidefinition)
                api(libs.simbot.common.suspend)
                api(libs.simbot.common.core)
                api(libs.simbot.common.annotations)

                api(libs.ktor.client.core)
                api(libs.ktor.client.contentNegotiation)
                api(libs.kotlinx.serialization.json)
            }
        }

        commonTest {
            dependencies {
                implementation(kotlin("test"))
                implementation(libs.kotlinx.coroutines.test)
                implementation(libs.ktor.client.mock)
            }
        }

        jvmTest {
            dependencies {
                implementation(libs.ktor.client.cio)
                implementation(libs.log4j.api)
                implementation(libs.log4j.core)
                implementation(libs.log4j.slf4j2Impl)
            }
        }

        jsMain.dependencies {
            api(libs.ktor.client.js)
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
    arg("kook.api.finder.api.output", rootDir.resolve("generated-docs/api-list.md").absolutePath)
    arg("kook.api.finder.event.output", rootDir.resolve("generated-docs/event-list.md").absolutePath)
}
