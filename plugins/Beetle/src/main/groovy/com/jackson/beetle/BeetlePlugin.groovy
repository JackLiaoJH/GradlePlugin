package com.jackson.beetle

import com.jackson.beetle.core.Constants
import com.jackson.beetle.ext.AppExt
import com.jackson.beetle.ext.BeetleExt
import com.jackson.beetle.ext.LibraryExt
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 *
 * desc:  项目插件配置
 * author: 行走的老者
 * date: 2020-01-09 16:34
 */
class BeetlePlugin implements Plugin<Project> {


    @Override
    void apply(Project project) {
        BeetleExt beetleExt = new BeetleExt(project)
        project.extensions.add(Constants.BEETLE_EXTENSION_NAME, beetleExt)
        configApp(project)
    }

    void configApp(Project project) {
        List<String> moduleList = new ArrayList<>()
        NamedDomainObjectContainer<AppExt> appList
        BeetleExt beetleExt
        project.afterEvaluate {
            beetleExt = project.extensions.getByName(Constants.BEETLE_EXTENSION_NAME) as BeetleExt
            appList = beetleExt.apps
            checkRepeat(beetleExt)
            checkModules(beetleExt, moduleList)

        }
        initChildModules(moduleList, project)
        println("project child modules: $moduleList")
    }

    void initChildModules(List<String> moduleList, Project project) {
        if (project.childProjects.isEmpty()) {
            moduleList.add(project.toString()
                    .replace("project ", "")
                    .replace('\'', ''))
            return
        }
        project.childProjects.entrySet().forEach {
            initChildModules(moduleList, it.value)
        }

    }
    /**apps 与 modules模块配置是否重复检查*/
    static void checkRepeat(BeetleExt beetleExt) {
        Map<String, List<AppExt>> appGroupMap =
                beetleExt.apps.groupBy {
                    it.name.startsWith(':') ? it.name : new String(":" + it.name)
                }
        appGroupMap.forEach { k, v ->
            if (v.size() > 1) {
                throw new IllegalArgumentException("app=$k is repeat, please check !")
            }
        }

        Map<String, List<LibraryExt>> moduleGroupMap =
                beetleExt.modules.groupBy {
                    it.name.startsWith(':') ? it.name : new String(":" + it.name)
                }
        moduleGroupMap.forEach { k, v ->
            if (v.size() > 1) {
                throw new IllegalArgumentException("module=$k is repeat, please check !")
            }
        }
    }

    static void checkModules(BeetleExt beetleExt, List<String> projectModules) {
        Set<String> configSet = new HashSet<>()
        Set<String> modulesSet = new HashSet<>()
        if (projectModules != null) {
            modulesSet.addAll(projectModules)
        }
        List<String> notFoundList = new ArrayList<>()

        List<String> appNameList = beetleExt.apps
                .stream()
                .map { it.name.startsWith(':') ? it.name : new String(":" + it.name) }
                .collect()

        List<String> moduleNameList = beetleExt.modules.
                        stream()
                        .map {
                            String name = it.name.startsWith(':') ? it.name : new String(":" + it.name)
                            if (appNameList.contains(name)) {
                                throw new IllegalArgumentException("$it.name already configured " +
                                        "as an application, please check appConfig")
                            }
                            name
                        }.collect()

        println "moduleNameList = $moduleNameList"

        configSet.addAll(appNameList)
        configSet.addAll(moduleNameList)

        configSet.forEach {
            if (!modulesSet.contains(it)) {
                notFoundList.add(it)
            }
        }
        if (notFoundList.size() > 0) {
            throw new IllegalArgumentException(
                    "not fount modules = " + notFoundList
            )
        }

        beetleExt.apps.stream().forEach { app ->
            app.modules.stream().forEach {
                if (!configSet.contains(it)) {
                    throw new IllegalArgumentException(
                            "appConfig error , can not find $app.name modules $it by project")
                }
            }
        }

        println("modules: " + configSet)
    }
}
