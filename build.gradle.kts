import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent

plugins {
    id("io.papermc.paperweight.patcher")
}

paperweight {
    upstreams.register("aspaper") {
        repo = github("infernalsuite", "advancedslimepaper")
        ref = providers.gradleProperty("aspRef")

        patchFile {
            path = "aspaper-server/build.gradle.kts"
            outputFile = file("staticpaper-server/build.gradle.kts")
            patchFile = file("staticpaper-server/build.gradle.kts.patch")
        }
        patchFile {
            path = "aspaper-api/build.gradle.kts"
            outputFile = file("staticpaper-api/build.gradle.kts")
            patchFile = file("staticpaper-api/build.gradle.kts.patch")
        }

        patchDir("staticpaper-asp-core") {
            upstreamPath = "core"
            patchesDir = file("staticpaper-asp-core-patches")
            outputDir = file("staticpaper-asp-core")
        }
        patchDir("staticpaper-asp-api") {
            upstreamPath = "api"
            patchesDir = file("staticpaper-asp-api-patches")
            outputDir = file("staticpaper-asp-api")
        }
//        patchDir("aspaperApi") {
//            upstreamPath = "aspaper-api"
//            excludes = listOf("build.gradle.kts", "build.gradle.kts.patch", "paper-patches")
//            patchesDir = file("staticpaper-api/aspaper-patches")
//            outputDir = file("aspaper-api")
//        }
        patchRepo("paperApi") {
            upstreamPath = "paper-api"
            patchesDir = file("staticpaper-api/paper-patches")
            outputDir = file("paper-api")
        }
    }
}

val paperMavenPublicUrl = "https://repo.papermc.io/repository/maven-public/"

subprojects {
    apply(plugin = "java-library")
    apply(plugin = "maven-publish")

    extensions.configure<JavaPluginExtension> {
        toolchain {
            languageVersion = JavaLanguageVersion.of(21)
        }
    }

    repositories {
        mavenCentral()
        maven(paperMavenPublicUrl)
    }

    tasks.withType<AbstractArchiveTask>().configureEach {
        isPreserveFileTimestamps = false
        isReproducibleFileOrder = true
    }
    tasks.withType<JavaCompile> {
        options.encoding = Charsets.UTF_8.name()
        options.release = 21
        options.isFork = true
    }
    tasks.withType<Javadoc> {
        options.encoding = Charsets.UTF_8.name()
    }
    tasks.withType<ProcessResources> {
        filteringCharset = Charsets.UTF_8.name()
    }
    tasks.withType<Test> {
        testLogging {
            showStackTraces = true
            exceptionFormat = TestExceptionFormat.FULL
            events(TestLogEvent.STANDARD_OUT)
        }
    }

    extensions.configure<PublishingExtension> {
        repositories {
            maven("https://repo.staticstudios.net/private") {
                name = "StaticStudios"
                credentials(PasswordCredentials::class)
            }
        }
    }
}
