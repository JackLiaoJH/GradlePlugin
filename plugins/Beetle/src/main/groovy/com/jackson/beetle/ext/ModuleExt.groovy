package com.jackson.beetle.ext

/**
 *
 * desc:   库配置
 * author: 行走的老者
 * date: 2020-01-09 16:47
 */
class ModuleExt extends BaseExt {

    boolean isRunAlone = false
    String dependence

    ModuleExt(String name) {
        super(name)
    }

    def name(String name) {
        this.name = name
    }


    def isRunAlone(boolean isRunAlone) {
        this.isRunAlone = isRunAlone
    }


    def applicationId(String applicationId) {
        this.applicationId = applicationId
    }

    def applicationName(String applicationName) {
        this.applicationName = applicationName
    }

    def dependence(String dependence) {
        this.dependence = dependence
    }

    def mainActivity(String mainActivity) {
        this.mainActivity = mainActivity
    }

    @Override
    String toString() {
        return "name = $name, isRunAlone = $isRunAlone, applicationId = $applicationId, " +
                "dependence = $dependence, mainActivity = $mainActivity"
    }
}
