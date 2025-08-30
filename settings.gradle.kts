import org.gradle.kotlin.dsl.maven

pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenLocal()
        maven("https://repo.papermc.io/repository/maven-public/")
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.9.0"
}

rootProject.name = "StaticPaper"

include("staticpaper-api")
include("staticpaper-server")
//include("staticpaper-asp-core")
//include("staticpaper-asp-api")
