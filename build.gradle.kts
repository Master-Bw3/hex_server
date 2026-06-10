import java.awt.AWTEventMulticaster.add

plugins {
    alias(libs.plugins.cloche)
    alias(libs.plugins.kotlin.jvm)
}

group = "mod.master_bw3.hex_server"
version = "0.1.0"

kotlin {
    jvmToolchain(21)
}

repositories {
    cloche {
        mavenNeoforgedMeta()
        mavenNeoforged()
        mavenFabric()
        mavenParchment()
        librariesMinecraft()
        main()
    }
    mavenLocal()
    mavenCentral()
    maven("https://maven.blamejared.com")
    maven("https://maven.fabricmc.net/")
    maven("https://maven.ladysnake.org/releases")
    maven("https://maven.minecraftforge.net/")
    maven("https://maven.parchmentmc.org")
    maven("https://maven.shedaniel.me")
    maven("https://maven.terraformersmc.com/releases")
    maven("https://thedarkcolour.github.io/KotlinForForge")

    exclusiveContent {
        filter {
            includeGroup("maven.modrinth")
        }
        forRepository {
            maven { url = uri("https://api.modrinth.com/maven") }
        }
    }
}

cloche {
    metadata {
        modId = "hex_server"
        name = "Hex Server"
        description = "Hex Server"
        license = "MIT"

        author("Master_Bw3")
    }

    common {
        dependencies {
            compileOnly("org.spongepowered:mixin:0.8.5")

            libs.bundles.ktor.asProvider().get().forEach {
                compileOnly(it) {
                    exclude("org.slf4j")
                    exclude("org.ow2.asm")
                }
            }

            libs.bundles.coroutines.get().forEach {
                compileOnly(it)
            }
        }

        mappings {
            official()
            parchment("2024.11.17")
        }

        metadata {
        }
    }

    fabric("fabric:1.21.1") {
        loaderVersion = libs.versions.fabric.loader
        minecraftVersion = "1.21.1"

        mappings {
            official()
            parchment("2024.11.17")
        }

//        client()
        includedClient()

        dependencies {
            fabricApi(libs.versions.fabric.api)
            modImplementation(libs.kotlin.fabric)

            libs.bundles.ktor.asProvider().get().forEach {
                implementation(it)
                include(it)
            }

            libs.bundles.ktor.deps.get().forEach {
                include(it)
            }

            libs.bundles.coroutines.get().forEach {
                implementation(it)
                include(it)
            }

        }

        runs {
            client {
                jvmArguments.add("-XX:+AllowEnhancedClassRedefinition")
            }
            server()
        }


        metadata {
            entrypoint("main") {
                value = "mod.master_bw3.hex_server.FabricHexServer"
                adapter = "kotlin"
            }
            entrypoint("client") {
                value = "mod.master_bw3.hex_server.client.FabricHexServerClient"
                adapter = "kotlin"
            }
        }
    }

    neoforge("neoforge:1.21.1") {
        loaderVersion = "21.1.233"
        minecraftVersion = "1.21.1"

        mappings {
            official()
            parchment("2024.11.17")
        }

        dependencies {
            libs.bundles.ktor.asProvider().get().forEach {
                modApi(it) {
                    exclude("org.slf4j")
                    exclude("org.ow2.asm")
                }
            }

            libs.bundles.ktor.deps.get().forEach {
                include(it) {
                    exclude("org.slf4j")
                    exclude("org.ow2.asm")
                }
            }

            libs.bundles.coroutines.get().forEach {
                modApi(it)
                include(it)
            }

        }

        runs {
            client {
                jvmArguments.add("-XX:+AllowEnhancedClassRedefinition")
            }
            server()
        }
    }
}