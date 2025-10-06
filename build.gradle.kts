plugins {
    kotlin("jvm") version "1.9.25"
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "com.github.shynixn"
version = "1.0.0"

repositories {
    mavenLocal()
    mavenCentral()
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://oss.sonatype.org/content/repositories/snapshots")
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")
    maven("https://maven.elmakers.com/repository/")
    maven("https://jitpack.io")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

dependencies {
    compileOnly("org.spigotmc:spigot-api:1.21.3-R0.1-SNAPSHOT")
    compileOnly("me.clip:placeholderapi:2.11.6")
    
    // Runtime dependencies
    implementation("com.github.shynixn.mccoroutine:mccoroutine-folia-api:2.22.0")
    implementation("com.github.shynixn.mccoroutine:mccoroutine-folia-core:2.22.0")
    implementation("com.github.shynixn:fasterxml:1.2.0")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.9.25")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    
    // Test dependencies
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.0")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.10.0")
    testImplementation("org.mockito:mockito-core:5.5.0")
}

tasks {
    compileKotlin {
        kotlinOptions {
            jvmTarget = "21"
        }
    }
    
    shadowJar {
        archiveFileName.set("ShyParticles-${project.version}.jar")
        
        relocate("com.github.shynixn.mccoroutine", "com.github.shynixn.shyparticles.lib.com.github.shynixn.mccoroutine")
        relocate("com.github.shynixn.fasterxml", "com.github.shynixn.shyparticles.lib.com.github.shynixn.fasterxml")
        relocate("kotlin", "com.github.shynixn.shyparticles.lib.kotlin")
        relocate("kotlinx", "com.github.shynixn.shyparticles.lib.kotlinx")
    }
    
    test {
        useJUnitPlatform()
    }
}