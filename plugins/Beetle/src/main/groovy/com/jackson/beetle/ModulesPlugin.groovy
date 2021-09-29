package com.jackson.beetle

import com.android.build.gradle.AppPlugin
import com.android.build.gradle.LibraryPlugin
import com.jackson.beetle.core.Constants
import com.jackson.beetle.core.impl.AppManifest
import com.jackson.beetle.core.impl.LibraryManifest
import com.jackson.beetle.ext.AppExt
import com.jackson.beetle.ext.BeetleExt
import com.jackson.beetle.ext.ModuleExt
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.UnknownDomainObjectException

import java.util.stream.Collectors

/**
 *
 * desc: module 配置插件
 * author: 行走的老者
 * date: 2020-01-09 16:38
 */
class ModulesPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        BeetleExt beetleExt = getBeetleConfigExtension(project)
        configModules(project, beetleExt)
    }

    static void configModules(Project project, BeetleExt beetleExt) {
        if (beetleExt == null) {
            throw new NullPointerException(
                    "can not find ${Constants.BEETLE_EXTENSION_NAME} config in project build.gradle")
        }
        List<AppExt> appExtList = beetleExt.apps.stream()
                .filter {
                    (it.name.startsWith(':') ? it.name : new String(":" + it.name)).endsWith(project.name)
                }
                .collect()
        println("appExtList>>>>> $appExtList")
        if (appExtList != null && appExtList.size() > 0) {
            AppExt appExt = appExtList.get(0)
            AppPlugin appPlugin = project.plugins.apply(AppPlugin)
            setDefaultConfig(appPlugin, appExt)
            new AppManifest(project).resetManifest(appExt, appPlugin, beetleExt.isDebugEnable())
            dependModules(project, appExt, beetleExt)
        } else {
            modulesRunAlone(project, beetleExt.modules, beetleExt.debugEnable)
        }
    }

    private static setDefaultConfig(AppPlugin appPlugin, AppExt appExt) {
        appPlugin.extension.defaultConfig.setApplicationId(appExt.applicationId)
        appPlugin.extension.defaultConfig.setVersionCode(
                appExt.versionCode > 0 ? appExt.versionCode : appPlugin.extension.defaultConfig.versionCode)
        appPlugin.extension.defaultConfig.setVersionName(
                ((appExt.versionName != null && !appExt.versionName.isEmpty()) ? appExt.versionName :
                        appPlugin.extension.defaultConfig.versionName)
        )
    }

    static void dependModules(Project project, AppExt appExt, BeetleExt beetleExt) {
        Map<String, ModuleExt> moduleExtMap = beetleExt.modules.stream()
                .filter { modules ->
                    String modulesName = appExt.modules.stream().find { it.contains(modules.name) }
                    modulesName != null && !modulesName.isEmpty()
                }
                .collect(Collectors.toMap({ it.name }, { it -> it }))

        if (appExt.modules != null && appExt.modules.size() > 0) {
            List<String> modulesList = appExt.modules.stream()
                    .filter {
                        beetleExt.debugEnable ? (moduleExtMap != null && !moduleExtMap[it].isRunAlone) : true
                    }
                    .map {
                        project.dependencies.add(appExt.dependMethod, project.project(it))
                        it
                    }.collect()
            println("build app: [$appExt.name] , depend modules: $modulesList")
        }
    }

    BeetleExt getBeetleConfigExtension(Project project) {
        try {
            return project.parent.extensions.getByName(Constants.BEETLE_EXTENSION_NAME) as BeetleExt
        } catch (UnknownDomainObjectException ignored) {
            if (project.parent != null) {
                getBeetleConfigExtension(project.parent)
            } else {
                throw new UnknownDomainObjectException(ignored as String)
            }
        }
    }

    private static void modulesRunAlone(Project project, NamedDomainObjectContainer<ModuleExt> modules, boolean isDebug) {
        List<ModuleExt> filterList = modules.stream()
                .filter { it.name.endsWith(project.name) }
                .collect()

        if (filterList != null && filterList.size() > 0) {
            ModuleExt moduleExt = filterList.get(0)

            if (isDebug && moduleExt.isRunAlone) {
                AppPlugin appPlugin = project.plugins.apply(AppPlugin)
                appPlugin.extension.defaultConfig.setApplicationId(moduleExt.applicationId)
                if (moduleExt.dependence != null && !moduleExt.dependence.isEmpty()) {
                    // 依赖dependence 库执行
                    project.dependencies.add(Constants.DEPENDS_IMPLEMENTATION, project.project(moduleExt.dependence))
                    println("build run alone modules: [$moduleExt.name], runSuper = $moduleExt.dependence")
                } else {
                    println("build run alone modules: [$moduleExt.name]")
                }
                if (moduleExt.mainActivity != null && !moduleExt.mainActivity.isEmpty()) {
                    new AppManifest(project).resetManifest(moduleExt, appPlugin, isDebug)
                }
            } else {
                LibraryPlugin libraryPlugin = project.plugins.apply(LibraryPlugin)
                new LibraryManifest(project).resetManifest(moduleExt, libraryPlugin, isDebug)
            }
        }
    }
}
