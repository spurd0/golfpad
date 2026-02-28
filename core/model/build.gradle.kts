plugins {
    id("java-library")
    alias(libs.plugins.jetbrains.kotlin.jvm)
}
java {
    sourceCompatibility = JavaVersion.valueOf(libs.versions.javaVersion.get())
    targetCompatibility = JavaVersion.valueOf(libs.versions.javaVersion.get())
}
kotlin {
    compilerOptions {
        jvmTarget = org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11
    }
}
