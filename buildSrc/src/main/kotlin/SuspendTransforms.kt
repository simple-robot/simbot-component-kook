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

import love.forte.plugin.suspendtrans.*


object SuspendTransforms {
    private val includeAnnotationApi4JClassInfo = ClassInfo("love.forte.simbot", "Api4J")
    private val includeAnnotationApi4J = IncludeAnnotation(includeAnnotationApi4JClassInfo)
    private val includeAnnotations = listOf(includeAnnotationApi4J)
    
    /**
     * JvmBlocking
     */
    val jvmBlockingTransformer = SuspendTransformConfiguration.jvmBlockingTransformer.copy(
        syntheticFunctionIncludeAnnotations = includeAnnotations,
        transformFunctionInfo = FunctionInfo("love.forte.simbot.utils", null, "$\$runInBlocking"),
        copyAnnotationExcludes = SuspendTransformConfiguration.jvmBlockingTransformer.copyAnnotationExcludes + SuspendTransformConfiguration.jvmBlockingTransformer.markAnnotation.classInfo
    )
    
    /**
     * JvmAsync
     */
    val jvmAsyncTransformer = SuspendTransformConfiguration.jvmAsyncTransformer.copy(
        syntheticFunctionIncludeAnnotations = includeAnnotations,
        transformFunctionInfo = FunctionInfo("love.forte.simbot.utils", null, "$\$runInAsyncNullable"),
        copyAnnotationExcludes = SuspendTransformConfiguration.jvmAsyncTransformer.copyAnnotationExcludes + SuspendTransformConfiguration.jvmAsyncTransformer.markAnnotation.classInfo
    )
    
    //region @JvmSuspendTrans
    private val jvmSuspendTransMarkAnnotationClassInfo = ClassInfo("love.forte.simbot", "JvmSuspendTrans")
    
    private val jvmSuspendTransMarkAnnotationForBlocking = MarkAnnotation(
        jvmSuspendTransMarkAnnotationClassInfo,
        baseNameProperty = "blockingBaseName",
        suffixProperty = "blockingSuffix",
        asPropertyProperty = "blockingAsProperty",
        defaultSuffix = SuspendTransformConfiguration.jvmBlockingAnnotationInfo.defaultSuffix,
    )
    private val jvmSuspendTransMarkAnnotationForAsync = MarkAnnotation(
        jvmSuspendTransMarkAnnotationClassInfo,
        baseNameProperty = "asyncBaseName",
        suffixProperty = "asyncSuffix",
        asPropertyProperty = "asyncAsProperty",
        defaultSuffix = SuspendTransformConfiguration.jvmAsyncAnnotationInfo.defaultSuffix,
    )
    
    val jvmSuspendTransTransformerForBlocking = jvmBlockingTransformer.copy(
        markAnnotation = jvmSuspendTransMarkAnnotationForBlocking,
        copyAnnotationExcludes = SuspendTransformConfiguration.jvmBlockingTransformer.copyAnnotationExcludes + jvmSuspendTransMarkAnnotationForBlocking.classInfo
    )
    
    val jvmSuspendTransTransformerForAsync = jvmAsyncTransformer.copy(
        markAnnotation = jvmSuspendTransMarkAnnotationForAsync,
        copyAnnotationExcludes = SuspendTransformConfiguration.jvmAsyncTransformer.copyAnnotationExcludes + jvmSuspendTransMarkAnnotationForAsync.classInfo
    )
    //endregion
    
    //region @JvmSuspendTransProperty
    private val jvmSuspendTransPropMarkAnnotationClassInfo = ClassInfo("love.forte.simbot", "JvmSuspendTransProperty")
    
    private val jvmSuspendTransPropMarkAnnotationForBlocking = MarkAnnotation(
        jvmSuspendTransPropMarkAnnotationClassInfo,
        baseNameProperty = "blockingBaseName",
        suffixProperty = "blockingSuffix",
        asPropertyProperty = "blockingAsProperty",
        defaultSuffix = "",
        defaultAsProperty = true
    )
    private val jvmSuspendTransPropMarkAnnotationForAsync = MarkAnnotation(
        jvmSuspendTransPropMarkAnnotationClassInfo,
        baseNameProperty = "asyncBaseName",
        suffixProperty = "asyncSuffix",
        asPropertyProperty = "asyncAsProperty",
        defaultSuffix = SuspendTransformConfiguration.jvmAsyncAnnotationInfo.defaultSuffix,
        defaultAsProperty = true
    )
    
    val jvmSuspendTransPropTransformerForBlocking = jvmBlockingTransformer.copy(
        markAnnotation = jvmSuspendTransPropMarkAnnotationForBlocking,
        copyAnnotationExcludes = SuspendTransformConfiguration.jvmBlockingTransformer.copyAnnotationExcludes + jvmSuspendTransPropMarkAnnotationForBlocking.classInfo
    )
    
    val jvmSuspendTransPropTransformerForAsync = jvmAsyncTransformer.copy(
        markAnnotation = jvmSuspendTransPropMarkAnnotationForAsync,
        copyAnnotationExcludes = SuspendTransformConfiguration.jvmAsyncTransformer.copyAnnotationExcludes + jvmSuspendTransPropMarkAnnotationForAsync.classInfo
    )
    //endregion
    
}




