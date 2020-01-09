package com.jackson.beetle.core

import com.android.build.gradle.AppPlugin
import com.android.build.gradle.BasePlugin
import com.jackson.beetle.ext.BaseExt
import com.jackson.beetle.ext.LibraryExt
import groovy.util.slurpersupport.GPathResult
import groovy.xml.StreamingMarkupBuilder
import org.gradle.api.Project
import groovy.xml.XmlUtil

/**
 *
 * desc: Manifest 根据配置生成清单文件
 * author: 行走的老者
 * date: 2020-01-09 17:30
 */
abstract class Manifest {
    protected String path
    protected String outputGroupPath
    protected String outputPath
    protected GPathResult manifest
    private Project project

    Manifest(Project project) {
        this.project = project
        path = "${project.getBuildFile().getParent()}/src/main/AndroidManifest.xml"
        outputGroupPath = "${project.getBuildFile().getParent()}/beetle"
        outputPath = "${project.getBuildFile().getParent()}/beetle/AndroidManifest.xml"
        File manifestFile = new File(path)
        if (!manifestFile.getParentFile().exists() && !manifestFile.getParentFile().mkdirs()) {
            println "Unable to find AndroidManifest and create fail, please manually create"
        }
        manifest = new XmlSlurper(false, false).parse(manifestFile)
    }

    void resetManifest(BaseExt moduleExt, BasePlugin appPlugin, boolean isDebug) {
        setApplication(manifest.application, moduleExt)
        if (manifest.@package != moduleExt.applicationId && moduleExt.applicationId != null && !moduleExt.applicationId.isEmpty()) {
            manifest.@package = moduleExt.applicationId
        }

        boolean isFindMain = false
        if (moduleExt.mainActivity != null && !moduleExt.mainActivity.isEmpty()) {
            manifest.application.activity.each { activity ->
                if (activity.@'android:name' == moduleExt.mainActivity) {
                    def filter = activity.'intent-filter'.find {
                        it.action.@'android:name' == "android.intent.action.MAIN"
                    }
                    isFindMain = true
                    setMainIntentFilter(activity, filter != null && filter.size() > 0)
                }
            }
        }

        manifest.application.activity.each { activity ->
            def filter = activity.'intent-filter'.find {
                it.action.@'android:name' == "android.intent.action.MAIN"
            }
            if (filter != null
                    && moduleExt.mainActivity != null
                    && !moduleExt.mainActivity.isEmpty()
                    && activity.@'android:name' != moduleExt.mainActivity) {
                filter.replaceNode {}
            }
        }

        if (!isFindMain) {
            addMainActivity(manifest.application, moduleExt)
        }

        buildModulesManifest(manifest, moduleExt, appPlugin, isDebug)
    }

    void addMainActivity(def application, BaseExt baseExt) {
        if (baseExt.mainActivity != null && !baseExt.mainActivity.isEmpty()) {
            application.appendNode {
                activity('android:name': baseExt.mainActivity) {
                    'intent-filter' {
                        action('android:name': "android.intent.action.MAIN")
                        category('android:name': "android.intent.category.LAUNCHER")
                    }
                }
            }
        }

    }

    void buildModulesManifest(def manifestFile, BaseExt moduleExt, BasePlugin appPlugin, boolean isDebug) {
        println ":${moduleExt.name}cleanBuildModulesManifest"
        def outputGroupFile = new File(outputGroupPath)
        if (outputGroupFile.exists()) {
            outputGroupFile.deleteDir()
        }
        if ((moduleExt instanceof LibraryExt) && !((moduleExt as LibraryExt).isRunAlone && isDebug)) {
            return
        }

        println ":${moduleExt.name}buildModulesManifest"
        outputGroupFile = new File(outputGroupPath)
        if (!outputGroupFile.exists()) {
            outputGroupFile.mkdirs()
        }
        def outputFile = new File(outputPath)

        StreamingMarkupBuilder outputBuilder = new StreamingMarkupBuilder()
        String root = outputBuilder.bind {
            mkp.xmlDeclaration()
            mkp.yield manifestFile
        }
        String result = XmlUtil.serialize(root)
        outputFile.text = result
        if (appPlugin instanceof AppPlugin) {
            appPlugin.extension.sourceSets {
                main {
                    manifest.srcFile(outputPath)
                }
            }
        }
    }

    abstract void setApplication(def application, BaseExt baseExt)

    abstract void setMainIntentFilter(def activity, boolean isFindMain)
}
