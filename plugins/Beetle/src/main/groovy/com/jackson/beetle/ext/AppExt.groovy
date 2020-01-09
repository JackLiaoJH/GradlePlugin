package com.jackson.beetle.ext

/**
 *
 * desc:  App 配置
 * author: 行走的老者
 * date: 2020-01-09 16:48
 */
class AppExt extends BaseExt {
    String dependMethod = "implementation"
    List<String> modules = new ArrayList<>()

    AppExt(String name) {
        super(name)
    }

    def name(String name) {
        this.name = name
    }

    def applicationId(String applicationId) {
        this.applicationId = applicationId
    }

    def applicationName(String applicationName) {
        this.applicationName = applicationName
    }

    def dependMethod(String dependMethod) {
        this.dependMethod = dependMethod
    }

    def mainActivity(String mainActivity) {
        this.mainActivity = mainActivity
    }

    def modules(String... modules) {
        this.modules.addAll(modules)
    }

    @Override
    String toString() {
        return "app = $name, applicationId = $applicationId, " +
                "${applicationName.isEmpty() ? "" : "application = $applicationName,"}" +
                " dependMethod = $dependMethod\n" +
                "modules: ${modules.isEmpty() ? "is empty" : "$modules"}"
    }
}
