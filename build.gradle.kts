// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.jetbrainsKotlinAndroid) apply false
//    kotlin("kapt") version "1.9.21"
    id("com.google.dagger.hilt.android") version "2.49" apply false
    id("org.jetbrains.kotlin.plugin.serialization") version "1.8.21"
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin") version "2.0.1"
}