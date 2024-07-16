---
switcher-label: Java API 风格
---
# 消息元素

<include from="snippets.md" element-id="to-main-doc" />

本章节介绍 KOOK 组件中针对 simbot 标准库中 **消息元素(`Message.Element`)** 的实现类型。


## Message.Element

先简单介绍一下 `Message.Element`。它是 simbot 标准库中提供一个接口类型，
用来定义一个**消息元素**。一个消息元素或多个消息元素组成的**消息链**可用于发送消息。

simbot 标准库提供了一些常见的标准消息元素实现，例如 `At`、`Image` 等。
但是很多情况下，对于一个组件而言这些标准实现可能不能满足需求，这时候就需要组件实现这个接口，
来提供更多期望的功能。

> 你可以前往 [simbot4 手册的消息元素](https://simbot.forte.love/basic-messages.html)
> 了解更多详情。

## 标准消息元素支持

<deflist>
<def title="PlainText">

最基础的消息元素类型。支持使用文本消息元素，
例如 `Text`。

</def>
<def title="At">

提及一个用户、提及一个角色或提及一个子频道。
提及目标根据 `At.type` 决定:

- `user`(默认) 或其他未知: 提及一个用户
- `role`: 提及一个角色
- `channel`: 提及一个子频道

</def>
<def title="AtAll">

提及所有人。即 `@全体`。

</def>
<def title="Image">

支持使用: 

<list>
<li>

`OfflineImage`: 上传一个本地离线文件作为图片并用于发送。

</li>
<li>

`RemoteImage`: 如果是 `RemoteUrlAwareImage`，则使用它的 `url`，否则将它的 `id` 视为 `url` 字符串。

> 不会进行 url 的有效性校验。因为如果不是url、或者 url 不能用于发送则可能会引发异常。

</li>
</list>


</def>
<def title="Face">

会作为 [KMarkdown](https://developer.kookapp.cn/doc/kmarkdown) 
中的 “服务器表情” 使用。

<warning>
目前已知的问题是 `Face` 只有 `id`,
而“服务器表情”似乎还需要“名称”, 因此会将 `id` 也视为名称,
最终产生如下结果:

`(emj)服务器表情id(emj)[服务器表情id]`

</warning>

</def>
<def title="Emoji">

会作为 [KMarkdown](https://developer.kookapp.cn/doc/kmarkdown)
中的 “emoji” 使用。
`Emoji.id` 会被处理为:

`:Emoji.id:`

</def>
</deflist>

## KOOK 组件消息元素实现

KOOK 组件进行特殊实现的 `Message.Element` 均继承自 `love.forte.simbot.component.kook.message.KookMessageElement`。

<deflist>
<def title="KookAssetMessage"></def>
<def title="KookKMarkdownMessage"></def>
<def title="KookCardMessage"></def>
<def title="KookAtAllHere"></def>
<def title="KookAttachmentMessage"></def>
<def title="KookAttachmentMessage">

<deflist>
<def title="KookAttachment"></def>
<def title="KookAttachmentFile"></def>
<def title="KookAttachmentImage"></def>
<def title="KookAttachmentVideo"></def>
</deflist>

</def>
<def title="KookAssetImage"></def>
</deflist>
