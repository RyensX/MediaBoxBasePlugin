package com.su.basevideopluginframework.framework.data

import com.su.mediabox.pluginapi.data.TagData


/**
 *
 * Created by Ryens.
 * https://github.com/RyensX
 */
class VideoDetailInfo(
    val name: String,
    val desc: String,
    val score: Float,
    val coverUrl: String,
    val update: String,
    val playList: List<VideoPlayListInfo>,
    val relatedVideoList: List<VideoInfo>? = null
    //val tag: List<TagData>? = null
)