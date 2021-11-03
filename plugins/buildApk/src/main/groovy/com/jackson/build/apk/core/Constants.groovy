package com.jackson.build.apk.core

/**
 * @author: jackson liao
 * @createDate: 2021/11/2 11:36
 * @description: 常量
 */
interface Constants {
    String BUILD_APK_NAME = "buildApk"


    //360加固命令行文档： https://jiagu.360.cn/#/global/help/164
    String LOGIN_CMD = "java -jar %1s -login %2s %3s"
    String IMPORT_KEY_CMD = "java -jar %1s -importsign %2s %3s %4s %5s"
    String CHECK_SIGN_INFO_CMD = "java -jar %1s -showsign"
    String INIT_JIAGU_SERVICE_CMD = "java -jar %1s -config"
}