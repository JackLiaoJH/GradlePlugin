package com.jackson.imgoptimizer

import com.android.build.gradle.AppPlugin
import com.android.build.gradle.LibraryPlugin
import com.android.build.gradle.api.BaseVariant
import com.jackson.imgoptimizer.core.Constants
import com.jackson.imgoptimizer.ext.ImgOptimizerExtension
import org.gradle.api.DomainObjectCollection
import org.gradle.api.Plugin
import org.gradle.api.Project
import com.jackson.imgoptimizer.core.task.ImgOptimizerTask

class ImgOptimizerPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        if (project.plugins.withType(AppPlugin)) {
            applyAndroidPlugin(project, (DomainObjectCollection<BaseVariant>) project.android.applicationVariants)
        } else if (project.plugins.withType(LibraryPlugin)) {
            applyAndroidPlugin(project, (DomainObjectCollection<BaseVariant>) project.android.libraryVariants)
        } else {
            throw new IllegalArgumentException('img-optimizer gradle plugin only works in with Android module.')
        }
    }

    static void applyAndroidPlugin(Project project, DomainObjectCollection<BaseVariant> variants) {
        def ext = project.extensions.create(Constants.IMG_OPTIMIZER_EXT_NAME, ImgOptimizerExtension)
        variants.all { variant ->
            // variant: debug ,release
//            println("=======variant:${variant.name}=============")
            List<File> imgDirList = []
            variant.sourceSets.each { sourceSet ->
                // main
//                println("=======sourceSet:${sourceSet.name}=============")
                sourceSet.resDirectories.each { resDir ->
//                    println("=======resDir:${resDir.name}=============>")
                    if (resDir.exists()) {
                        resDir.eachDir {
                            if (it.directory && (it.name.startsWith("drawable") || it.name.startsWith("mipmap"))) {
//                                println("$it.absolutePath")
                                imgDirList << it
                            }
                        }
                    }
                }
            }

            if (!imgDirList.empty) {
                project.task(type: ImgOptimizerTask, overwrite: true, Constants.IMG_OPTIMIZER_TASK_NAME.
                        concat(project.name.capitalize()).concat(variant.buildType.name.capitalize())) {
                    it.group = "optimize"
                    it.description = "Optimize ${variant.buildType.name} images"
                    it.imgDirs = imgDirList
                    it.triggerSize = ext.triggerSize
                    it.suffix = ext.suffix
                    it.level = ext.level
                }
            }
        }
    }
}