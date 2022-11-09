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