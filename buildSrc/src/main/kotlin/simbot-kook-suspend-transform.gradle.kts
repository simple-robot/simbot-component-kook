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

import love.forte.plugin.suspendtrans.SuspendTransformConfiguration

plugins {
    id("love.forte.plugin.suspend-transform")
}

suspendTransform {
    includeRuntime = false
    jvm {
        // jvmBlockingMarkAnnotation.functionInheritable = true
        // jvmAsyncMarkAnnotation.functionInheritable = true
        // api and annotation comes from :apis:simbot-api
        val api4JIncludeAnnotation = SuspendTransformConfiguration.IncludeAnnotation("love.forte.simbot.Api4J")
        syntheticBlockingFunctionIncludeAnnotations = listOf(api4JIncludeAnnotation)
        syntheticAsyncFunctionIncludeAnnotations = listOf(api4JIncludeAnnotation)
        jvmBlockingFunctionName = "love.forte.simbot.utils.$\$runInBlocking"
        jvmAsyncFunctionName = "love.forte.simbot.utils.$\$runInAsync"
    }
}
