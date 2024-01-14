package com.su.basevideopluginframework.framework.data

/**
 *
 * Created by Ryens.
 * https://github.com/RyensX
 *
 * 框架通用视频横幅信息
 *
 * @param imageUrl 横幅图片url
 * @param title 横幅标题
 * @param desc 横幅说明信息
 * @param videoDetailUrl 视频详情链接，点击跳转
 */
data class VideoBannerData(
    val imageUrl: String,
    val title: String,
    val desc: String,
    val videoDetailUrl: String
)