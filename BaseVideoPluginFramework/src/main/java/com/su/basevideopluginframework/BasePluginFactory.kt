package com.su.basevideopluginframework

import androidx.annotation.CallSuper
import com.su.basevideopluginframework.framework.*
import com.su.mediabox.pluginapi.IPluginFactory

/**
 * 每个插件必须实现本类
 *
 * 注意包和类名都要相同，且必须提供公开的无参数构造方法
 */
abstract class BasePluginFactory : IPluginFactory() {

    @CallSuper
    override fun pluginLaunch() {
        Const.host = host
    }
}