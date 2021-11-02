package com.jackson.build.apk.utils

import com.jackson.build.apk.core.Constants
import com.jackson.build.apk.vo.BuildApkVo
import org.gradle.api.Project
import org.gradle.api.UnknownDomainObjectException

/**
 * @author: jackson liao
 * @createDate: 2021/11/2 11:38
 * @description: 工具类
 */
object BuildApkUtils {

    fun getBuildApkConfigVo(project: Project?): BuildApkVo? {
       if (project == null)  {
            throw NullPointerException("project == null, please check buildApk config !")
        }
        return try {
            project.extensions.getByName(Constants.BUILD_APK_NAME) as? BuildApkVo
        } catch (ignored: UnknownDomainObjectException) {
            if (project.parent != null) {
                getBuildApkConfigVo(project.parent)
            } else {
                throw  UnknownDomainObjectException(
                    ignored.message ?: "Can not find buildApk config?"
                )
            }
        }
    }
}