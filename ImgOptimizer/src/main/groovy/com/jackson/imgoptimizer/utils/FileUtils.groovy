package com.jackson.imgoptimizer.utils

import org.apache.tools.ant.taskdefs.condition.Os
import org.gradle.api.Project

/**
 *
 * desc: 文件操作工具类
 * author: 行走的老者
 * date: 2020-01-09 09:58
 */
class FileUtils {
    /**
     * 获取.../build/targetDir 目录
     * @param project
     * @param targetDir 目标目录
     * @return path
     */
    static def getBuildTargetDirPath(Project project, String targetDir) {
        return project.buildDir.absolutePath + File.separator + targetDir
    }

    /**
     * 获取.../build/targetDir 目录
     * @param project
     * @param targetDir 目标目录
     * @return file
     */
    static def getBuildTargetDir(Project project, String targetDir) {
        return new File(getBuildTargetDirPath(project, targetDir))
    }

    /**
     * 获取文件路径
     * @param fileDir 文件目录
     * @param fileName 文件名
     * @return path
     */
    static def getFilePath(String fileDir, String fileName) {
        return fileDir + File.separator + fileName
    }

    static def getOsSuffix() {
        if (Os.isFamily(Os.FAMILY_WINDOWS)) {
            return ".exe"
        } else if (Os.isFamily(Os.FAMILY_MAC)) {
            return "-mac"
        } else {
            return ""
        }
    }
    /**
     * 根据系统添加文件后缀
     * @param fileName
     * @return path
     */
    static def appendOsSuffix(String fileName) {
        if (Os.isFamily(Os.FAMILY_WINDOWS)) {
            return "${fileName}.exe"
        } else if (Os.isFamily(Os.FAMILY_MAC)) {
            return "${fileName}-mac"
        } else {
            return "${fileName}"
        }
    }


    //===================copy=============================>

    static def copyFile(String srcFilePath, String descFilePath) {

    }
    /**
     * 复制Resource 下的srcFile文件到descFilePath
     * @param srcFile Resource下的文件
     * @param descFilePath 目标文件
     * @param isCover true: 覆盖,false:不覆盖
     * @return 是否成功
     */
    static def copyResourceFile(String srcFile, String descDirPath, String descFileName, boolean isCover) {
        def descFile = new File(descDirPath)
        if (!descFile.exists() || isCover) {
            def mkdirsSuccess = descFile.mkdirs()
            if (!mkdirsSuccess) {
                println("Create dir=${descDirPath} fail !!!")
                return false
            }
            def descFilePath = new File(descFile, descFileName)
            new FileOutputStream(descFilePath).withStream {
                def is = FileUtils.class.getResourceAsStream(srcFile)
                it.write(is.getBytes())
            }
            descFilePath.setExecutable(true, false)
            println("Copy ${srcFile} to ${descFilePath.absolutePath} success !!!")
            return true
        }
        println("${descDirPath + descFileName} is exist !!!")
        return true
    }
}
