plugins {
    kotlin("jvm") version "1.9.21"
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

group = "icu.takeneko"
version = "0.0.1"

repositories {
    mavenCentral()
    maven { url = uri("https://jitpack.io") }
    maven { url = uri("https://git.foxapplication.com/api/packages/koro/maven") }
}

dependencies {
    compileOnly("com.github.OhMyMinecraftServer:omms-central:0.16.1")
    implementation("com.foxapplication.mc:foxcore:1.0.1")
    implementation("com.foxapplication.mc:interaction-base:1.2.1")
    implementation("com.foxapplication.mc:interconnectioncommon:1.0.1")
    implementation("net.kyori:adventure-api:4.16.0")
    implementation("net.kyori:adventure-text-serializer-gson:4.16.0")
    implementation("net.kyori:adventure-text-serializer-legacy:4.16.0")
    implementation("net.kyori:examination-api:1.3.0")
    implementation("net.kyori:examination-string:1.3.0")
    testImplementation("org.jetbrains.kotlin:kotlin-test")
}

tasks {
    shadowJar {
        archiveClassifier.set("full")
        exclude("kotlin*")
        dependencies{
            exclude(dependency("com.google.code.gson:gson"))
            exclude(dependency("org.slf4j:slf4j-api"))
        }

        relocate("net.kyori", "icu.takeneko.deps.adventure")
    }
}

tasks.test {
    useJUnitPlatform()
}

tasks {
    withType<ProcessResources> {
        inputs.property("version", project.version)
        filesMatching("plugin.metadata.json") {
            expand(mapOf("version" to project.version))
        }
    }
}

kotlin {
    jvmToolchain(17)
}