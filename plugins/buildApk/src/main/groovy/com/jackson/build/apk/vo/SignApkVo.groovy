package com.jackson.build.apk.vo

import org.apache.http.util.TextUtils

/**
 * @author: jackson liao
 * @createDate: 2021/11/3 10:41
 * @description: apk签名信息
 */
class SignApkVo {
    /**签名pwd*/
    String signPassword = ""

    /**签名别名key*/
    String signKeyAlias = ""

    /**签名别名密码*/
    String signKeyPwd = ""

    /**签名文件路径*/
    String signPath = ""


    Boolean checkSign() {
        if (TextUtils.isEmpty(signPassword)) return false
        if (TextUtils.isEmpty(signKeyAlias)) return false
        if (TextUtils.isEmpty(signKeyPwd)) return false
        if (TextUtils.isEmpty(signPath)) return false
        return true
    }


    @Override
    public String toString() {
        return "SignApkVo{" +
                "signPassword='" + signPassword + '\'' +
                ", signKeyAlias='" + signKeyAlias + '\'' +
                ", signKeyPwd='" + signKeyPwd + '\'' +
                ", signPath='" + signPath + '\'' +
                '}';
    }
}