dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            from(files("libs.versions.toml"))
        }
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