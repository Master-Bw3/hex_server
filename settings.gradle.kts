dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }

    versionCatalogs {
        create("libs") {
            from(files("libs.versions.toml"))
        }
        create("ktorLibs").from("io.ktor:ktor-version-catalog:3.5.0")
    }
}

pluginManagement {
    repositories {
        // Repositories where you can get
        mavenCentral()
        gradlePluginPortal()
        maven("https://maven.msrandom.net/repository/cloche/")
        maven("https://maven.blamejared.com/")
    }
}

include(":BundledKtor")