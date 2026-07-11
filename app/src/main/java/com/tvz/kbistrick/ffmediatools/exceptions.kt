package com.tvz.kbistrick.ffmediatools

class MediaInfoReadException : Exception("Failed to read info of selected media")

class FatalAppException(message: String) : Exception("Fatal app error: $message")
