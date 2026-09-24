import org.gradle.kotlin.dsl.extra

// 像这样配置插件也可以，不要 plugins 块
//buildscript {
//    repositories {
//        mavenCentral()
//        google()
//    }
//    dependencies {
//        classpath "cn.lalaki.AndResGuard:cn.lalaki.AndResGuard.gradle.plugin:2.0.4"
//    }
//}
plugins {
    // AndResGuard 推荐这样配置
    // id("cn.lalaki.AndResGuard") version "${ANDRESGUARD_VERSION}" apply false
    alias(libs.plugins.lalaki.andresguard) apply false
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.lalaki.central) apply false
}
extra.set("POM_PACKAGING", "pom")
extra.set("POM_DESCRIPTION", "AndResGuard Unofficial")
extra.set("versionName", providers.gradleProperty("ANDRESGUARD_VERSION").get())
extra.set("groupID", providers.gradleProperty("GROUP_LIB").get())
extra.set("javaVersion", JavaVersion.VERSION_21)
