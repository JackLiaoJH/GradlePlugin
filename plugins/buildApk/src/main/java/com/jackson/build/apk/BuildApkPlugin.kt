package com.jackson.build.apk

import com.jackson.build.apk.core.Constants
import com.jackson.build.apk.vo.BuildApkVo
import org.gradle.api.Plugin
import org.gradle.api.Project

class BuildApkPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        println("BuildApkPlugin start.....$project")
        val buildApkVo = project.extensions.create(Constants.BUILD_APK_NAME, BuildApkVo::class.java)
//        val buildApkVo = BuildApkVo(project)
//        project.extensions.add(Constants.BUILD_APK_NAME, buildApkVo)
        println("BuildApkPlugin config: ${buildApkVo.enable}")
        var newBuildApkVo: BuildApkVo
        project.afterEvaluate {
            newBuildApkVo = project.extensions.getByName(Constants.BUILD_APK_NAME) as BuildApkVo
//
            println("BuildApkPlugin config2: ${buildApkVo.debugEnable}, ${newBuildApkVo.build360}")
        }
    }
}