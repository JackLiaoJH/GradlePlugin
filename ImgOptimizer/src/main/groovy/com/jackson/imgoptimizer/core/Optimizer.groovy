package com.jackson.imgoptimizer.core

import com.jackson.imgoptimizer.utils.FileUtils
import com.jackson.imgoptimizer.utils.Logger
import com.jackson.imgoptimizer.utils.OptimizerLevel
import org.gradle.api.Project

/**
 *
 * desc: 压缩接口
 * author: 行走的老者
 * date: 2020-01-09 10:08
 */
abstract class Optimizer {
    protected OptimizerLevel mOptimizerLevel

    Optimizer(OptimizerLevel optimizerLevel) {
        this.mOptimizerLevel = optimizerLevel
    }

    /**
     * 压缩
     * @param project
     * @param log 日志对象
     * @param suffix 图片后缀
     * @param files 图片文件列表
     */
    abstract void optimize(Project project, Logger log, String suffix, List<File> files)

    /**
     * 复制对应压缩命令到build文件夹下
     * @param project
     */
    protected void copyCmd2BuildFolder(Project project) {
        def name = mOptimizerLevel.name
        def srcFile = "/$name/${FileUtils.appendOsSuffix(name)}"
        def descDir = FileUtils.getBuildTargetDirPath(project, name)
        FileUtils.copyResourceFile(srcFile, descDir, FileUtils.appendOsSuffix(name), false)
    }
    /**
     * 获取完整的压缩命令路径 ,.../build/${optimizerLevel.name}/${optimizerLevel.name}(.exe)
     * @param project
     * @return
     */
    protected def getCmdPath(Project project) {
        def name = mOptimizerLevel.name
        return FileUtils.getFilePath(FileUtils.getBuildTargetDirPath(project, name),
                FileUtils.appendOsSuffix(name))
    }
}