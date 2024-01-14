package com.su.basevideopluginframework.framework.data


/**
 *
 * Created by Ryens.
 * https://github.com/RyensX
 *
 * 视频分类信息
 *
 * @param name 分类名称
 * @param classifyUrl 分类对应链接
 * @param videoList 对应分类下的视频信息
 */
data class VideoClassifyInfo(
    val name: String,
    val classifyUrl: String,
    val videoList: List<VideoInfo>
)