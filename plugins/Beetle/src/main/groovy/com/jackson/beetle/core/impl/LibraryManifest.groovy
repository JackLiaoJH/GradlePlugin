package com.jackson.beetle.core.impl

import com.jackson.beetle.core.Manifest
import com.jackson.beetle.ext.BaseExt
import org.gradle.api.Project

/**
 *
 * desc:   库 Manifest配置
 * author: 行走的老者
 * date: 2020-01-09 17:36
 */
class LibraryManifest extends Manifest {
    LibraryManifest(Project project) {
        super(project)
    }

    @Override
    void setApplication(Object application, BaseExt baseExt) {
        if (!baseExt.isRunAlone || baseExt.applicationName == null || baseExt.applicationName.isEmpty()) {
            application.each {
                it.attributes().remove("android:name")
            }
        }
    }

    @Override
    void setMainIntentFilter(Object activity, boolean isFindMain) {
        if (isFindMain) {
            println "build lib"
            activity.'intent-filter'.each {
                if (it.action.@'android:name' == "android.intent.action.MAIN") {
                    it.replaceNode {}
                }
            }
        }
    }
}
