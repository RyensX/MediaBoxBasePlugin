package com.su.basevideopluginframework.framework

import com.su.basevideopluginframework.framework.data.VideoSearchInfo
import com.su.basevideopluginframework.util.JsoupUtil
import com.su.mediabox.pluginapi.action.ClassifyAction
import com.su.mediabox.pluginapi.action.DetailAction
import com.su.mediabox.pluginapi.components.IMediaSearchPageDataComponent
import com.su.mediabox.pluginapi.data.BaseData
import com.su.mediabox.pluginapi.data.MediaInfo2Data
import com.su.mediabox.pluginapi.data.TagData
import org.jsoup.nodes.Document

abstract class VideoSearchFramework : IMediaSearchPageDataComponent {

    /**
     * 搜索地址模板，搜索关键词用“%key"代替，页码用%page代替
     */
    abstract val searchUrlTemplate: String

    abstract suspend fun search(document: Document): List<VideoSearchInfo>?

    override suspend fun getSearchData(keyWord: String, page: Int): List<BaseData> {
        return search(
            JsoupUtil.getDocument(
                searchUrlTemplate.replace("%key", keyWord).replace("%page", page.toString())
            )
        )?.map { searchInfo ->
            MediaInfo2Data(
                searchInfo.name,
                searchInfo.coverUrl,
                searchInfo.url,
                searchInfo.episode,
                searchInfo.desc,
                searchInfo.tagList?.map { tag ->
                    TagData(tag.name).apply {
                        tag.url?.let {
                            action = ClassifyAction.obtain(it, name)
                        }
                    }
                })
                .apply {
                    action = DetailAction.obtain(url)
                }
        } ?: emptyList()
    }

}