package com.jackson.beetle.core.impl

import com.jackson.beetle.core.Manifest
import com.jackson.beetle.ext.BaseExt
import org.gradle.api.Project

/**
 *
 * desc:   App Manifest配置
 * author: 行走的老者
 * date: 2020-01-09 17:34
 */
class AppManifest extends Manifest {
    AppManifest(Project project) {
        super(project)
    }

    @Override
    void setApplication(Object application, BaseExt baseExt) {
        if (baseExt.applicationName == null || baseExt.applicationName.isEmpty()) {
            application.each { it.attributes().remove("android:name") }
            return
        }
        if (application.@'android:name' == null ||
                application.@'android:name' != baseExt.applicationName) {
            application.@'android:name' = baseExt.applicationName
        }
    }

    @Override
    void setMainIntentFilter(Object activity, boolean isFindMain) {
        if (!isFindMain) {
            activity.appendNode {
                'intent-filter' {
                    action('android:name': "android.intent.action.MAIN")
                    category('android:name': "android.intent.category.LAUNCHER")
                }
            }
        } else {
            def filter = activity.'intent-filter'.find {
                it.category.@'android:name' == "android.intent.category.LAUNCHER"
            }
            if (filter == null || filter.size() == 0) {
                activity.'intent-filter'.appendNode {
                    category('android:name': "android.intent.category.LAUNCHER")
                }
            }
        }
    }
}
