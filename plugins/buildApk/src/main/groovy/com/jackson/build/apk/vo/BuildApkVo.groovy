package com.jackson.build.apk.vo

import org.apache.http.util.TextUtils
import org.gradle.api.Action

/**
 * @author: jackson liao
 * @createDate: 2021/11/2 11:19
 * @description: 加固自动化配置
 */
class BuildApkVo {
    Boolean debugEnable = false
    Boolean enable = false

    Build360Vo build360 = new Build360Vo()
    BuildChannelVo buildChannel = new BuildChannelVo()

    // 文档
    // https://www.jianshu.com/p/58d86b4c0ee5
    // https://www.jianshu.com/p/1d96fa45c3d5

    void build360(Action<Build360Vo> action) {
        action.execute(build360)
    }

    void buildChannel(Action<BuildChannelVo> action) {
        action.execute(buildChannel)
    }


    String getChannelOutputPath() {
        if (buildChannel == null) return null
        if (TextUtils.isEmpty(buildChannel.outPutPath)) return null
        return buildChannel.outPutPath
    }

    @Override
    public String toString() {
        return "BuildApkVo{" +
                "debugEnable=" + debugEnable +
                ", enable=" + enable +
                ", build360=" + build360 +
                ", buildChannel=" + buildChannel +
                '}';
    }
}
