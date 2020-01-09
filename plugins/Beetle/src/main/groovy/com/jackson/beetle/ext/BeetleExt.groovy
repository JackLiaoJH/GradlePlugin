package com.jackson.beetle.ext

import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Project

/**
 *
 * desc:   项目配置
 * author: 行走的老者
 * date: 2020-01-09 16:49
 */
class BeetleExt {
    boolean debugEnable = false
    NamedDomainObjectContainer<AppExt> apps
    NamedDomainObjectContainer<LibraryExt> modules

    BeetleExt(Project project) {
        apps = project.container(AppExt)
        modules = project.container(LibraryExt)
    }

    def debugEnable(boolean debugEnable) {
        this.debugEnable = debugEnable
    }

    def apps(Closure closure) {
        apps.configure(closure)
    }


    def modules(Closure closure) {
        modules.configure(closure)
    }

    @Override
    String toString() {
        return "isDebug: $debugEnable\n" +
                "apps: ${apps.isEmpty() ? "is empty" : "$apps"}" +
                "modules: ${modules.isEmpty() ? "is empty" : "$modules"}"
    }
}
