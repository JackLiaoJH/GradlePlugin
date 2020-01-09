package com.jackson.beetle

import com.android.build.gradle.AppPlugin
import com.android.build.gradle.LibraryPlugin
import com.jackson.beetle.core.Constants
import com.jackson.beetle.core.impl.AppManifest
import com.jackson.beetle.core.impl.LibraryManifest
import com.jackson.beetle.ext.AppExt
import com.jackson.beetle.ext.BeetleExt
import com.jackson.beetle.ext.LibraryExt
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
            throw new NullPointerException("can not find ${Constants.BEETLE_EXTENSION_NAME}")
        }
        List<AppExt> filterList = beetleExt.apps.stream()
                .filter {
                    (it.name.startsWith(':') ? it.name : new String(":" + it.name)).endsWith(project.name)
                }
                .skip(0).collect()

        if (filterList != null && filterList.size() > 0) {
            AppExt appExt = filterList.get(0)
            AppPlugin appPlugin = project.plugins.apply(AppPlugin)
            appPlugin.extension.defaultConfig.setApplicationId(appExt.applicationId)
            AppManifest appManifest = new AppManifest(project)
            appManifest.resetManifest(appExt, appPlugin, beetleExt.isDebugEnable())
            dependModules(project, appExt, beetleExt)
        } else {
            modulesRunAlone(project, beetleExt.modules, beetleExt.debugEnable)
        }

    }

    static void dependModules(Project project, AppExt appExt, BeetleExt beetleExt) {
        Map<String, LibraryExt> moduleExtMap = beetleExt.modules.stream().filter {
            modules ->
                String modulesName = appExt.modules.stream().find { it.contains(modules.name) }
                modulesName != null && !modulesName.isEmpty()
        }.collect(Collectors.toMap({ it.name }, { it -> it }))

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

    private static void modulesRunAlone(Project project, NamedDomainObjectContainer<LibraryExt> modules, boolean isDebug) {
        List<LibraryExt> filterList = modules.stream().filter {
            it.name.endsWith(project.name)
        }.skip(0).collect()
        if (filterList != null && filterList.size() > 0) {
            LibraryExt moduleExt = filterList.get(0)

            if (isDebug && moduleExt.isRunAlone) {
                AppPlugin appPlugin = project.plugins.apply(AppPlugin)
                appPlugin.extension.defaultConfig.setApplicationId(moduleExt.applicationId)
                if (moduleExt.runAloneSuper != null && !moduleExt.runAloneSuper.isEmpty()) {
                    project.dependencies.add("implementation", project.project(moduleExt.runAloneSuper))
                    println("build run alone modules: [$moduleExt.name], runSuper = $moduleExt.runAloneSuper")
                } else {
                    println("build run alone modules: [$moduleExt.name]")
                }
                if (moduleExt.mainActivity != null && !moduleExt.mainActivity.isEmpty()) {
                    AppManifest appManifest = new AppManifest(project)
                    appManifest.resetManifest(moduleExt, appPlugin, isDebug)
                }
            } else {
                LibraryPlugin libraryPlugin = project.plugins.apply(LibraryPlugin)
                new LibraryManifest(project).resetManifest(moduleExt, libraryPlugin, isDebug)
            }
        }

    }
}
