package com.jackson.imgoptimizer.core

import com.jackson.imgoptimizer.utils.Logger
import org.gradle.api.Project

interface Optimizer {
    void optimize(Project project, Logger log, String suffix, List<File> files)
}