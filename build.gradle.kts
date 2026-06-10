import org.jetbrains.kotlin.gradle.dsl.KotlinVersion
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

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
    maven("https://maven.shedaniel.me")
    maven("https://maven.fabricmc.net/")
    maven("https://maven.ladysnake.org/releases")
    maven("https://maven.minecraftforge.net/")
    maven("https://maven.parchmentmc.org")
    maven("https://maven.shedaniel.me")
    maven("https://maven.terraformersmc.com/releases")
    maven("https://thedarkcolour.github.io/KotlinForForge")
    maven("https://jitpack.io")
    maven("https://maven.su5ed.dev/releases")
    maven("https://maven.theillusivec4.top/")
    maven("https://maven.wispforest.io/releases")

    flatDir { dir(rootProject.file("libs")) }

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

        mappings {
            official()
            parchment("2024.11.17")
        }

        dependencies {
            compileOnly("org.spongepowered:mixin:0.8.5")
            modCompileOnly(libs.forgifiedFabricApi)

            libs.bundles.hexcasting.neoforge.get().forEach {
                modCompileOnly(it)
            }

            compileOnly("mod.master_bw3:BundledKtor-1.0.0")
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

            // hex casting + deps
            libs.bundles.hexcasting.fabric.get().forEach {
                modImplementation(it)
            }
            libs.bundles.cardinalComponents.get().forEach {
                modImplementation(it)
            }
            modRuntimeOnly(libs.architectury.fabric)

            modImplementation(libs.clothConfig.fabric) {
                exclude(group = "net.fabricmc.fabric-api")
            }
            modImplementation(libs.modMenu)

            // ktor
            implementation("mod.master_bw3:BundledKtor-1.0.0")
            include("mod.master_bw3:BundledKtor-1.0.0")
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
        loaderVersion = libs.versions.neoforge
        minecraftVersion = "1.21.1"

        mappings {
            official()
            parchment("2024.11.17")
        }

        dependencies {
            modImplementation(libs.forgifiedFabricApi)
            modImplementation(libs.kotlin.forge)

            // hex casting + deps
            libs.bundles.hexcasting.neoforge.get().forEach {
                modImplementation(it)
            }
            modRuntimeOnly(libs.architectury.neoforge)

            implementation("mod.master_bw3:BundledKtor-1.0.0")
            include("mod.master_bw3:BundledKtor-1.0.0")
        }

        runs {
            client {
                jvmArguments.add("-XX:+AllowEnhancedClassRedefinition")
            }
            server()
        }

        metadata {
            modLoader = "kotlinforforge"
        }
    }
}

tasks.withType<KotlinCompile>().configureEach {
    compilerOptions {
        languageVersion = KotlinVersion.KOTLIN_2_2
        freeCompilerArgs.addAll(
            "-Xmulti-platform",
            "-Xno-check-actual",
            "-Xexpect-actual-classes",
        )
    }
}

configurations.configureEach {
    resolutionStrategy.force("org.slf4j:slf4j-api:2.0.9")

    resolutionStrategy.force(
        "org.ow2.asm:asm:9.8",
        "org.ow2.asm:asm-tree:9.8",
        "org.ow2.asm:asm-commons:9.8",
        "org.ow2.asm:asm-util:9.8",
        "org.ow2.asm:asm-analysis:9.8"
    )
}
