// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    id("com.google.gms.google-services") version "4.4.2" apply false
}
// File: build.gradle.kts (Project-level)
buildscript {
    dependencies {
        classpath("com.google.gms:google-services:4.4.0")
    }
}
