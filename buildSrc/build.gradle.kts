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

plugins {
    `kotlin-dsl`
}

repositories {
    mavenCentral()
    gradlePluginPortal()
    mavenLocal()
}

val kotlinVersion: String = libs.versions.kotlin.get()

dependencies {
    implementation(kotlin("gradle-plugin", kotlinVersion))
    implementation(kotlin("serialization", kotlinVersion))
    implementation(libs.dokka.plugin)

    // see https://github.com/gradle-nexus/publish-plugin
//    implementation("io.github.gradle-nexus:publish-plugin:2.0.0")

    // see https://www.jetbrains.com/help/kotlin-multiplatform-dev/multiplatform-publish-libraries.html#configure-the-project
    // see https://github.com/vanniktech/gradle-maven-publish-plugin
    // see https://plugins.gradle.org/plugin/com.vanniktech.maven.publish
    implementation(libs.maven.publish)

    // simbot suspend transform gradle common
    implementation(libs.simbot.gradle)

    // suspend transform
    implementation(libs.suspend.transform.gradle)

    // gradle common
    implementation(libs.bundles.gradle.common)

    // vanniktech/gradle-maven-publish-plugin
    // https://github.com/vanniktech/gradle-maven-publish-plugin
    // https://www.jetbrains.com/help/kotlin-multiplatform-dev/multiplatform-publish-libraries.html#set-up-the-publishing-plugin
//    implementation("com.vanniktech:gradle-maven-publish-plugin:0.32.0")

}
