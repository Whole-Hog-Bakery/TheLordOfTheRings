buildscript {
    dependencies {
        classpath("com.android.tools.build:gradle:8.2.2")
        classpath("com.google.gms:google-services:4.4.0")
        classpath("com.google.firebase:firebase-crashlytics-gradle:2.9.9")
        classpath("app.cash.molecule:molecule-gradle-plugin:1.2.1")
    }
}
plugins {
    id("com.android.application") version "8.2.2" apply false
    id("org.jetbrains.kotlin.android") version "1.9.10" apply false
    id("com.google.dagger.hilt.android") version "2.50" apply false
    id("com.google.devtools.ksp") version "1.9.10-1.0.13" apply false
    id("org.jetbrains.kotlin.plugin.serialization") version "1.9.10" apply false
}