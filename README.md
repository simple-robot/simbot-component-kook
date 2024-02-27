# Simple Robot - KOOK 组件

这是 
[Simple Robot v4][simbot4] 
下的子项目，是针对
[KOOK(开黑啦)bot](https://developer.kookapp.cn/doc/reference)
各方面的 simbot 组件库实现，
包括对 `API` 内容的实现、事件相关的实现以及 bot 对于事件的监听与交互等。

KOOK组件库可以作为底层API依赖使用、
轻量级的KOOK事件调度框架使用，
也可以基于 simbot 核心库的种种快速开发一个功能强大的 KOOK 机器人！

- 基于 [`Kotlin`](https://kotlinlang.org/) 提供 [KMP 多平台](https://kotlinlang.org/docs/multiplatform.html) 特性，提供 Java 友好的API。
- 基于 [`Kotlin coroutines`](https://github.com/Kotlin/kotlinx.coroutines) 与 [`Ktor`](https://ktor.io/) 提供轻量高效的API。

> [!Note]
> 下文中 `Simple Robot v4` 简称为 `simbot4`

## 文档

- 了解simbot: [**Simple Robot 应用手册**](https://simbot.forte.love)
- **KOOK组件**手册：<https://component-kook.simbot.forte.love/> (即当前仓库的 GitHub Pages)
- **API文档**: [**文档引导站点**](https://docs.simbot.forte.love) 中KOOK的 [**KDoc站点**](https://docs.simbot.forte.love/components/qq-guild)

---

## 支持列表

前往 [支持列表](support-list.md) 查看已经支持的API、尚未支持的API。
如果你迫切的希望支持某些API，而它们尚未被支持，请[让我们知道](https://github.com/simple-robot/simbot-component-kook/issues)!

## 贡献

前往 [贡献手册](docs/CONTRIBUTING_CN.md) 了解更多！

## 快速开始

前往 [组件手册][website] 阅读 **快速开始** 相关章节。

## 法欧莉

如果你想看一看使用 KOOK组件实现的具体作品，
不妨添加我们亲爱的 [法欧莉斯卡雷特](https://www.kookapp.cn/app/oauth2/authorize?id=10250&permissions=197958144&client_id=jqdlyHK85xe1i5Bo&redirect_uri=&scope=bot) 
并使用 `@法欧莉 今天的我` 来看看效果吧~


## License

`simbot-component-kook` 使用 `LGPLv3` 许可证开源。

```
This program is free software: you can redistribute it and/or modify it under the terms of 
the GNU Lesser General Public License as published by the Free Software Foundation, either 
version 3 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
See the GNU Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public License along with this 
program. If not, see <https://www.gnu.org/licenses/>.
```

[m-api]: simbot-component-kook-api
[m-stdlib]: simbot-component-kook-stdlib
[m-core]: simbot-component-kook-core
[simbot4]: https://github.com/simple-robot/simpler-robot

[website]: https://component-kook.simbot.forte.love/
