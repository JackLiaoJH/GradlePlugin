package com.jackson.build.apk.utils;

/**
 * @author: jackson liao
 * @createDate: 2021/11/3 14:56
 * @description: 工具类
 */
class BuildApkUtils {

    /**
     * 根据后缀查找匹配的apk文件
     * @param path 路径
     * @param suffix 后缀
     * @return
     */
    static File findApkFile(path, suffix) {
        def dir = new File(path)
        return dir.listFiles().find {
            it.isFile() && it =~ /.*${suffix}\.apk/
        }
    }

    /**
     * 执行加固命令
     * @param cmd 命令
     */
    static void executeJiaGuCMD(String cmd) {
        println(cmd)
        def process = cmd.execute()
        println(process.text)
        process.waitFor()// 用以等待外部进程调用结束
        println(process.exitValue())
    }
}
