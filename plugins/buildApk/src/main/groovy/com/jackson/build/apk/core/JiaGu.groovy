package com.jackson.build.apk.core

import com.jackson.build.apk.vo.BuildApkVo
import com.jackson.build.apk.vo.SignApkVo

/**
 * @author: jackson liao
 * @createDate: 2021/11/3 10:48
 * @description: 加固接口
 */
abstract class JiaGu {

    protected SignApkVo signApkVo

    JiaGu(SignApkVo signApkVo) {
        this.signApkVo = signApkVo
    }
    /**
     * 开始加固
     * @param apkFile
     * @param buildApkVo
     */
    abstract void startJiaGu(File apkFile, BuildApkVo buildApkVo)
}