plugins {
    id("java-library")
    alias(libs.plugins.jetbrains.kotlin.jvm)
}
dependencies {
    implementation(project(":core:model"))
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
