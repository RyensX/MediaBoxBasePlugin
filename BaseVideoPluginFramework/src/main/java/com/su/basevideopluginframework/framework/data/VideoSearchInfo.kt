package com.su.basevideopluginframework.framework.data

data class VideoSearchInfo(
    val name: String,
    val coverUrl: String,
    val url: String,
    val episode: String,
    val desc: String,
    val tagList: List<VideoTag>? = null,
)