package com.su.basevideopluginframework.framework

import android.net.Uri
import android.util.Log
import androidx.core.net.toUri
import com.su.basevideopluginframework.BasePluginFactory
import com.su.basevideopluginframework.framework.Const.ua
import com.su.basevideopluginframework.util.JsoupUtil
import com.su.mediabox.pluginapi.components.IVideoPlayPageDataComponent
import com.su.mediabox.pluginapi.data.VideoPlayMedia
import com.su.mediabox.pluginapi.util.AppUtil
import com.su.mediabox.pluginapi.util.WebUtil
import com.su.mediabox.pluginapi.util.WebUtilIns
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import org.jsoup.nodes.Document
import java.io.File

abstract class VideoPlayFramework : IVideoPlayPageDataComponent {

    private val mTag = "VideoPlayFramework"

    private fun blobDataTmpFile(url: String) =
        File(
            AppUtil.appContext.externalCacheDir,
            "${Uri.parse(url).path?.replace("/", "!") ?: "blob_data_tmp"}.m3u8"
        ).apply {
            if (!exists())
                createNewFile()
        }

    override suspend fun getVideoPlayMedia(episodeUrl: String): VideoPlayMedia {
        val url = Const.url(episodeUrl)
        val document = JsoupUtil.getDocument(url)
        //播放链接
        val episodePlayUrl = withContext(Dispatchers.IO) {
            async { getVideoEpisodePlayUrl(document) ?: "" }
        }

        //剧集名
        val episodeName = withContext(Dispatchers.IO) {
            async {
                getVideoEpisodeName(document)
            }
        }

        return VideoPlayMedia(episodeName.await(), episodePlayUrl.await())
    }

    /**
     * 获取当前播放剧集名称
     *
     * @param doc 播放页面DOM
     */
    abstract suspend fun getVideoEpisodeName(doc: Document): String

    /**
     * 获取当前播放剧集播放链接，一般是m3u8或其他视频文件
     *
     * 默认提供了自动拦截m3u8链接和blob(m3u8)数据的支持，如无效才需要重写
     *
     * @param doc 播放页面DOM
     */
    open suspend fun getVideoEpisodePlayUrl(doc: Document): String? {
        val url = doc.baseUri()
        Log.d(mTag, "getVideoEpisodePlayUrl: url=$url")
        var videoUrl: String? = null
        //尝试拦截m3u8
        videoUrl = WebUtilIns.interceptResource(
            url, "http(.*)\\.m3u8",
            loadPolicy = object : WebUtil.LoadPolicy by WebUtil.DefaultLoadPolicy {
                override val userAgentString = ua
                override val isClearEnv = false
            }
        )
        Log.d(mTag, "getVideoEpisodePlayUrl:intercept m3u8 -> $videoUrl")
        if (!videoUrl.isNullOrBlank()) {
            //进一步处理url（例如传入播放链接到某个url）
            videoUrl = videoUrl.run {
                substring(lastIndexOf("http"))
            }
            return videoUrl
        }
        //尝试拦截blob
        val blobData = WebUtilIns.interceptBlob(
            url,
            "^#EXTM(.*)",
            object : WebUtil.LoadPolicy by WebUtil.DefaultLoadPolicy {
                override val userAgentString = ua
                override val isClearEnv = false
            })
        videoUrl = blobDataTmpFile(url).run {
            writeText(blobData)
            absolutePath
        }.toUri().toString()
        Log.d(mTag, "getVideoEpisodePlayUrl:intercept blob -> $videoUrl")
        return videoUrl
    }

}