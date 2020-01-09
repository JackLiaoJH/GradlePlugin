package com.jackson.imgoptimizer.utils

import com.jackson.imgoptimizer.core.Constants

/**
 *
 * desc: 压缩级别
 * author: 行走的老者
 * date: 2020-01-09 11:10
 */
enum OptimizerLevel {

    ONE(Constants.ZOP_FLI_PNG, 1), TWO(Constants.PNG_QUANT, 2)

    private String name
    private int level

    OptimizerLevel(String name, int level) {
        this.name = name
        this.level = level
    }

    def getName() {
        return name
    }

    def getLevel() {
        return level
    }

    static def getOptimizerLevel(int level) {
        switch (level) {
            case ONE.level:
                return ONE
            case TWO.level:
                return TWO
            default:
                return TWO
        }
    }
}