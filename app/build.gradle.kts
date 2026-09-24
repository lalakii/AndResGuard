import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("AndResGuard")
}
android {
    namespace = "cn.lalaki.andresguarddemo"
    compileSdkPreview = "CinnamonBun"
    buildToolsVersion = "37.0.0"
    defaultConfig {
        applicationId = "cn.lalaki.andresguarddemo"
        minSdk = 26
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }
    signingConfigs {
        getByName("debug") {
            val testPassword = "123456"
            storePassword = testPassword
            keyPassword = testPassword
            keyAlias = "key0"
            storeFile = file("../debug.jks")
        }
    }
    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
            signingConfig = signingConfigs.getByName("debug")
        }
        debug {
            signingConfig = signingConfigs.getByName("debug")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
    kotlin {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_21)
            freeCompilerArgs.add("-opt-in=kotlin.RequiresOptIn")
        }
    }
}
dependencies {
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.junit.ktx)
    implementation(libs.kotlin.stdlib.jdk8)
    implementation(libs.commons.codec)
    implementation(libs.commons.io)
    testImplementation(libs.junit)
}
andResGuard {
    onlyV3V4Sign = true
    delMetaInf = true
    use7zip = true
    useSign = true
}
