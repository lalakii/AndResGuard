plugins {
    id("com.android.application")
    id("AndResGuard")
    id("org.jetbrains.kotlin.android") version "2.1.10"
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
            storeFile = file("../test.jks")
            storePassword = "123456"
            keyAlias = "key0"
            keyPassword = "123456"
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
        sourceCompatibility = JavaVersion.VERSION_20
        targetCompatibility = JavaVersion.VERSION_20
    }
    kotlinOptions {
        jvmTarget = "20"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            excludes += "/kotlin/**"
            excludes + "DebugProbesKt.bin"
            excludes + "kotlin-tooling-metadata.json"
        }
    }
}

dependencies {
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:2.1.10")
    testImplementation("junit:junit:4.12")
}

andResGuard {
    onlyV3V4Sign = true
    delMetaInf = true
    use7zip = true
    useSign = true
}
