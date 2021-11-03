package com.jackson.build.apk.core

import com.android.build.gradle.AppExtension
import com.android.utils.FileUtils
import com.jackson.build.apk.core.imp.JiaGu360
import com.jackson.build.apk.utils.BuildApkUtils
import com.jackson.build.apk.vo.BuildApkVo
import com.jackson.build.apk.vo.SignApkVo
import org.apache.http.util.TextUtils
import org.gradle.api.Project
import org.gradle.api.Task

/**
 * @author: jackson liao
 * @createDate: 2021/11/3 10:48
 * @description: 构建打包任务
 */
class BuildApkTasks {
    private String groupTask = "publishApks"
    private Project project
    private BuildApkVo buildApkVo
    private SignApkVo releaseSign

    private AppExtension appExtension

    private def assembleCleanApkDirRelease
    private def assemble360JiaGuRelease
    private def assembleWalleChannelsRelease
    private def assembleRenameApkFileRelease

    BuildApkTasks(Project project, String rootPath, BuildApkVo buildApkVo, SignApkVo releaseSign) {
        this.project = project
        this.buildApkVo = buildApkVo
        this.releaseSign = releaseSign

        appExtension = project.getExtensions().getByType(AppExtension)

        assembleCleanApkDirRelease(rootPath)
        assemble360JiaGuRelease(rootPath)
        assembleWalleChannelsRelease(rootPath)
        assembleRenameApkFileRelease(rootPath)
    }

    void dependsTasks() {
        def assembleRelease = project.tasks.getByName("assembleRelease") { Task task ->
            task.dependsOn(assembleCleanApkDirRelease)
        }

        if (buildApkVo == null || !buildApkVo.enable) {
            project.tasks.getByName("assembleJacksonRelease") { Task task ->
                task.dependsOn(assembleRelease)
            }
            return
        }

        def build360 = buildApkVo.build360
        def buildChannel = buildApkVo.buildChannel

        if ((build360 == null || !build360.enable) && (buildChannel == null || !buildChannel.enable)) {
            project.tasks.getByName("assembleJacksonRelease") { Task task ->
                task.dependsOn(assembleRelease)
            }
            return
        }

        if (build360 == null || !build360.enable) {
            project.tasks.getByName("assembleWalleChannelsRelease") { Task task ->
                task.dependsOn(assembleRelease)
            }
        } else  {
            project.tasks.getByName("assemble360JiaGuRelease") { Task task ->
                task.dependsOn(assembleRelease)
            }
            project.tasks.getByName("assembleWalleChannelsRelease") { Task task ->
                task.dependsOn(assemble360JiaGuRelease)
            }
        }


        project.tasks.getByName("assembleWalleChannelsRelease") { Task task ->
            task.dependsOn(assembleRelease)
        }

        project.tasks.getByName("assembleJacksonRelease") { Task task ->
            if (build360.enable && buildChannel.enable) {
                task.dependsOn(assembleRelease, assemble360JiaGuRelease, assembleWalleChannelsRelease)
            } else if (build360.enable && !buildChannel.enable) {
                task.dependsOn(assembleRelease, assemble360JiaGuRelease)
            } else if (!build360.enable && buildChannel.enable) {
                task.dependsOn(assembleRelease, assembleWalleChannelsRelease)
            } else {
                task.dependsOn(assembleRelease)
            }
        }
    }

    private String getChannelOutputApkPath(String rootPath, boolean enableJiaGu) {
        String suffix = enableJiaGu ? "_sign" : ""
        def targetApk = BuildApkUtils.findApkFile(project.file(rootPath).getParentFile().path, suffix)
        def channelOutputDir = buildApkVo.getChannelOutputPath()
        if (channelOutputDir == null || TextUtils.isEmpty(channelOutputDir))
            channelOutputDir = "${targetApk.getParentFile().path}\\channels"
        return channelOutputDir
    }

    private void delete(Object o) {
        println("================开始执行删除apk输出目录任务${o}====================")
        project.delete(o)
        println("================删除apk输出目录任务完成${o}========================")
    }


    private void assembleCleanApkDirRelease(String rootPath) {
        /**删除apk目录任务*/
        assembleCleanApkDirRelease = project.task("assembleCleanApkDirRelease") { Task task ->
            task.setGroup(groupTask)
            task.doLast {
                def apkRootPath = project.file(rootPath).getParentFile().path
                delete(apkRootPath)
                if (buildApkVo.buildChannel != null && !TextUtils.isEmpty(buildApkVo.buildChannel.outPutPath))
                    delete(buildApkVo.buildChannel.outPutPath)
            }
        }
    }

    private void assemble360JiaGuRelease(String rootPath) {
        /**开始加固*/
        assemble360JiaGuRelease = project.task("assemble360JiaGuRelease") { Task task ->
            task.setGroup(groupTask)
            task.doLast {
                println("================开始执行加固任务===============================")
                new JiaGu360(releaseSign).startJiaGu(new File(rootPath), buildApkVo)
                // 360自动加固，后缀会有_sign
                def jiaGuApk = BuildApkUtils.findApkFile(project.file(rootPath).getParentFile().path, "_sign")
                println("================加固任务完成${jiaGuApk}========================")
            }
        }
    }

    private void assembleWalleChannelsRelease(String rootPath) {
        /**walle多渠道打包*/
        assembleWalleChannelsRelease = project.task("assembleWalleChannelsRelease") { Task task ->
            task.setGroup(groupTask)
            task.doLast {
                def channelVo = buildApkVo.buildChannel
                def build360 = buildApkVo.build360
                println("================开始walle多渠道打包任务===============================")
                String suffix = build360.enable ? "_sign" : ""
                def targetApk = BuildApkUtils.findApkFile(project.file(rootPath).getParentFile().path, suffix)
                println("================src apk:${targetApk}===============================")
                if (targetApk != null) {
                    def channelOutputDir = getChannelOutputApkPath(rootPath, build360.enable)
                    def walleChannelCmd = "java -jar ${channelVo.jarPath} batch -f ${channelVo.channelPath} ${targetApk.path} $channelOutputDir"
                    BuildApkUtils.executeJiaGuCMD(walleChannelCmd)
                    println("================walle多渠道打包任务完成====================")
                } else {
                    println("================walle多渠道打包任务失败====================")
                }
            }
        }
    }

    private void assembleRenameApkFileRelease(String rootPath) {
        /**根据规则重命名apk文件名*/
        assembleRenameApkFileRelease = project.task("assembleJacksonRelease") { Task task ->
            task.setGroup(groupTask)
            task.doLast {
                def build360 = buildApkVo.build360
                def channelOutputDir = getChannelOutputApkPath(rootPath, build360.enable)
                def channelOutputFile = project.file(channelOutputDir);
                println(">>>>>>>>>>> ${channelOutputFile}")
                if (channelOutputFile.exists() && channelOutputFile.isDirectory()) {
                    String versionName = appExtension.defaultConfig.versionName
                    String srcApkFileName;
                    String descApkFileName;
                    String channel;
                    channelOutputFile.listFiles().each {
                        descApkFileName = "app-"
                        srcApkFileName = it.name
                        channel = srcApkFileName.substring(srcApkFileName.lastIndexOf("_") + 1, srcApkFileName.lastIndexOf(".apk"))
                        descApkFileName += channel
                        descApkFileName += "-v${versionName}.apk"

                        println(">>>>>>>>>>>${srcApkFileName} , ${channel} , ${descApkFileName}")
                        FileUtils.renameTo(it, new File(it.parentFile, descApkFileName))
                    }
                }
                println("================渠道包位置：${channelOutputDir}====================")
            }
        }
    }
}