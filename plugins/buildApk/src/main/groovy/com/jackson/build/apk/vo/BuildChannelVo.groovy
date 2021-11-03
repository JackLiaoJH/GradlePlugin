package com.jackson.build.apk.vo

/**
 * @author: jackson liao
 * @createDate: 2021/11/2 11:19
 * @description: 多渠道打包实体
 */
class BuildChannelVo {
    Boolean enable = false
    String channelPath = ""
    String outPutPath = ""

    /**jar 加固路径需要自己赋值*/
    String jarPath = ""


    @Override
    public String toString() {
        return "BuildChannelVo{" +
                "enable=" + enable +
                ", channelPath='" + channelPath + '\'' +
                ", outPutPath='" + outPutPath + '\'' +
                ", jarPath='" + jarPath + '\'' +
                '}';
    }
}