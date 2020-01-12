# GradlePlugin
Android developer plugin tools.


## 模块化运行插件Beetle
[ ![Download](https://api.bintray.com/packages/jiahui/jiahui/Beetle/images/download.svg?version=1.0.0) ](https://bintray.com/jiahui/jiahui/Beetle/1.0.0/link)

Beetle是一款Android模块化运行配置插件，可以灵活配置各个模块的单独运行，相比原有的直接修改build.gradle更直观，更灵活，与传统模块化配置区别可查看该文章<>,具体的使用配置如下：

- step1
在跟项目下添加：

```gradle
// 引入
buildscript {
    ext.kotlin_version = '1.3.50'
    repositories {
        google()
        jcenter()
        maven { url "https://jitpack.io" }
    }
    dependencies {
        classpath 'com.android.tools.build:gradle:3.5.3'
        classpath "org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin_version"
        
        classpath "com.jackson:Beetle:1.0.0"
    }
}

// beetle 配置
 apply plugin: 'beetle.config'

 beetle {
     debugEnable true
     apps {
         app {
             applicationId "com.jackson.gradleapp"
             mainActivity "com.jackson.module.home.MainActivity"
             modules ':modules:module_search', // 这个是有在isRunAlone为true的是否生效
                     ':modules:module_home',
                     ':modules:module_setting'
             // 注意这两个配置优先级比app build.gradle低
             versionCode 100 
             versionName "1.0.0"
         }

     }

     modules {
         search {
             name ":modules:module_search"
             applicationId "com.jackson.module.search"
             mainActivity ".MainActivity"
             isRunAlone false
         }

         home {
             name ":modules:module_home"
             applicationId "com.jackson.module.home"
             mainActivity ".MainActivity"
             isRunAlone false
         }

         setting {
             name ":modules:module_setting"
             applicationId "com.jackson.module.setting"
             mainActivity ".SettingsActivity"
             isRunAlone false
         }
     }
 }
```
- step2
在module的每个build.gradle里面添加：
```
apply plugin: 'beetle.modules'  //注意放在第一行也就是apply plugin: 'com.android.application'的位置
```

这样就算配置好了，要想单独运行只需要修改项目下build.gradle的beetle配置即可，轻松食用。
