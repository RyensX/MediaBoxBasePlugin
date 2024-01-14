package com.su.basevideopluginframework.framework

import android.graphics.Color
import android.graphics.Typeface
import android.util.Log
import android.view.Gravity
import com.su.basevideopluginframework.BasePluginFactory
import com.su.basevideopluginframework.framework.data.VideoDetailInfo
import com.su.basevideopluginframework.util.JsoupUtil
import com.su.mediabox.pluginapi.action.DetailAction
import com.su.mediabox.pluginapi.action.PlayAction
import com.su.mediabox.pluginapi.components.IMediaDetailPageDataComponent
import com.su.mediabox.pluginapi.data.*
import com.su.mediabox.pluginapi.util.TextUtil.urlEncode
import com.su.mediabox.pluginapi.util.UIUtil.dp
import org.jsoup.nodes.Document

abstract class VideoDetailFramework : IMediaDetailPageDataComponent {

    abstract fun videoDetail(document: Document): VideoDetailInfo?

    final override suspend fun getMediaDetailData(partUrl: String): Triple<String, String, List<BaseData>> {
        Log.d("videoDetail", "partUrl=$partUrl")
        val detailInfo = videoDetail(JsoupUtil.getDocument(Const.url(partUrl)))
        val otherDetailList = mutableListOf<BaseData>()
        detailInfo?.apply {
            //封面
            otherDetailList.add(Cover1Data(this.coverUrl, score = this.score).apply {
                layoutConfig =
                    BaseData.LayoutConfig(
                        itemSpacing = 12.dp,
                        listLeftEdge = 12.dp,
                        listRightEdge = 12.dp
                    )
            })
            //标题
            otherDetailList.add(
                SimpleTextData(this.name).apply {
                    fontColor = Color.WHITE
                    fontSize = 20F
                    gravity = Gravity.CENTER
                    fontStyle = 1
                }
            )
            //简介
            otherDetailList.add(
                LongTextData(this.desc).apply {
                    fontColor = Color.WHITE
                }
            )
            //豆瓣
            otherDetailList.add(LongTextData(douBanSearch(this.name)).apply {
                fontSize = 14F
                fontColor = Color.WHITE
                fontStyle = Typeface.BOLD
            })
            //更新状态
            if (this.update.isNotBlank())
                otherDetailList.add(SimpleTextData("·${this.update}").apply {
                    fontSize = 14F
                    fontColor = Color.WHITE
                    fontStyle = Typeface.BOLD
                })
        }
        //播放列表
        val play = mutableListOf<BaseData>()
        detailInfo?.playList?.forEach {
            play.add(
                SimpleTextData(it.name).apply {
                    fontSize = 16F
                    fontColor = Color.WHITE
                }
            )
            play.add(
                EpisodeListData(
                    it.playList.map {
                        EpisodeData(it.name, it.url).apply {
                            action = PlayAction.obtain(it.url)
                        }
                    }
                )
            )
        }
        //相关作品
        val relate = mutableListOf<BaseData>()
        detailInfo?.relatedVideoList?.forEach {
            relate.add(MediaInfo1Data(
                it.name, it.coverUrl, it.videoDetailUrl,
                nameColor = Color.WHITE, coverHeight = 120.dp
            ).apply {
                action = DetailAction.obtain(it.videoDetailUrl)
            })
        }
        return Triple(
            detailInfo?.coverUrl ?: "",
            detailInfo?.name ?: "",
            mutableListOf<BaseData>().apply {
                addAll(otherDetailList)
                addAll(play)
                addAll(relate)
            })
    }

    private fun douBanSearch(name: String) =
        "·豆瓣评分：https://m.douban.com/search/?query=${name.urlEncode()}"
}