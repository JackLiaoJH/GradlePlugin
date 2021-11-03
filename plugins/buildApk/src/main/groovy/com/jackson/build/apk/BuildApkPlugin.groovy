package com.jackson.build.apk

import com.android.build.gradle.AppExtension
import com.jackson.build.apk.core.BuildApkTasks
import com.jackson.build.apk.core.Constants
import com.jackson.build.apk.vo.BuildApkVo
import com.jackson.build.apk.vo.SignApkVo
import org.gradle.api.Plugin
import org.gradle.api.Project

class BuildApkPlugin implements Plugin<Project> {

    String rootPath = ""
    String debugRootPath = ""
    private SignApkVo releaseSign = new SignApkVo()

    @Override
    void apply(Project project) {
        println("BuildApkPlugin start.....$project")
        def buildApkVo = project.getExtensions().create(Constants.BUILD_APK_NAME, BuildApkVo)
        project.afterEvaluate {
            println("BuildApkPlugin config:\n $buildApkVo")
            def android = project.getExtensions().getByType(AppExtension)
            initApkOutDir(android)
            initSignInfo(android)

            new BuildApkTasks(project, rootPath, buildApkVo, releaseSign).dependsTasks()
        }
    }


    private void initApkOutDir(AppExtension android) {
        android.applicationVariants.all { variant ->
            String apkRootPath = variant.packageApplicationProvider.get().outputDirectory
            println("apkRootPath=$apkRootPath")
            variant.outputs.all { output ->
                if (variant.buildType.name == "release") {
                    rootPath = "${apkRootPath}\\${output.outputFile.name}"
                    println("release--> $rootPath")
                } else if (variant.buildType.name == "debug") {
                    debugRootPath = "${apkRootPath}\\${output.outputFile.name}"
                    println("debug--> $debugRootPath")
                }
            }
        }
        def versionName = android.defaultConfig.versionName
        println("$rootPath,$debugRootPath ,$versionName")
    }


    private void initSignInfo(android) {
        android.signingConfigs.forEach {
            if (it.name == "release") {
                releaseSign.signPassword = it.storePassword
                releaseSign.signKeyAlias = it.keyAlias
                releaseSign.signKeyPwd = it.keyPassword
                releaseSign.signPath = it.storeFile.absolutePath
                println("release sign: $releaseSign")
            }
        }
    }


}