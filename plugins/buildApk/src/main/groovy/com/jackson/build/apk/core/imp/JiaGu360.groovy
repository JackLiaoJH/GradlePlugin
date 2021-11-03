package com.jackson.build.apk.core.imp

import com.jackson.build.apk.core.Constants
import com.jackson.build.apk.core.JiaGu
import com.jackson.build.apk.vo.BuildApkVo
import com.jackson.build.apk.vo.SignApkVo
import org.apache.http.util.TextUtils
import com.jackson.build.apk.utils.BuildApkUtils

/**
 * @author: jackson liao
 * @createDate: 2021/11/3 10:51
 * @description: 360 加固
 */
class JiaGu360 extends JiaGu {

    JiaGu360(SignApkVo signApkVo) {
        super(signApkVo)
    }

    @Override
    void startJiaGu(File apkFile, BuildApkVo buildApkVo) {
        def build360Vo = buildApkVo.build360
        if (!build360Vo.enable) return
        String jarPath = build360Vo.jarPath
        if (TextUtils.isEmpty(jarPath)) {
            throw IllegalArgumentException("You must config build360 jarPath in build.gradle first !")
        }
        if (TextUtils.isEmpty(build360Vo.account)) {
            throw IllegalArgumentException("You must config build360 account in build.gradle first !")
        }
        if (TextUtils.isEmpty(build360Vo.password)) {
            throw IllegalArgumentException("You must config build360 password in build.gradle first !")
        }

        if (!signApkVo.checkSign()) {
            throw IllegalArgumentException("You must config sign in build.gradle first !")
        }

        String loginCmd = String.format(
                Constants.LOGIN_CMD, jarPath, build360Vo.account, build360Vo.password
        )
        // 登录360
        BuildApkUtils.executeJiaGuCMD(loginCmd)

        String importKeyCmd = String.format(
                Constants.IMPORT_KEY_CMD, jarPath, signApkVo.signPath,
                signApkVo.signPassword, signApkVo.signKeyAlias, signApkVo.signKeyPwd
        )
        // 导入签名
        BuildApkUtils.executeJiaGuCMD(importKeyCmd)

        String checkSignInfoCmd = String.format(Constants.CHECK_SIGN_INFO_CMD, jarPath)
        // 查看加固签名信息
        BuildApkUtils.executeJiaGuCMD(checkSignInfoCmd)

        String initJiaGuServiceCmd = String.format(Constants.INIT_JIAGU_SERVICE_CMD, jarPath)
        // 初始化加固服务配置
        BuildApkUtils.executeJiaGuCMD(initJiaGuServiceCmd)

        // 执行加固，然后自动签名
        String jiaGuCmd =
                "java -jar $jarPath -jiagu ${apkFile.path} ${apkFile.parentFile.path} -autosign -automulpkg"
        BuildApkUtils.executeJiaGuCMD(jiaGuCmd)
    }
}