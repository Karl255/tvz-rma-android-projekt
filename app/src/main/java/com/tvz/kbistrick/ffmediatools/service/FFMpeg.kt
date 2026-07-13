package com.tvz.kbistrick.ffmediatools.service

import android.content.Context
import android.util.Log

object FFMpeg {
    private const val TAG = "FFMpeg"
    private const val EXECUTABLE = "libffmpegbin.so"

    fun execute(context: Context, args: List<String>): Boolean {
        val libraryDir = context.applicationInfo.nativeLibraryDir
        val binaryPath = "$libraryDir/$EXECUTABLE"

        return try {
            val command = listOf(binaryPath) + args
            logCommand(command)

            val process = ProcessBuilder(command)
                .directory(context.filesDir)
                .redirectErrorStream(true)
                .start()

            process.inputStream.bufferedReader().use { reader ->
                var line: String?
                while (reader.readLine().also { line = it } != null) {
                    Log.d("ffmpeg stdout", line ?: "")
                }
            }

            val exitCode = process.waitFor()

            Log.println(if (exitCode == 0) Log.INFO else Log.ERROR, TAG, "Process finished with exit code $exitCode")

            exitCode == 0
        } catch (e: Exception) {
            Log.e(TAG, "Error executing ffmpeg", e)

            false
        }
    }

    fun logCommand(args: List<String>) {
        Log.d(TAG, "Running ffmpeg with args:")

        for (i in args.indices) {
            Log.d(TAG, "  [$i]: ${args[i]}")
        }
    }
}