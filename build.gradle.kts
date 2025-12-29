import org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_21

plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version "2.1.10"
    id("org.jetbrains.intellij.platform") version "2.10.5"
}

group = "com.pannous"
version = "1.0-SNAPSHOT"

kotlin {
    jvmToolchain(21)
}

repositories {
    mavenCentral()
    intellijPlatform {
        defaultRepositories()
    }
}

sourceSets {
    main {
        kotlin.srcDirs("src/wasp")
        resources.srcDirs("src/main/resources")
    }
    test {
        kotlin.srcDirs("test/wasp")
    }
}

tasks.processResources {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

tasks.register<Copy>("bundleWaspExecutable") {
    description = "Bundle the latest wasp executable with the plugin"
    group = "build"

    val waspSourcePath = "/Users/me/wasp/cmake-build-debug/wasp"
    val waspSource = file(waspSourcePath)

    if (waspSource.exists()) {
        from(waspSource)
        into("src/main/resources/bin/mac/arm")
        filePermissions {
            unix("rwxr-xr-x")
        }

        // Also copy to mac root for fallback
        doLast {
            copy {
                from(waspSource)
                into("src/main/resources/bin/mac")
                filePermissions {
                    unix("rwxr-xr-x")
                }
            }
        }
    }
}

tasks.named("processResources") {
    dependsOn("bundleWaspExecutable")
}

dependencies {
    intellijPlatform {
        local("/Applications/IntelliJ IDEA.app/Contents")
        plugin("com.redhat.devtools.lsp4ij", "0.6.0")
        testFramework(org.jetbrains.intellij.platform.gradle.TestFrameworkType.Platform)
    }
    testImplementation("junit:junit:4.13.2")
    testImplementation("org.opentest4j:opentest4j:1.3.0")
}

// Read more: https://plugins.jetbrains.com/docs/intellij/tools-intellij-platform-gradle-plugin.html
intellijPlatform {
    pluginConfiguration {
        ideaVersion {
            sinceBuild = "241"
            untilBuild = provider { null }
        }
    }
}

tasks {
    // Set the JVM compatibility versions
    withType<JavaCompile> {
        sourceCompatibility = "21"
        targetCompatibility = "21"
    }
    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        compilerOptions.jvmTarget.set(JVM_21)

    }

    test {
        testLogging {
            events("failed")
            exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.SHORT
            showStandardStreams = true
            showStackTraces = false
        }
    }

    signPlugin {
        certificateChain.set(System.getenv("CERTIFICATE_CHAIN"))
        privateKey.set(System.getenv("PRIVATE_KEY"))
        password.set(System.getenv("PRIVATE_KEY_PASSWORD"))
    }

    publishPlugin {
        token.set(System.getenv("PUBLISH_TOKEN"))
    }
}
