package com.tvz.kbistrick.ffmediatools.model

object Action {
    object JobFinished {
        const val ACTION = "com.tvz.kbistrick.ffmediatools.ACTION_JOB_FINISHED"
        const val OUTPUT_PATH = "outputPath"
        const val WIDTH = "width"
        const val HEIGHT = "height"
        const val THUMBNAIL_PATH = "thumbnailPath"
    }

    object StartJob {
        const val ACTION = "com.tvz.kbistrick.ffmediatools.ACTION_START_JOB"
        const val ARGS = "args"
        const val OUTPUT_PATH = "outputPath"
        const val NOTIFICATION_DESCRIPTION = "notificationDescription"
    }

    object AbortJob {
        const val ACTION = "com.tvz.kbistrick.ffmediatools.ACTION_ABORT_JOB"
    }
}