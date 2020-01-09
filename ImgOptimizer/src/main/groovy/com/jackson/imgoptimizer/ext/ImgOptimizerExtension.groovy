package com.jackson.imgoptimizer.ext

import com.jackson.imgoptimizer.core.Constants
import com.jackson.imgoptimizer.utils.OptimizerLevel

class ImgOptimizerExtension {
    /**优化后生成的图片后缀名,默认为空*/
    String suffix = ''
    /**触发优化的图片最小Size (kb),默认为0, 都优化*/
    int triggerSize = 0
    /**压缩级别, 默认无损损压缩*/
    int level = OptimizerLevel.TWO.level
}