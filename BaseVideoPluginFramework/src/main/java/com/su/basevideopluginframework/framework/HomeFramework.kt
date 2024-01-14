package com.su.basevideopluginframework.framework

import android.graphics.Color
import android.graphics.Typeface
import android.view.Gravity
import com.su.basevideopluginframework.BasePluginFactory
import com.su.basevideopluginframework.framework.Const.host
import com.su.basevideopluginframework.framework.data.VideoBannerData
import com.su.basevideopluginframework.framework.data.VideoClassifyInfo
import com.su.basevideopluginframework.util.JsoupUtil
import com.su.mediabox.pluginapi.action.ClassifyAction
import com.su.mediabox.pluginapi.action.DetailAction
import com.su.mediabox.pluginapi.components.IHomePageDataComponent
import com.su.mediabox.pluginapi.data.BannerData
import com.su.mediabox.pluginapi.data.BaseData
import com.su.mediabox.pluginapi.data.MediaInfo1Data
import com.su.mediabox.pluginapi.data.SimpleTextData
import com.su.mediabox.pluginapi.util.UIUtil.dp
import org.jsoup.nodes.Document

/**
 * 封装好的基本视频插件主页框架，只需关注数据获取无需设计UI
 *
 * 具体样式可见樱花动漫2插件
 */
abstract class HomeFramework : IHomePageDataComponent {

    /**
     * 1.横幅，需要显示可重写
     *
     * @param doc 首页DOM树
     */
    open fun bannerList(doc: Document): List<VideoBannerData>? = null

    /**
     * 3.各类推荐
     *
     * @param doc 首页DOM树
     * @return 一个分类名称对一组视频数据的map
     */
    abstract fun classifyList(doc: Document): List<VideoClassifyInfo>?

    private val layoutSpanCount = 12

    final override suspend fun getData(page: Int): List<BaseData>? {
        //首页默认只有1页
        if (page != 1)
            return null
        val url = host
        val doc = JsoupUtil.getDocument(url)
        val data = mutableListOf<BaseData>()

        //1.横幅
        bannerList(doc)?.map {
            BannerData.BannerItemData(it.imageUrl, it.title, it.desc).apply {
                action = DetailAction.obtain(it.videoDetailUrl)
            }
        }?.let {
            if (it.isNotEmpty())
                data.add(BannerData(it, 6.dp).apply {
                    layoutConfig = BaseData.LayoutConfig(layoutSpanCount, 14.dp)
                    spanSize = layoutSpanCount
                })
        }


        //3.各类推荐
        classifyList(doc)?.let { infoList ->
            if (infoList.isNotEmpty()) {
                infoList.forEach {
                    data.add(SimpleTextData(it.name).apply {
                        fontSize = 18F
                        fontStyle = Typeface.BOLD
                        fontColor = Color.BLACK
                        spanSize = layoutSpanCount / 2
                    })
                    data.add(SimpleTextData("查看更多 >").apply {
                        fontSize = 12F
                        gravity = Gravity.RIGHT or Gravity.CENTER_VERTICAL
                        fontColor = Const.INVALID_GREY
                        spanSize = layoutSpanCount / 2
                    }.apply {
                        action = ClassifyAction.obtain(it.classifyUrl, it.name)
                    })
                    it.videoList.forEach { videoInfo ->
                        data.add(MediaInfo1Data(
                            videoInfo.name, videoInfo.coverUrl, videoInfo.videoDetailUrl,
                            videoInfo.episode
                        )
                            .apply {
                                spanSize = layoutSpanCount / 3
                                action = DetailAction.obtain(videoInfo.videoDetailUrl)
                            })
                    }
                }
            }
        }
        data.first().apply {
            layoutConfig = BaseData.LayoutConfig(layoutSpanCount, 14.dp)
        }
        return data
    }
}