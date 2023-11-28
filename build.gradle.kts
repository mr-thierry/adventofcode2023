plugins {
    kotlin("jvm") version "1.9.20"
}

repositories {
    mavenCentral()
}

tasks {
    sourceSets {
        main {
            java.srcDirs("src")
        }
    }

    wrapper {
        gradleVersion = "8.4"
    }
}

dependencies {
    implementation("org.junit.jupiter:junit-jupiter:5.9.0")
    implementation("com.willowtreeapps.assertk:assertk-jvm:0.25")
}