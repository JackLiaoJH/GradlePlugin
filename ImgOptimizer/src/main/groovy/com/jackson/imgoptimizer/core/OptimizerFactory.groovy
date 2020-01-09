package com.jackson.imgoptimizer.core

import com.jackson.imgoptimizer.core.impl.PngquantOptimizer
import com.jackson.imgoptimizer.core.impl.ZopflipngOptimizer
import com.jackson.imgoptimizer.utils.OptimizerLevel

class OptimizerFactory {
    private OptimizerFactory() {}

    static Optimizer getOptimizer(OptimizerLevel optimizerLevel) {
        switch (optimizerLevel) {
            case OptimizerLevel.ONE:
                return new ZopflipngOptimizer(optimizerLevel)
            case OptimizerLevel.TWO:
                return new PngquantOptimizer(optimizerLevel)
            default:
                return new PngquantOptimizer(optimizerLevel)
        }
    }
}