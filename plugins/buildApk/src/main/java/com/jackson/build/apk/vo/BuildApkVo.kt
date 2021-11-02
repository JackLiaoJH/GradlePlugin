package com.jackson.build.apk.vo

/**
 * @author: jackson liao
 * @createDate: 2021/11/2 11:19
 * @description: 加固自动化配置
 */
open class BuildApkVo(/*project: Project*/) {
    var debugEnable: Boolean = false
    var enable: Boolean = false

//    var debugSign: SignApkVo? = null
    var build360: Build360Vo? = null

//    val debugSign: NamedDomainObjectContainer<SignApkVo>? = project.container(SignApkVo::class.java)
//    val releaseSign: NamedDomainObjectContainer<SignApkVo>? =
//        project.container(SignApkVo::class.java)
//    val build360: NamedDomainObjectContainer<Build360Vo>? =
//        project.container(Build360Vo::class.java)
}

/**
 * apk签名信息
 */
open class SignApkVo(
    /**签名pwd*/
    val signPassword: String,
    /**签名别名key*/
    val signKeyAlias: String,
    /**签名别名密码*/
    val signKeyPwd: String,
    /**签名文件路径*/
    val signPath: String
)