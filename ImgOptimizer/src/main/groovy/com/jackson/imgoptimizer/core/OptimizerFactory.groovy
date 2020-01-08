package com.jackson.imgoptimizer.core

import com.jackson.imgoptimizer.core.impl.PngquantOptimizer
import com.jackson.imgoptimizer.core.impl.ZopflipngOptimizer

class OptimizerFactory {
    private OptimizerFactory() {}

    static Optimizer getOptimizer(String level) {
        if (Constants.LEVEL_LOSSY == level) {
            return new PngquantOptimizer()
        } else if (Constants.LEVEL_LOSS_LIMIT == level) {
            return new ZopflipngOptimizer()
        } else {
            throw new IllegalArgumentException("Unacceptable optimizer level. Please use ${Constants.LEVEL_LOSSY} or ${Constants.LEVEL_LOSS_LIMIT}.")
        }
    }
}