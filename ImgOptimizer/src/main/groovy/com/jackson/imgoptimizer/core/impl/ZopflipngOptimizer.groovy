package com.jackson.imgoptimizer.core.impl

import com.jackson.imgoptimizer.core.Optimizer
import com.jackson.imgoptimizer.utils.Logger
import com.jackson.imgoptimizer.utils.ZopflipngUtils
import org.gradle.api.Project

class ZopflipngOptimizer implements Optimizer {
    @Override
    void optimize(Project project, Logger log, String suffix, List<File> files) {
        ZopflipngUtils.copyZopflipng2BuildFolder(project)
        if (suffix == null || "" == suffix.trim()) {
            suffix = ".png"
        } else if (!suffix.endsWith(".png")) {
            suffix += ".png"
        }

        int succeed = 0
        int skipped = 0
        int failed = 0
        long totalSaved = 0L
        def zopflipng = ZopflipngUtils.getZopflipngFilePath(project)
        files.each { file ->
            long originalSize = file.length()

            String output = file.absolutePath.substring(0, file.absolutePath.lastIndexOf(".")).concat(suffix)
            Process process = new ProcessBuilder(zopflipng, "-y", "-m", file.absolutePath, output).
                    redirectErrorStream(true).start();
            BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()))
            StringBuilder error = new StringBuilder()
            String line
            while (null != (line = br.readLine())) {
                error.append(line)
            }
            int exitCode = process.waitFor()

            if (exitCode == 0) {
                succeed++
                long optimizedSize = new File(output).length()
                float rate = 1.0f * (originalSize - optimizedSize) / originalSize * 100
                totalSaved += (originalSize - optimizedSize)
                log.i("Succeed! ${originalSize}B-->${optimizedSize}B, ${rate}% saved! ${file.absolutePath}")
            } else {
                failed++
                log.e("Failed! ${file.absolutePath}")
                log.e("Exit: ${exitCode}. " + error.toString())
            }
        }

        log.i("Total: ${files.size()}, Succeed: ${succeed}, " +
                "Skipped: ${skipped}, Failed: ${failed}, Saved: ${totalSaved / 1024}KB")
    }
}