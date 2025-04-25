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

import org.gradle.api.Project
import org.gradle.api.tasks.SourceSetContainer
import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.kotlin.dsl.assign
import org.gradle.kotlin.dsl.get
import org.gradle.kotlin.dsl.getByName
import org.gradle.kotlin.dsl.withType
import org.gradle.process.CommandLineArgumentProvider
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinBaseExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.targets.jvm.KotlinJvmTarget


@OptIn(ExperimentalKotlinGradlePluginApi::class)
inline fun KotlinJvmTarget.configJava(crossinline block: KotlinJvmTarget.() -> Unit = {}) {
    compilerOptions {
        javaParameters = true
        freeCompilerArgs.addAll(
            "-Xjvm-default=all"
        )
    }

    testRuns["test"].executionTask.configure {
        useJUnitPlatform()
    }
    block()
}


fun KotlinBaseExtension.configJavaToolchain(jdkVersion: Int) {
    jvmToolchain(jdkVersion)
}

inline fun KotlinMultiplatformExtension.configKotlinJvm(
    jdkVersion: Int = JVMConstants.KT_JVM_TARGET_VALUE,
    crossinline block: KotlinJvmTarget.() -> Unit = {}
) {
    configJavaToolchain(jdkVersion)
    jvm {
        configJava(block)
    }
}

inline fun KotlinJvmProjectExtension.configKotlinJvm(
    jdkVersion: Int = JVMConstants.KT_JVM_TARGET_VALUE,
    crossinline block: KotlinJvmProjectExtension.() -> Unit = {}
) {
    configJavaToolchain(jdkVersion)
    compilerOptions {
        javaParameters = true
        jvmTarget.set(JvmTarget.fromTarget(jdkVersion.toString()))
        // freeCompilerArgs.addAll("-Xjvm-default=all", "-Xjsr305=strict")
        freeCompilerArgs.set(freeCompilerArgs.getOrElse(emptyList()) + listOf("-Xjvm-default=all", "-Xjsr305=strict"))
    }
    block()
}

inline fun Project.configJavaCompileWithModule(
    moduleName: String? = null,
    jvmVersion: String = JVMConstants.KT_JVM_TARGET,
    crossinline block: JavaCompile.() -> Unit = {}
) {
    tasks.withType<JavaCompile> {
        options.encoding = "UTF-8"
        sourceCompatibility = jvmVersion
        targetCompatibility = jvmVersion

        if (moduleName != null) {
            options.compilerArgumentProviders.add(CommandLineArgumentProvider {
                val sourceSet = sourceSets.findByName("main") ?: sourceSets.findByName("jvmMain")
                if (sourceSet != null) {
                    // Provide compiled Kotlin classes to javac – needed for Java/Kotlin mixed sources to work
                    listOf("--patch-module", "$moduleName=${sourceSet.output.asPath}")
                } else {
                    emptyList()
                }
            })
        }

        block()
    }
}

@PublishedApi
internal val Project.sourceSets: SourceSetContainer
    get() = extensions.getByName<SourceSetContainer>("sourceSets")
