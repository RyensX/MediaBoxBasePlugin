package com.su.basevideopluginframework.util

import android.net.Uri
import android.util.Log
import android.webkit.CookieManager
import com.su.basevideopluginframework.framework.Const
import com.su.basevideopluginframework.util.Text.trimAll
import com.su.mediabox.pluginapi.action.WebBrowserAction
import com.su.mediabox.pluginapi.util.AppUtil
import com.su.mediabox.pluginapi.util.PluginPreferenceIns
import com.su.mediabox.pluginapi.util.WebUtil
import com.su.mediabox.pluginapi.util.WebUtilIns
import org.jsoup.HttpStatusException
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

object JsoupUtil {
    /**
     * 获取没有运行js的html
     */
    suspend fun getDocument(url: String): Document =
        runCatching { getDocumentSynchronously(url) }.getOrThrow()

    const val cfClearanceKey = "cf_clearance"

    private suspend fun getHtmlCode(url: String) = Jsoup.connect(url)
        .userAgent(Const.ua)
        .get()

    suspend fun getDocumentSynchronously(url: String): Document =
        try {
            getHtmlCode(url)
        } catch (e: HttpStatusException) {
            //遇到cloudflare验证
            if (e.statusCode == 503) {
                //遇到cloudflare验证
                Log.e("cloudflare", "发现验证 链接:$url 异常:$e")
                CookieManager.getInstance().removeAllCookies(null)
                PluginPreferenceIns.set(cfClearanceKey, "", false)
                //模拟网页获取验证参数
                WebUtilIns.interceptResource(
                    url, "jquery",
                    loadPolicy = object :
                        WebUtil.LoadPolicy by WebUtil.DefaultLoadPolicy {
                        override val userAgentString = Const.ua
                        override val isClearEnv = true
                        override val timeOut: Long = 30 * 1000
                        override val isBlockRes = false
                    }
                )
                val host = Uri.parse(url).run { "$scheme://$host" }
                val cfClearance = CookieManager.getInstance().getCookie(host) ?: ""
                Log.d("cloudflare", "验证信息$cfClearance")
                if (cfClearance.isNotBlank() && cfClearance.contains(cfClearanceKey)) {
                    Log.d("cloudflare", "获取验证信息成功")
                    //重新存入并重新发起请求
                    cfClearance.split(";").find { it.contains(cfClearanceKey) }?.also {
                        val target = "${it};rtsm=1".trimAll()
                        Log.d("cloudflare", "重新存入并重新发起请求($host)：\"$it\"")
                        PluginPreferenceIns.set(cfClearanceKey, target, false)
                        CookieManager.getInstance().setCookie(host, target)
                    }
                } else {
                    Log.e("cloudflare", "获取验证信息失败($host)")
                    //无感知验证失败，只是继续尝显式手动验证
                    CookieManager.getInstance().setCookie(host, "")
                    WebBrowserAction.obtain(url,
                        loadPolicy = object :
                            WebUtil.LoadPolicy by WebUtil.DefaultLoadPolicy {
                            override val userAgentString = Const.ua
                        }).go(AppUtil.appContext)
                    throw RuntimeException("请在验证结束后点击左上角返回，然后下拉刷新")
                }
                getHtmlCode(url)
            } else
                throw e

        }


}