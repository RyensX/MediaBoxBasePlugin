package com.su.basevideopluginframework.framework.data


/**
 *
 * Created by Ryens.
 * https://github.com/RyensX
 *
 * 框架通用视频信息
 *
 * @param name 视频名称
 * @param coverUrl 封面Url
 * @param videoDetailUrl 视频详情链接，点击跳转
 * @param episode 剧集信息，一般用于展示更新到哪集
 */
data class VideoInfo(
    val name: String,
    val coverUrl: String,
    val videoDetailUrl: String,
    val episode: String
)