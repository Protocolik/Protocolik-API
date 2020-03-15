plugins {
    kotlin("jvm") version "1.3.70"
    `maven-publish`
}

group = "com.github.protocolik"
version = "0.0.1-SNAPSHOT"

repositories {
    mavenLocal()
    jcenter()
    maven { setUrl("https://jitpack.io/") }
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    api("com.google.code.gson", "gson", "2.8.6")
    api("it.unimi.dsi", "fastutil", "8.3.1")
    api("io.netty", "netty-buffer", "4.1.47.Final")
    api("io.netty", "netty-transport", "4.1.47.Final")
    api("com.github.protocolik", "protocolik-nbt", "1.0.0")
}