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
import gradle.kotlin.dsl.accessors._06ae31f6857ff01ea4cac8dbece12685.suspendTransform
import love.forte.simbot.gradle.suspendtransforms.SuspendTransforms

plugins {
    id("love.forte.plugin.suspend-transform")
}

suspendTransform {
    includeRuntime = false
    includeAnnotation = false

    addJvmTransformers(
        // @JvmBlocking
        SuspendTransforms.jvmBlockingTransformer,
        // @JvmAsync
        SuspendTransforms.jvmAsyncTransformer,

        // @JvmSuspendTrans
        SuspendTransforms.suspendTransTransformerForJvmBlocking,
        SuspendTransforms.suspendTransTransformerForJvmAsync,
        SuspendTransforms.suspendTransTransformerForJvmReserve,

        // @JvmSuspendTransProperty
        SuspendTransforms.jvmSuspendTransPropTransformerForBlocking,
        SuspendTransforms.jvmSuspendTransPropTransformerForAsync,
        SuspendTransforms.jvmSuspendTransPropTransformerForReserve,
    )

    // addJsTransformers(
    //     SuspendTransforms.suspendTransTransformerForJsPromise,
    // )
}

