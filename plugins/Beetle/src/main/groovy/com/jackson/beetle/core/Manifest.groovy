package com.jackson.beetle.core

import com.android.build.gradle.AppPlugin
import com.android.build.gradle.BasePlugin
import com.jackson.beetle.ext.BaseExt
import com.jackson.beetle.ext.ModuleExt
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
    protected String manifestPath
    protected GPathResult manifestXmlParse
    protected String outputGroupPath
    protected String outputManifestPath
    private Project project

    Manifest(Project project) {
        this.project = project
        manifestPath = "${project.getBuildFile().getParent()}/src/main/AndroidManifest.xml"
        outputGroupPath = "${project.getBuildFile().getParent()}/beetle"
        outputManifestPath = "${project.getBuildFile().getParent()}/beetle/AndroidManifest.xml"
        File manifestFile = new File(manifestPath)
        if (!manifestFile.getParentFile().exists() && !manifestFile.getParentFile().mkdirs()) {
            println "Unable to find AndroidManifest and create fail, please manually create !"
        }
        manifestXmlParse = new XmlSlurper(false, false).parse(manifestFile)
    }

    void resetManifest(BaseExt moduleExt, BasePlugin appPlugin, boolean isDebug) {
        setApplication(manifestXmlParse.application, moduleExt)
        if (manifestXmlParse.@package != moduleExt.applicationId
                && moduleExt.applicationId != null
                && !moduleExt.applicationId.isEmpty()) {
            manifestXmlParse.@package = moduleExt.applicationId
        }

        boolean isFindMain = false

        if (moduleExt.mainActivity != null && !moduleExt.mainActivity.isEmpty()) {
            manifestXmlParse.application.activity.each { activity ->
                def filter = activity.'intent-filter'.find {
                    it.action.@'android:name' == "android.intent.action.MAIN"
                }
                if (activity.@'android:name' == moduleExt.mainActivity) {
                    // 设置module配置的mainActivity 为启动Activity
                    isFindMain = true
                    setMainIntentFilter(activity, filter != null && filter.size() > 0)
                } else {
                    // 移除非mainActivity的所有Activity的action:name="android.intent.action.MAIN"的intent-filter属性
                    if (filter != null) {
                        filter.replaceNode {}
                    }
                }
            }
        }

        if (!isFindMain) {
            addMainActivity(manifestXmlParse.application, moduleExt)
        }

        buildModulesManifest(manifestXmlParse, moduleExt, appPlugin, isDebug)
    }

    private void addMainActivity(def application, BaseExt baseExt) {
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
        println ":${moduleExt.name}  cleanBuildModulesManifest"
        def outputGroupFile = new File(outputGroupPath)
        if (outputGroupFile.exists()) {
            outputGroupFile.deleteDir()
        }
        if ((moduleExt instanceof ModuleExt) && !((moduleExt as ModuleExt).isRunAlone && isDebug)) {
            return
        }

        println ":${moduleExt.name}  buildModulesManifest"
        outputGroupFile = new File(outputGroupPath)
        if (!outputGroupFile.exists()) {
            outputGroupFile.mkdirs()
        }
        def outputFile = new File(outputManifestPath)

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
                    manifest.srcFile(outputManifestPath)
                }
            }
        }
    }

    abstract void setApplication(def application, BaseExt baseExt)

    abstract void setMainIntentFilter(def activity, boolean isFindMain)
}
