package com.jackson.beetle.task

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

/**
 *
 * desc:  beetle 插件测试task
 * author: 行走的老者
 * date: 2020-01-09 17:10
 */
class BeetleTestTask extends DefaultTask {

    List<String> moduleList

    @TaskAction
    void excute() {
        println("BeetleTestTask excute ....${moduleList}")
    }

}
