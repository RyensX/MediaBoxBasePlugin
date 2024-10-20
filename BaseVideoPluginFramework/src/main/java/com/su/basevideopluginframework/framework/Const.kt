package com.su.basevideopluginframework.framework

import android.net.Uri
import com.su.mediabox.pluginapi.Constant
import java.net.URL

object Const {

    lateinit var host: String
    private val hostUrl by lazy {
        URL(host)
    }

    val ua = Constant.Request.USER_AGENT_ARRAY[0]

    const val INVALID_GREY: Int = 0xff999999.toInt()

    fun url(rawUrl: String): String {
        return if (rawUrl.startsWith("http"))
            rawUrl
        else {
            URL(hostUrl, rawUrl).toString()
        }
    }

}