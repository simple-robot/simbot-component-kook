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

plugins {
    kotlin("multiplatform")
    `kook-multiplatform-maven-publish`
    kotlin("plugin.serialization")
    `kook-dokka-partial-configure`
    `simbot-kook-suspend-transform`
    id("kotlinx-atomicfu")
}

setup(P)
if (isSnapshot()) {
    version = P.snapshotVersion.toString()
}

tasks.withType<JavaCompile> {
    sourceCompatibility = "1.8"
    targetCompatibility = "1.8"
    options.encoding = "UTF-8"
}

repositories {
    mavenCentral()
}

kotlin {
    explicitApi()
    applyDefaultHierarchyTemplate()

    sourceSets.configureEach {
        languageSettings {
            optIn("love.forte.simbot.InternalSimbotApi")
            optIn("love.forte.simbot.ExperimentalSimbotApi")
        }
    }

    jvm {
        withJava()
        compilations.all {
            kotlinOptions {
                jvmTarget = "1.8"
                javaParameters = true
                freeCompilerArgs = freeCompilerArgs + listOf("-Xjvm-default=all")
            }
        }
        testRuns["test"].executionTask.configure {
            useJUnitPlatform()
        }
    }

    js(IR) {
        nodejs()
    }

    // Tier 1
    macosX64()
    macosArm64()
    iosSimulatorArm64()
    iosX64()

    // Tier 2
    linuxX64()
    linuxArm64()
    watchosSimulatorArm64()
    watchosX64()
    watchosArm32()
    watchosArm64()
    tvosSimulatorArm64()
    tvosX64()
    tvosArm64()
    iosArm64()

    // Tier 3
//    androidNativeArm32()
//    androidNativeArm64()
//    androidNativeX86()
//    androidNativeX64()
    mingwX64()
//    watchosDeviceArm64()

    sourceSets {
        commonMain.dependencies {
            api(project(":simbot-component-kook-api"))
            api(simbotLogger)
            api(simbotUtilLoop)
            api(simbotUtilSuspendTransformer)
            compileOnly(simbotUtilAnnotations)
            api(libs.kotlinx.coroutines.core)
            api(libs.ktor.client.ws)
            api("org.jetbrains.kotlinx:atomicfu:${libs.versions.atomicfu.get()}")
        }

        commonTest.dependencies {
            implementation(kotlin("test"))
            implementation(libs.kotlinx.coroutines.test)
        }

        jvmMain.dependencies {
            compileOnly(simbotUtilAnnotations) // use @Api4J annotation
        }

        jvmTest.dependencies {
            implementation(libs.ktor.client.cio)
            implementation(simbotApi)
            implementation(simbotLogger)
            implementation(simbotLoggerSlf4j)
        }

        jsMain.dependencies {
            implementation(libs.ktor.client.js)
        }

    }

}

atomicfu {
    transformJvm = true
    transformJs = true
    jvmVariant = "FU"
}


// suppress all?
//tasks.withType<org.jetbrains.dokka.gradle.DokkaTaskPartial>().configureEach {
//    dokkaSourceSets.configureEach {
//        suppress.set(true)
//        perPackageOption {
//            suppress.set(true)
//        }
//    }
//}


