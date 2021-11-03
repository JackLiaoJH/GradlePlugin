package com.jackson.build.apk.vo

/**
 * @author: jackson liao
 * @createDate: 2021/11/2 11:19
 * @description: 360 加固配置
 */
class Build360Vo {
    Boolean enable = false
    String account = ""
    String password = ""

    /**jar 加固路径需要自己赋值*/
    String jarPath = ""


    @Override
    public String toString() {
        return "Build360Vo{" +
                "enable=" + enable +
                ", account='" + account + '\'' +
                ", password='" + password + '\'' +
                ", jarPath='" + jarPath + '\'' +
                '}';
    }
}