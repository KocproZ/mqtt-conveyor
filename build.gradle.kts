plugins {
    kotlin("jvm") version "1.8.10"
    id("org.graalvm.buildtools.native") version "0.9.13"
//    id("com.github.johnrengelman.shadow") version "8.0.0"
    application
}

group = "me.kocproz"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.eclipse.paho:org.eclipse.paho.mqttv5.client:1.2.5")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:2.14.2")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.14.2")
    implementation("org.jetbrains.kotlinx:kotlinx-cli:0.3.5")
    implementation("org.tinylog:tinylog-api-kotlin:2.6.0")
    implementation("org.tinylog:tinylog-impl:2.6.0")


    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions.jvmTarget = "17"
}

kotlin {
    jvmToolchain(17)
}

application {
    mainClass.set("me.kocproz.conveyor.MainKt")
}

graalvmNative {
    agent {
        defaultMode.set("standard")
    }
    binaries {
        named("main") {
            imageName.set("ha-mqtt-conveyor")
            javaLauncher.set(javaToolchains.launcherFor {
                languageVersion.set(JavaLanguageVersion.of(17))
                vendor.set(JvmVendorSpec.matching("GraalVM Community"))
                sharedLibrary.set(false)
                buildArgs.add("-H:ReflectionConfigurationResources=reflect-config.json")
                buildArgs.add("-H:+ReportExceptionStackTraces")
                buildArgs.add("-H:+ReportUnsupportedElementsAtRuntime")
                val resourceBundles = setOf(
                    "org.eclipse.paho.mqttv5.client.internal.nls.logcat",
                    "org.eclipse.paho.mqttv5.common.nls.logcat",
                    "org.eclipse.paho.mqttv5.common.nls.messages"
                )
                buildArgs.add("-H:IncludeResourceBundles="+resourceBundles.joinToString(","))
            })
            useFatJar.set(true)
        }
    }
    toolchainDetection.set(false)
}
