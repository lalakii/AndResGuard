# AndResGuard.Unofficial

[![Maven Central](https://img.shields.io/maven-central/v/cn.lalaki.AndResGuard/cn.lalaki.AndResGuard.gradle.plugin.svg?label=Maven%20Central&logo=sonatype)](https://central.sonatype.com/artifact/cn.lalaki.AndResGuard/cn.lalaki.AndResGuard.gradle.plugin/) [![License: Apache-2.0 (shields.io)](https://img.shields.io/badge/License-Apache--2.0-c02041?logo=apache)](https://github.com/lalakii/AndResGuard/blob/master/LICENSE)

目前使用的 Gradle 8.13 + AGP 8.9.0 编译，运行良好

新增v3/v4签名算法支持，修复一些警告、弃用以及报错~

目前用在一些自己的小玩具上，挺不错的~ 不过由于自身没有什么时间这个项目就不长期维护拉

演示项目 [AndResGuardDemo](https://github.com/lalakii/AndResGuard/tree/master/app)

## 文档

[AndResGuard](https://github.com/shwenzhang/AndResGuard)

## 如何使用

修改项目中对应的文件

+ build.gradle.kts

```kts
plugins {
    id("cn.lalaki.AndResGuard") version "$latest_version" apply false
}
```

+ app/build.gradle.kts

```kts
plugins {
    id("AndResGuard")
}

andResGuard {
    // 增加了两个配置项，其他的参数查看官方仓库文档
    onlyV3V4Sign = boolean    // 仅使用v3/v4签名
    delMetaInf = boolean      // 删除apk的META-INF目录，因为删除操作在apk签名之前，所以不会影响v1签名

   //  注意：此节点已经重构。不再需要手动配置，记得在你自己的项目中注释掉
   //  sevenzip {
   //      artifact = "cn.lalaki.AndResGuard:SevenZip:$latest_version"
   // }
}
```

+ gradle.properties

```properties
# 可能需要禁用资源优化，生成的APK才能正常工作
android.enableResourceOptimizations=false

# 可能需要禁用Gradle守护进程，否则部分编译时生成的文件会被占用(Windows)
org.gradle.daemon=false
```

## 编译APK

准备就绪后，在项目的根目录打开终端，执行下面的命令

```console
# Powershell
.\gradlew resguardRelease

# Linux
./gradlew resguardRelease
```

编译完成终端会输出```output: 路径```指向最终生成的apk文件

##

## by lalaki.cn
