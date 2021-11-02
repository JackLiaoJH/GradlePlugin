package com.jackson.build.apk.vo

/**
 * @author: jackson liao
 * @createDate: 2021/11/2 11:19
 * @description: 360 加固配置
 */
open class Build360Vo {
    var enable: Boolean = false
    var account: String? = null
    var password: String? = null


    override fun toString(): String {
        return "Build360Vo(enable=$enable, account=$account, password=$password)"
    }
}