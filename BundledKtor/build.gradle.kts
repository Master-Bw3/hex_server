plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(ktorLibs.plugins.ktor)
}

group = "mod.master_bw3"
version = "1.0.0"

application {
    mainClass.set("mod.master_bw3.MainKt")
}

kotlin {
    jvmToolchain(21)
}
dependencies {
    implementation(ktorLibs.server.config.yaml)
    implementation(ktorLibs.server.core)
    implementation(ktorLibs.server.jetty)
    implementation(libs.logback.classic)
}

configurations.configureEach {
    exclude(group = "org.ow2.asm")
    exclude(group = "org.slf4j")
}

ktor {
    fatJar {
        archiveFileName.set("BundledKtor-1.0.0.jar")
    }
}