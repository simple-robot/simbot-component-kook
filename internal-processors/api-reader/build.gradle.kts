import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    kotlin("jvm")
}

repositories {
    mavenCentral()
}

kotlin {
    jvmToolchain(11)
    compilerOptions {
        javaParameters = true
        jvmTarget.set(JvmTarget.JVM_11)
    }
}

configJavaCompileWithModule()

dependencies {
//    implementation(project(":annotations"))
    api(libs.ksp)
    api(libs.kotlinPoet.ksp)
    testImplementation(kotlin("test-junit5"))
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}

