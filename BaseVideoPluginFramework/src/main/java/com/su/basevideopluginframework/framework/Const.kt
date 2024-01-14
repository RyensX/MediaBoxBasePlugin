package com.su.basevideopluginframework.framework

import android.net.Uri
import com.su.mediabox.pluginapi.Constant

object Const {

    lateinit var host: String

    val ua = Constant.Request.USER_AGENT_ARRAY[0]

    const val INVALID_GREY: Int = 0xff999999.toInt()

    fun url(rawUrl: String): String {
        return if (rawUrl.startsWith("http"))
            rawUrl
        else {
            Uri.parse(host).buildUpon().appendPath(rawUrl).build().toString()
        }
    }

}