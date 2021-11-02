package com.jackson.build.apk.vo

import groovy.lang.Closure
import org.gradle.api.Action

/**
 * @author: jackson liao
 * @createDate: 2021/11/2 11:19
 * @description: 加固自动化配置
 */
open class BuildApkVo(/*project: Project*/) {
    var debugEnable: Boolean = false
    var enable: Boolean = false

    //    var debugSign: SignApkVo? = null
    var build360: Build360Vo = Build360Vo()

    // https://www.jianshu.com/p/58d86b4c0ee5
    //    val debugSign: NamedDomainObjectContainer<SignApkVo>? = project.container(SignApkVo::class.java)
//    val releaseSign: NamedDomainObjectContainer<SignApkVo>? =
//        project.container(SignApkVo::class.java)
//    val build360: NamedDomainObjectContainer<Build360Vo>? = null
//
//    fun build360(action: Action<NamedDomainObjectContainer<Build360Vo>>) {
//        if (this.build360 == null) return
//        action.execute(this.build360)
//    }

    fun build360(action: Action<Build360Vo>) {
//        if (this.build360 == null) return
        action.execute(build360)
    }

//    fun build360(c:Closure) {
//
//    }
}

/**
 * apk签名信息
 */
open class SignApkVo(
    /**签名pwd*/
    var signPassword: String,
    /**签名别名key*/
    var signKeyAlias: String,
    /**签名别名密码*/
    var signKeyPwd: String,
    /**签名文件路径*/
    var signPath: String
)