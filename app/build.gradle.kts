plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("AndResGuard")
}
android {
    namespace = "cn.lalaki.andresguarddemo"
    compileSdk = 35
    defaultConfig {
        applicationId = "cn.lalaki.andresguarddemo"
        minSdk = 26
        targetSdk = 35
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
            storeFile = file("../debug.jks")
            keyAlias = "key0"
            storePassword = testPassword
            keyPassword = testPassword
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
    kotlinOptions {
        jvmTarget = "21"
    }
}
dependencies {
    implementation(libs.androidx.appcompat)
    implementation(libs.kotlin.stdlib.jdk8)
    testImplementation(libs.junit)
}
andResGuard {
    onlyV3V4Sign = true
    delMetaInf = true
    use7zip = true
    useSign = true
}
