plugins {
    `java-library`
    kotlin("jvm") // version "1.6.0" // apply false
    kotlin("plugin.serialization") // version "1.6.0" // apply false
    id("org.jetbrains.dokka")
}



repositories {
    mavenLocal()
    mavenCentral()
    maven {
        url = uri(Sonatype.`snapshot-oss`.URL)
        mavenContent {
            snapshotsOnly()
        }
    }
}


val isSnapshotOnly = System.getProperty("snapshotOnly") != null
val isReleaseOnly = System.getProperty("releaseOnly") != null

val isPublishConfigurable = when {
    isSnapshotOnly -> P.ComponentKaiheila.isSnapshot
    isReleaseOnly -> !P.ComponentKaiheila.isSnapshot
    else -> true
}


println("isSnapshotOnly: $isSnapshotOnly")
println("isReleaseOnly: $isReleaseOnly")
println("isPublishConfigurable: $isPublishConfigurable")

group = P.ComponentKaiheila.GROUP
version = P.ComponentKaiheila.VERSION

subprojects {
    group = P.ComponentKaiheila.GROUP
    version = P.ComponentKaiheila.VERSION

    apply(plugin = "java")

    repositories {
        mavenLocal()
        mavenCentral()
        maven {
            url = uri(Sonatype.`snapshot-oss`.URL)
            mavenContent {
                snapshotsOnly()
            }
        }
    }

    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
        kotlinOptions {
            freeCompilerArgs = freeCompilerArgs + listOf("-Xjvm-default=all")
        }
    }

    if (isPublishConfigurable) {
        apply(plugin = "maven-publish")
        apply(plugin = "signing")
        // configurePublishing(name)
        // println("[publishing-configure] - [$name] configured.")
        // set gpg file path to root
        // val secretKeyRingFileKey = "signing.secretKeyRingFile"
        // val secretRingFile = File(project.rootDir, getProp(secretKeyRingFileKey)?.toString() ?: "ForteScarlet.gpg")
        // extra[secretKeyRingFileKey] = secretRingFile
        // setProperty(secretKeyRingFileKey, secretRingFile)

        // signing {
        //     sign(publishing.publications)
        // }
    }


    configurations.all {
        // check for updates every build
        resolutionStrategy.cacheChangingModulesFor(0, "seconds")
    }
}


tasks.withType<JavaCompile>() {
    options.encoding = "UTF-8"
}



// val sonatypeUsername: String? = getProp("sonatype.username")?.toString()
// val sonatypePassword: String? = getProp("sonatype.password")?.toString()

// if (sonatypeUsername != null && sonatypePassword != null) {
    // nexusPublishing {
    //     packageGroup.set(P.ComponentTencentGuild.GROUP)
    //
    //     useStaging.set(
    //         project.provider { !project.version.toString().endsWith("SNAPSHOT", ignoreCase = true) }
    //     )
    //
    //     transitionCheckOptions {
    //         maxRetries.set(20)
    //         delayBetween.set(java.time.Duration.ofSeconds(5))
    //     }
    //
    //     repositories {
    //         sonatype {
    //             snapshotRepositoryUrl.set(uri(Sonatype.`snapshot-oss`.URL))
    //             username.set(sonatypeUsername)
    //             password.set(sonatypePassword)
    //         }
    //
    //     }
    // }
// } else {
//     println("[WARN] - sonatype.username or sonatype.password is null, cannot config nexus publishing.")
// }


fun org.jetbrains.dokka.gradle.AbstractDokkaTask.configOutput(format: String) {
    outputDirectory.set(rootProject.file("dokka/$format/v$version"))
}

tasks.dokkaHtmlMultiModule.configure {
    configOutput("html")
}
tasks.dokkaGfmMultiModule.configure {
    configOutput("gfm")
}

tasks.register("dokkaHtmlMultiModuleAndPost") {
    group = JavaBasePlugin.DOCUMENTATION_GROUP
    dependsOn("dokkaHtmlMultiModule")
    doLast {
        val outDir = rootProject.file("dokka/html")
        val indexFile = File(outDir, "index.html")
        indexFile.createNewFile()
        indexFile.writeText(
            """
            <html xmlns="http://www.w3.org/1999/xhtml">
            <head>
                <meta http-equiv="refresh" content="0;URL='v$version'" />
            </head>
            <body>
            </body>
            </html>
        """.trimIndent()
        )

        // TODO readme
    }
}

tasks.create("createChangelog") {
    group = "build"
    doFirst {
        val version = "v${rootProject.version}"
        println("Generate change log for $version ...")
        // configurations.runtimeClasspath
        val changelogDir = rootProject.file(".changelog").also {
            it.mkdirs()
        }
        val file = File(changelogDir, "$version.md")
        if (!file.exists()) {
            file.createNewFile()
            val autoGenerateText = """
                

                ## 其他日志
                
            """.trimIndent()


            file.writeText(autoGenerateText)
        }


    }
}




configurations.all {
    // check for updates every build
    resolutionStrategy.cacheChangingModulesFor(0, "seconds")
}