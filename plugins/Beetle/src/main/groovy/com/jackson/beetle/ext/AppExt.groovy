package com.jackson.beetle.ext

import com.jackson.beetle.core.Constants

/**
 *
 * desc:  App 配置
 * author: 行走的老者
 * date: 2020-01-09 16:48
 */
class AppExt extends BaseExt {
    String dependMethod = Constants.DEPENDS_IMPLEMENTATION
    List<String> modules = new ArrayList<>()
    int versionCode
    String versionName

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

    def versionCode(int versionCode) {
        this.versionCode = versionCode
    }

    def versionName(String versionName) {
        this.versionName = versionName
    }

    @Override
    String toString() {
        return "app = $name, applicationId = $applicationId, " +
                "${applicationName.isEmpty() ? "" : "application = $applicationName,"}" +
                ", versionCode=" + versionCode +
                ", versionName='" + versionName +
                " dependMethod = $dependMethod\n" +
                "modules: ${modules.isEmpty() ? "is empty" : "$modules"}"
    }
}
