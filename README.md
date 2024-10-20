# BaseVideoPluginFramework

[**媒体盒子**](https://github.com/RyensX/MediaBoxPlugin)视频插件框架

提供基础UI和数据框架，方便快速实现视频插件

详细使用例子见[这里](https://github.com/RyensX/CooingPlugin)

## 如何使用

1. ```git submodule```引入本仓库，具体做法:

在目标工程根目录下加入git子模块
```
git submodule add https://github.com/RyensX/MediaBoxBasePlugin.git submodules/MediaBoxBasePlugin
```
如果报错则手动建立submodules文件夹

然后再初始化一下
```
git submodule update --init --recursive
```

在目标工程的settings.gradle(.kts)尾部加入
```
val baseFrameworkPath = "./submodules/MediaBoxBasePlugin"

val basePluginApi = ":BaseVideoPluginFramework"
include(basePluginApi)
project(basePluginApi).projectDir = File("$baseFrameworkPath/BaseVideoPluginFramework")

val pluginApi = ":MediaBoxPluginApi"
include(pluginApi)
project(pluginApi).projectDir = File("$baseFrameworkPath/submodules/MediaBoxPlugin/pluginApi")

```

2. 按需继承并编写Framework组件
3. 继承BasePluginFactory编写PluginFactory，具体做法类似[这里](https://github.com/RyensX/MediaBox/wiki/2.%E5%BC%80%E5%8F%91%E5%AE%9E%E4%BE%8B#2%E6%B3%A8%E5%86%8C%E6%8F%92%E4%BB%B6)
4. 按照[流程](https://github.com/RyensX/MediaBoxPluginRepository)发布插件

## 免责声明

1. 此软件显示的所有内容，其**版权**均**归原作者**所有。
2. 此软件**仅可用作学习交流**，未经授权，**禁止用于其他用途**，请在下载**24小时内删除**。
3. 因使用此软件产生的版权问题，软件作者概不负责。
