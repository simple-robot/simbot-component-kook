/*
 * Copyright (c) 2024. ForteScarlet.
 *
 * This file is part of simbot-component-qq-guild.
 *
 * simbot-component-qq-guild is free software: you can redistribute it and/or modify it under the terms
 * of the GNU Lesser General Public License as published by the Free Software Foundation,
 * either version 3 of the License, or (at your option) any later version.
 *
 * simbot-component-qq-guild is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with simbot-component-qq-guild.
 * If not, see <https://www.gnu.org/licenses/>.
 */

import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.targets.js.dsl.KotlinJsTargetDsl


inline fun KotlinJsTargetDsl.configJs(
    nodeJs: Boolean = true,
    browser: Boolean = true,
    block: () -> Unit = {}
) {
    if (nodeJs) {
        nodejs()
//        {
////            testTask {
////                useMocha {
////                    timeout = "10000"
////                }
////            }
//        }
    }

    if (browser) {
        browser()
//        {
//            testTask{
//                useKarma {
//                    useChromeHeadless()
//                    // useConfigDirectory(File(project.rootProject.projectDir, "karma"))
//                }
//            }
//        }
    }

    binaries.library()
    block()
}


fun Project.configJsTestTasks() {
    // val shouldRunJsBrowserTest = !hasProperty("teamcity") || hasProperty("enable-js-tests")
    // if (shouldRunJsBrowserTest) return
    tasks.findByName("cleanJsBrowserTest")?.apply {
        onlyIf { false }
    }
    tasks.findByName("jsBrowserTest")?.apply {
        onlyIf { false }
    }
}
