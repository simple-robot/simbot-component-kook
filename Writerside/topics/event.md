# 事件定义列表

KOOK组件中的**事件类型**包含两个层面：

1. **API 模块** 中，对 KOOK API 中官方定义的事件结构的基本封装与实现。
2. **核心模块** 中，基于 API 模块中的事件封装，对 simbot4 标准库中的 `Event` 事件类型的实现。

## API 模块事件封装

API 模块所有的事件封装类型都在包 `love.forte.simbot.kook.event` 中，
并且基本上命名与官网API中的事件类型名称有一定关联。

事件类型定义为 `love.forte.simbot.kook.event.Event<out EX : EventExtra>`，
而不同类型的事件之间的区别在于 `EX` 的差异。

所有事件的 `extra` 的封装类型均继承密封类 `love.forte.simbot.kook.event.EventExtra`。

<deflist>
<def title="SelfJoinedGuildEventExtra" id="love_forte_simbot_kook_event_SelfJoinedGuildEventExtra">

`love.forte.simbot.kook.event.SelfJoinedGuildEventExtra`

事件类型名: `"self_joined_guild"`

<a ignore-vars="true" href="https://developer.kookapp.cn/doc/event/user#%E8%87%AA%E5%B7%B1%E6%96%B0%E5%8A%A0%E5%85%A5%E6%9C%8D%E5%8A%A1%E5%99%A8">自己新加入服务器</a>
> 当自己被邀请或主动加入新的服务器时, 产生该事件

</def>
<def title="SelfExitedGuildEventExtra" id="love_forte_simbot_kook_event_SelfExitedGuildEventExtra">

`love.forte.simbot.kook.event.SelfExitedGuildEventExtra`

事件类型名: `"self_exited_guild"`

<a ignore-vars="true" href="https://developer.kookapp.cn/doc/event/user#%E8%87%AA%E5%B7%B1%E9%80%80%E5%87%BA%E6%9C%8D%E5%8A%A1%E5%99%A8">自己退出服务器</a>
> 当自己被踢出服务器或被拉黑或主动退出服务器时, 产生该事件

</def>
<def title="MessageBtnClickEventExtra" id="love_forte_simbot_kook_event_MessageBtnClickEventExtra">

`love.forte.simbot.kook.event.MessageBtnClickEventExtra`

事件类型名: `"message_btn_click"`

<a ignore-vars="true" href="https://developer.kookapp.cn/doc/event/user#Card%20%E6%B6%88%E6%81%AF%E4%B8%AD%E7%9A%84%20Button%20%E7%82%B9%E5%87%BB%E4%BA%8B%E4%BB%B6">Card 消息中的 Button 点击事件</a>

</def>
<def title="AddedChannelEventExtra" id="love_forte_simbot_kook_event_AddedChannelEventExtra">

`love.forte.simbot.kook.event.AddedChannelEventExtra`

事件类型名: `"added_channel"`

<a ignore-vars="true" href="https://developer.kookapp.cn/doc/event/channel#%E6%96%B0%E5%A2%9E%E9%A2%91%E9%81%93">新增频道</a>

</def>
<def title="UpdatedChannelEventExtra" id="love_forte_simbot_kook_event_UpdatedChannelEventExtra">

`love.forte.simbot.kook.event.UpdatedChannelEventExtra`

事件类型名: `"updated_channel"`

<a ignore-vars="true" href="https://developer.kookapp.cn/doc/event/channel#%E4%BF%AE%E6%94%B9%E9%A2%91%E9%81%93%E4%BF%A1%E6%81%AF">修改频道信息</a>

</def>
<def title="DeletedChannelEventExtra" id="love_forte_simbot_kook_event_DeletedChannelEventExtra">

`love.forte.simbot.kook.event.DeletedChannelEventExtra`

事件类型名: `"deleted_channel"`

<a ignore-vars="true" href="https://developer.kookapp.cn/doc/event/channel#%E5%88%A0%E9%99%A4%E9%A2%91%E9%81%93">删除频道</a>

</def>
<def title="AddedReactionEventExtra" id="love_forte_simbot_kook_event_AddedReactionEventExtra">

`love.forte.simbot.kook.event.AddedReactionEventExtra`

事件类型名: `"added_reaction"`

<a ignore-vars="true" href="https://developer.kookapp.cn/doc/event/channel#%E9%A2%91%E9%81%93%E5%86%85%E7%94%A8%E6%88%B7%E6%B7%BB%E5%8A%A0%20reaction">频道内用户添加 reaction</a>

</def>
<def title="DeletedReactionEventExtra" id="love_forte_simbot_kook_event_DeletedReactionEventExtra">

`love.forte.simbot.kook.event.DeletedReactionEventExtra`

事件类型名: `"deleted_reaction"`

<a ignore-vars="true" href="https://developer.kookapp.cn/doc/event/channel#%E9%A2%91%E9%81%93%E5%86%85%E7%94%A8%E6%88%B7%E5%8F%96%E6%B6%88%20reaction">频道内用户取消 reaction</a>

</def>
<def title="UpdatedMessageEventExtra" id="love_forte_simbot_kook_event_UpdatedMessageEventExtra">

`love.forte.simbot.kook.event.UpdatedMessageEventExtra`

事件类型名: `"updated_message"`

<a ignore-vars="true" href="https://developer.kookapp.cn/doc/event/channel#%E9%A2%91%E9%81%93%E6%B6%88%E6%81%AF%E6%9B%B4%E6%96%B0">频道消息更新</a>

</def>
<def title="DeletedMessageEventExtra" id="love_forte_simbot_kook_event_DeletedMessageEventExtra">

`love.forte.simbot.kook.event.DeletedMessageEventExtra`

事件类型名: `"deleted_message"`

<a ignore-vars="true" href="https://developer.kookapp.cn/doc/event/channel#%E9%A2%91%E9%81%93%E6%B6%88%E6%81%AF%E8%A2%AB%E5%88%A0%E9%99%A4">频道消息被删除</a>

</def>
<def title="PinnedMessageEventExtra" id="love_forte_simbot_kook_event_PinnedMessageEventExtra">

`love.forte.simbot.kook.event.PinnedMessageEventExtra`

事件类型名: `"pinned_message"`

<a ignore-vars="true" href="https://developer.kookapp.cn/doc/event/channel#%E6%96%B0%E7%9A%84%E9%A2%91%E9%81%93%E7%BD%AE%E9%A1%B6%E6%B6%88%E6%81%AF">新的频道置顶消息</a>

</def>
<def title="UnpinnedMessageEventExtra" id="love_forte_simbot_kook_event_UnpinnedMessageEventExtra">

`love.forte.simbot.kook.event.UnpinnedMessageEventExtra`

事件类型名: `"unpinned_message"`

<a ignore-vars="true" href="https://developer.kookapp.cn/doc/event/channel#%E5%8F%96%E6%B6%88%E9%A2%91%E9%81%93%E7%BD%AE%E9%A1%B6%E6%B6%88%E6%81%AF">取消频道置顶消息</a>

</def>
<def title="EventExtra" id="love_forte_simbot_kook_event_EventExtra">

`love.forte.simbot.kook.event.EventExtra`

事件的消息 `extra`。

**`UnknownExtra` **

`UnknownExtra` 与其他子类型有所不同。 `UnknownExtra` 是由框架定义并实现的特殊类型，
它用来承载那些接收后无法被解析或尚未支持的事件类型。

</def>
<def title="TextExtra" id="love_forte_simbot_kook_event_TextExtra">

`love.forte.simbot.kook.event.TextExtra`

事件类型名: `"text"`

文字频道消息 `extra`
> 当 [`type`] `Event.typeValue` 非系统消息(255)时
当此事件的频道类型 `Event.channelType` 为 `Event.ChannelType.PERSON` 时，例如 `guildId` 等频道才有的属性可能会使用空内容填充。

</def>
<def title="TextEventExtra" id="love_forte_simbot_kook_event_TextEventExtra">

`love.forte.simbot.kook.event.TextEventExtra`

<a ignore-vars="true" href="https://developer.kookapp.cn/doc/event/message#%E6%96%87%E5%AD%97%E6%B6%88%E6%81%AF">文字消息事件 extra</a>

</def>
<def title="ImageEventExtra" id="love_forte_simbot_kook_event_ImageEventExtra">

`love.forte.simbot.kook.event.ImageEventExtra`

<a ignore-vars="true" href="https://developer.kookapp.cn/doc/event/message#%E5%9B%BE%E7%89%87%E6%B6%88%E6%81%AF">图片消息事件 extra</a>

</def>
<def title="VideoEventExtra" id="love_forte_simbot_kook_event_VideoEventExtra">

`love.forte.simbot.kook.event.VideoEventExtra`

<a ignore-vars="true" href="https://developer.kookapp.cn/doc/event/message#%E8%A7%86%E9%A2%91%E6%B6%88%E6%81%AF">视频消息事件 extra</a>

</def>
<def title="KMarkdownEventExtra" id="love_forte_simbot_kook_event_KMarkdownEventExtra">

`love.forte.simbot.kook.event.KMarkdownEventExtra`

<a ignore-vars="true" href="https://developer.kookapp.cn/doc/event/message#KMarkdown%20%E6%B6%88%E6%81%AF">KMarkdown 消息事件 extra</a>

</def>
<def title="CardEventExtra" id="love_forte_simbot_kook_event_CardEventExtra">

`love.forte.simbot.kook.event.CardEventExtra`

<a ignore-vars="true" href="https://developer.kookapp.cn/doc/event/message#Card%20%E6%B6%88%E6%81%AF">Card 消息事件 extra</a>

</def>
<def title="SystemExtra" id="love_forte_simbot_kook_event_SystemExtra">

`love.forte.simbot.kook.event.SystemExtra`

事件类型名: `"sys"`

系统事件消息 `extra`
> 当 [`type`] `Event.typeValue` 为系统消息(255)时

</def>
<def title="UnknownExtra" id="love_forte_simbot_kook_event_UnknownExtra">

`love.forte.simbot.kook.event.UnknownExtra`

事件类型名: `"$$UNKNOWN"`

当一个事件反序列化失败的时候，会被**尝试**使用 `UnknownExtra` 作为 `extra` 的序列化目标。
如果是因为一个未知的事件导致的这次失败，则 `UnknownExtra` 便会反序列化成功并被推送。
`UnknownExtra` 不会提供任何可反序列化的属性，
取而代之的是提供了 `source` 来获取本次反序列化失败的的原始JSON字符串信息。
你可以通过 `source` 来做一些临时性处理，例如解析并获取其中的信息。

**FragileSimbotApi**

`UnknownExtra` 类型的事件会随着支持的事件类型的增多而减少。
对可能造成 `UnknownExtra` 出现概率降低的更新不会做专门的提示。
因此使用 `UnknownExtra` 时应当明确了解其可能出现的内容，同时不可过分依赖它。

</def>
<def title="UpdateGuildEventExtra" id="love_forte_simbot_kook_event_UpdateGuildEventExtra">

`love.forte.simbot.kook.event.UpdateGuildEventExtra`

事件类型名: `"updated_guild"`

<a ignore-vars="true" href="https://developer.kookapp.cn/doc/event/guild#%E6%9C%8D%E5%8A%A1%E5%99%A8%E4%BF%A1%E6%81%AF%E6%9B%B4%E6%96%B0">服务器信息更新</a>

</def>
<def title="DeleteGuildEventExtra" id="love_forte_simbot_kook_event_DeleteGuildEventExtra">

`love.forte.simbot.kook.event.DeleteGuildEventExtra`

事件类型名: `"deleted_guild"`

<a ignore-vars="true" href="https://developer.kookapp.cn/doc/event/guild#%E6%9C%8D%E5%8A%A1%E5%99%A8%E5%88%A0%E9%99%A4">服务器删除</a>

</def>
<def title="AddBlockListEventExtra" id="love_forte_simbot_kook_event_AddBlockListEventExtra">

`love.forte.simbot.kook.event.AddBlockListEventExtra`

事件类型名: `"added_block_list"`

<a ignore-vars="true" href="https://developer.kookapp.cn/doc/event/guild#%E6%9C%8D%E5%8A%A1%E5%99%A8%E5%B0%81%E7%A6%81%E7%94%A8%E6%88%B7">服务器封禁用户</a>

</def>
<def title="DeleteBlockListEventExtra" id="love_forte_simbot_kook_event_DeleteBlockListEventExtra">

`love.forte.simbot.kook.event.DeleteBlockListEventExtra`

事件类型名: `"deleted_block_list"`

<a ignore-vars="true" href="https://developer.kookapp.cn/doc/event/guild#%E6%9C%8D%E5%8A%A1%E5%99%A8%E5%8F%96%E6%B6%88%E5%B0%81%E7%A6%81%E7%94%A8%E6%88%B7">服务器取消封禁用户</a>

</def>
<def title="AddedEmojiEventExtra" id="love_forte_simbot_kook_event_AddedEmojiEventExtra">

`love.forte.simbot.kook.event.AddedEmojiEventExtra`

事件类型名: `"added_emoji"`

<a ignore-vars="true" href="https://developer.kookapp.cn/doc/event/guild#%E6%9C%8D%E5%8A%A1%E5%99%A8%E6%B7%BB%E5%8A%A0%E6%96%B0%E8%A1%A8%E6%83%85">服务器添加新表情</a>

</def>
<def title="RemovedEmojiEventExtra" id="love_forte_simbot_kook_event_RemovedEmojiEventExtra">

`love.forte.simbot.kook.event.RemovedEmojiEventExtra`

事件类型名: `"removed_emoji"`

<a ignore-vars="true" href="https://developer.kookapp.cn/doc/event/guild#%E6%9C%8D%E5%8A%A1%E5%99%A8%E5%88%A0%E9%99%A4%E8%A1%A8%E6%83%85">服务器删除表情</a>

</def>
<def title="UpdatedEmojiEventExtra" id="love_forte_simbot_kook_event_UpdatedEmojiEventExtra">

`love.forte.simbot.kook.event.UpdatedEmojiEventExtra`

事件类型名: `"updated_emoji"`

<a ignore-vars="true" href="https://developer.kookapp.cn/doc/event/guild#%E6%9C%8D%E5%8A%A1%E5%99%A8%E6%9B%B4%E6%96%B0%E8%A1%A8%E6%83%85">服务器更新表情</a>

</def>
<def title="JoinedGuildEventExtra" id="love_forte_simbot_kook_event_JoinedGuildEventExtra">

`love.forte.simbot.kook.event.JoinedGuildEventExtra`

事件类型名: `"joined_guild"`

<a ignore-vars="true" href="https://developer.kookapp.cn/doc/event/guild-member#%E6%96%B0%E6%88%90%E5%91%98%E5%8A%A0%E5%85%A5%E6%9C%8D%E5%8A%A1%E5%99%A8">新成员加入服务器</a>

</def>
<def title="ExitedGuildEventExtra" id="love_forte_simbot_kook_event_ExitedGuildEventExtra">

`love.forte.simbot.kook.event.ExitedGuildEventExtra`

事件类型名: `"exited_guild"`

<a ignore-vars="true" href="https://developer.kookapp.cn/doc/event/guild-member#%E6%9C%8D%E5%8A%A1%E5%99%A8%E6%88%90%E5%91%98%E9%80%80%E5%87%BA">服务器成员退出</a>

</def>
<def title="UpdatedGuildMemberEventExtra" id="love_forte_simbot_kook_event_UpdatedGuildMemberEventExtra">

`love.forte.simbot.kook.event.UpdatedGuildMemberEventExtra`

事件类型名: `"updated_guild_member"`

<a ignore-vars="true" href="https://developer.kookapp.cn/doc/event/guild-member#%E6%9C%8D%E5%8A%A1%E5%99%A8%E6%88%90%E5%91%98%E4%BF%A1%E6%81%AF%E6%9B%B4%E6%96%B0">服务器成员信息更新</a>

</def>
<def title="GuildMemberOnlineEventExtra" id="love_forte_simbot_kook_event_GuildMemberOnlineEventExtra">

`love.forte.simbot.kook.event.GuildMemberOnlineEventExtra`

事件类型名: `"guild_member_online"`

<a ignore-vars="true" href="https://developer.kookapp.cn/doc/event/guild-member#%E6%9C%8D%E5%8A%A1%E5%99%A8%E6%88%90%E5%91%98%E4%B8%8A%E7%BA%BF">服务器成员上线</a>

</def>
<def title="GuildMemberOfflineEventExtra" id="love_forte_simbot_kook_event_GuildMemberOfflineEventExtra">

`love.forte.simbot.kook.event.GuildMemberOfflineEventExtra`

事件类型名: `"guild_member_offline"`

<a ignore-vars="true" href="https://developer.kookapp.cn/doc/event/guild-member#%E6%9C%8D%E5%8A%A1%E5%99%A8%E6%88%90%E5%91%98%E4%B8%8B%E7%BA%BF">服务器成员下线</a>

</def>
<def title="UpdatedPrivateMessageEventExtra" id="love_forte_simbot_kook_event_UpdatedPrivateMessageEventExtra">

`love.forte.simbot.kook.event.UpdatedPrivateMessageEventExtra`

事件类型名: `"updated_private_message"`

<a ignore-vars="true" href="https://developer.kookapp.cn/doc/event/direct-message#%E7%A7%81%E8%81%8A%E6%B6%88%E6%81%AF%E6%9B%B4%E6%96%B0">私聊消息更新</a>

</def>
<def title="DeletedPrivateMessageEventExtra" id="love_forte_simbot_kook_event_DeletedPrivateMessageEventExtra">

`love.forte.simbot.kook.event.DeletedPrivateMessageEventExtra`

事件类型名: `"deleted_private_message"`

<a ignore-vars="true" href="https://developer.kookapp.cn/doc/event/direct-message#%E7%A7%81%E8%81%8A%E6%B6%88%E6%81%AF%E8%A2%AB%E5%88%A0%E9%99%A4">私聊消息删除</a>

</def>
<def title="AddedRoleEventExtra" id="love_forte_simbot_kook_event_AddedRoleEventExtra">

`love.forte.simbot.kook.event.AddedRoleEventExtra`

事件类型名: `"added_role"`

<a ignore-vars="true" href="https://developer.kookapp.cn/doc/event/guild-role#%E6%9C%8D%E5%8A%A1%E5%99%A8%E8%A7%92%E8%89%B2%E5%A2%9E%E5%8A%A0">服务器角色增加</a>

</def>
<def title="DeletedRoleEventExtra" id="love_forte_simbot_kook_event_DeletedRoleEventExtra">

`love.forte.simbot.kook.event.DeletedRoleEventExtra`

事件类型名: `"deleted_role"`

<a ignore-vars="true" href="https://developer.kookapp.cn/doc/event/guild-role#%E6%9C%8D%E5%8A%A1%E5%99%A8%E8%A7%92%E8%89%B2%E5%88%A0%E9%99%A4">服务器角色删除</a>

</def>
<def title="UpdatedRoleEventExtra" id="love_forte_simbot_kook_event_UpdatedRoleEventExtra">

`love.forte.simbot.kook.event.UpdatedRoleEventExtra`

事件类型名: `"updated_role"`

<a ignore-vars="true" href="https://developer.kookapp.cn/doc/event/guild-role#%E6%9C%8D%E5%8A%A1%E5%99%A8%E8%A7%92%E8%89%B2%E6%9B%B4%E6%96%B0">服务器角色更新</a>

</def>
<def title="JoinedChannelEventExtra" id="love_forte_simbot_kook_event_JoinedChannelEventExtra">

`love.forte.simbot.kook.event.JoinedChannelEventExtra`

事件类型名: `"joined_channel"`

<a ignore-vars="true" href="https://developer.kookapp.cn/doc/event/user#%E7%94%A8%E6%88%B7%E5%8A%A0%E5%85%A5%E8%AF%AD%E9%9F%B3%E9%A2%91%E9%81%93">用户加入语音频道</a>

</def>
<def title="ExitedChannelEventExtra" id="love_forte_simbot_kook_event_ExitedChannelEventExtra">

`love.forte.simbot.kook.event.ExitedChannelEventExtra`

事件类型名: `"exited_channel"`

<a ignore-vars="true" href="https://developer.kookapp.cn/doc/event/user#%E7%94%A8%E6%88%B7%E9%80%80%E5%87%BA%E8%AF%AD%E9%9F%B3%E9%A2%91%E9%81%93">用户退出语音频道</a>

</def>
<def title="UserUpdatedEventExtra" id="love_forte_simbot_kook_event_UserUpdatedEventExtra">

`love.forte.simbot.kook.event.UserUpdatedEventExtra`

事件类型名: `"user_updated"`

<a ignore-vars="true" href="https://developer.kookapp.cn/doc/event/user#%E7%94%A8%E6%88%B7%E4%BF%A1%E6%81%AF%E6%9B%B4%E6%96%B0">用户信息更新</a>
该事件与服务器无关, 遵循以下条件
- 仅当用户的 用户名 或 头像 变更时;
- 仅通知与该用户存在关联的用户或 Bot: a. 存在聊天会话 b. 双方好友关系

</def>

</deflist>

## Simbot 标准库 Event 实现

使用核心库，可以在 simbot4 的 `Application` 或 Spring Boot 中使用这些事件类型实现。

核心模块所有的 simbot Event 实现类型定义都在包 `love.forte.simbot.component.kook.event` 中。

所有实现类型均继承 `love.forte.simbot.component.kook.event.KookEvent`。

> KOOK的 simbot 事件实现会根据含义，选择性的实现一些特定的类型。
> 举个例子，`KookChannelMessageEvent` 可以代表“子频道消息事件”，
> 因此它实现了 `ChatChannelMessageEvent`。

<deflist>
<def title="KookEvent" id="love_forte_simbot_component_kook_event_KookEvent">

`love.forte.simbot.component.kook.event.KookEvent`

Kook 组件的事件类型基类。

</def>
<def title="KookBotEvent" id="love_forte_simbot_component_kook_event_KookBotEvent">

`love.forte.simbot.component.kook.event.KookBotEvent`

`KookEvent` 下实现 `BotEvent` 的基础类型。

</def>
<def title="KookSystemEvent" id="love_forte_simbot_component_kook_event_KookSystemEvent">

`love.forte.simbot.component.kook.event.KookSystemEvent`

`KookBotEvent` 的 **系统事件** 相关的事件基类。

</def>
<def title="KookBotRegisteredEvent" id="love_forte_simbot_component_kook_event_KookBotRegisteredEvent">

`love.forte.simbot.component.kook.event.KookBotRegisteredEvent`

当一个 `KookBot` 在 `KookBotManager` 中被_注册_时。

</def>
<def title="KookBotStartedEvent" id="love_forte_simbot_component_kook_event_KookBotStartedEvent">

`love.forte.simbot.component.kook.event.KookBotStartedEvent`

`KookBot` 执行 `start`  `KookBot.start` 之后推送的事件。

</def>
<def title="KookChannelChangedEvent" id="love_forte_simbot_component_kook_event_KookChannelChangedEvent">

`love.forte.simbot.component.kook.event.KookChannelChangedEvent`

KOOK 系统事件中与 _频道变更_ 相关的事件的simbot事件基准类。
涉及的 KOOK 原始事件 (的 `SystemExtra` 子类型) 有：
- `AddedChannelEventExtra`
- `UpdatedChannelEventExtra`
- `DeletedChannelEventExtra`

</def>
<def title="KookAddedChannelEvent" id="love_forte_simbot_component_kook_event_KookAddedChannelEvent">

`love.forte.simbot.component.kook.event.KookAddedChannelEvent`

某频道服务器中新增了一个频道后的事件。

</def>
<def title="KookUpdatedChannelEvent" id="love_forte_simbot_component_kook_event_KookUpdatedChannelEvent">

`love.forte.simbot.component.kook.event.KookUpdatedChannelEvent`

某频道发生了信息变更。

</def>
<def title="KookDeletedChannelEvent" id="love_forte_simbot_component_kook_event_KookDeletedChannelEvent">

`love.forte.simbot.component.kook.event.KookDeletedChannelEvent`

某频道被删除的事件。

</def>
<def title="KookCategoryChangedEvent" id="love_forte_simbot_component_kook_event_KookCategoryChangedEvent">

`love.forte.simbot.component.kook.event.KookCategoryChangedEvent`

KOOK 系统事件中与 _频道分组变更_ 相关的事件的simbot事件基准类。
涉及的 KOOK 原始事件 (的 `SystemExtra` 子类型) 有：
- `AddedChannelEventExtra`
- `UpdatedChannelEventExtra`
- `DeletedChannelEventExtra`

</def>
<def title="KookAddedCategoryEvent" id="love_forte_simbot_component_kook_event_KookAddedCategoryEvent">

`love.forte.simbot.component.kook.event.KookAddedCategoryEvent`

某频道服务器中新增了一个频道分组后的事件。

</def>
<def title="KookUpdatedCategoryEvent" id="love_forte_simbot_component_kook_event_KookUpdatedCategoryEvent">

`love.forte.simbot.component.kook.event.KookUpdatedCategoryEvent`

某频道分组发生了信息变更。

</def>
<def title="KookDeletedCategoryEvent" id="love_forte_simbot_component_kook_event_KookDeletedCategoryEvent">

`love.forte.simbot.component.kook.event.KookDeletedCategoryEvent`

某频道分组被删除的事件。

</def>
<def title="KookDeletedMessageEvent" id="love_forte_simbot_component_kook_event_KookDeletedMessageEvent">

`love.forte.simbot.component.kook.event.KookDeletedMessageEvent`

KOOK 系统事件中与 _消息删除_ 相关的事件的simbot事件基准类。
涉及的 KOOK 原始事件 (的 `SystemExtra` 子类型) 有：
- `DeletedMessageEventExtra`
- `DeletedPrivateMessageEventExtra`

</def>
<def title="KookDeletedChannelMessageEvent" id="love_forte_simbot_component_kook_event_KookDeletedChannelMessageEvent">

`love.forte.simbot.component.kook.event.KookDeletedChannelMessageEvent`

KOOK中一个频道消息被删除的事件。

</def>
<def title="KookDeletedPrivateMessageEvent" id="love_forte_simbot_component_kook_event_KookDeletedPrivateMessageEvent">

`love.forte.simbot.component.kook.event.KookDeletedPrivateMessageEvent`

KOOK中一个私聊消息被删除的事件。

</def>
<def title="KookMemberChangedEvent" id="love_forte_simbot_component_kook_event_KookMemberChangedEvent">

`love.forte.simbot.component.kook.event.KookMemberChangedEvent`

KOOK 的频道成员变更事件。
相关的 KOOK **原始**事件类型有：
- `ExitedChannelEventExtra`
- `JoinedChannelEventExtra`
- `JoinedGuildEventExtra`
- `ExitedGuildEventExtra`
- `UpdatedGuildMemberEventExtra`
- `SelfExitedGuildEventExtra`
- `SelfJoinedGuildEventExtra`
  其中， `SelfExitedGuildEventExtra` 和 `SelfJoinedGuildEventExtra`
  代表为 BOT 自身作为成员的变动，
  因此会额外提供相对应的 [bot成员变动] `KookBotMemberChangedEvent`
  事件类型来进行更精准的事件监听。

**相关事件**


**成员的频道变更事件**

`KookMemberChannelChangedEvent` 事件及其子类型
`KookMemberExitedChannelEvent` 、 `KookMemberJoinedChannelEvent`
代表了一个频道服务器中的某个群成员加入、离开某一个频道（通常为语音频道）的事件。

**成员的服务器变更事件**

`KookMemberGuildChangedEvent` 事件及其子类型
`KookMemberJoinedGuildEvent` 、 `KookMemberExitedGuildEvent`
代表了一个频道服务器中有新群成员加入、旧成员离开此服务器的事件。

**成员的信息变更事件**

`KookMemberUpdatedEvent` 事件
代表了一个成员的信息发生了变更的事件。

**Bot频道服务器事件**

`KookBotMemberChangedEvent` 事件及其子类型
`KookBotSelfJoinedGuildEvent` 、 `KookBotSelfExitedGuildEvent`
代表了当前bot加入新频道服务器、离开旧频道服务器的事件。

</def>
<def title="KookMemberChannelChangedEvent" id="love_forte_simbot_component_kook_event_KookMemberChannelChangedEvent">

`love.forte.simbot.component.kook.event.KookMemberChannelChangedEvent`

KOOK [成员变更事件] `KookMemberChangedEvent` 中与**语音频道的进出**相关的变更事件。
这类事件代表某人进入、离开某个语音频道 (`channel`)，而不代表成员进入、离开了当前的频道服务器（`guild`）。

</def>
<def title="KookMemberJoinedChannelEvent" id="love_forte_simbot_component_kook_event_KookMemberJoinedChannelEvent">

`love.forte.simbot.component.kook.event.KookMemberJoinedChannelEvent`

KOOK 成员加入(语音频道)事件。

</def>
<def title="KookMemberExitedChannelEvent" id="love_forte_simbot_component_kook_event_KookMemberExitedChannelEvent">

`love.forte.simbot.component.kook.event.KookMemberExitedChannelEvent`

KOOK 成员离开(语音频道)事件。

</def>
<def title="KookMemberGuildChangedEvent" id="love_forte_simbot_component_kook_event_KookMemberGuildChangedEvent">

`love.forte.simbot.component.kook.event.KookMemberGuildChangedEvent`

KOOK [成员变更事件] `KookMemberChangedEvent` 中与**频道服务器进出**相关的变更事件。
这类事件代表某人加入、离开某个频道服务器。

</def>
<def title="KookMemberExitedGuildEvent" id="love_forte_simbot_component_kook_event_KookMemberExitedGuildEvent">

`love.forte.simbot.component.kook.event.KookMemberExitedGuildEvent`

KOOK 成员离开(频道)事件。

</def>
<def title="KookMemberJoinedGuildEvent" id="love_forte_simbot_component_kook_event_KookMemberJoinedGuildEvent">

`love.forte.simbot.component.kook.event.KookMemberJoinedGuildEvent`

KOOK 成员加入(频道)事件。

</def>
<def title="KookMemberUpdatedEvent" id="love_forte_simbot_component_kook_event_KookMemberUpdatedEvent">

`love.forte.simbot.component.kook.event.KookMemberUpdatedEvent`

KOOK 频道成员信息更新事件。

</def>
<def title="KookBotMemberChangedEvent" id="love_forte_simbot_component_kook_event_KookBotMemberChangedEvent">

`love.forte.simbot.component.kook.event.KookBotMemberChangedEvent`

频道成员的变动事件中，变动本体为bot自身时的事件。
对应 KOOK 原始事件的 `SelfExitedGuildEventExtra` 和 `SelfJoinedGuildEventExtra` 。

</def>
<def title="KookBotSelfExitedGuildEvent" id="love_forte_simbot_component_kook_event_KookBotSelfExitedGuildEvent">

`love.forte.simbot.component.kook.event.KookBotSelfExitedGuildEvent`

KOOK BOT自身离开(频道)事件。

</def>
<def title="KookBotSelfJoinedGuildEvent" id="love_forte_simbot_component_kook_event_KookBotSelfJoinedGuildEvent">

`love.forte.simbot.component.kook.event.KookBotSelfJoinedGuildEvent`

KOOK BOT自身加入(频道)事件。

</def>
<def title="KookUserOnlineStatusChangedEvent" id="love_forte_simbot_component_kook_event_KookUserOnlineStatusChangedEvent">

`love.forte.simbot.component.kook.event.KookUserOnlineStatusChangedEvent`

KOOK 用户在线状态变更相关事件的抽象父类。
涉及到的原始事件有：
- `GuildMemberOfflineEventExtra`
- `GuildMemberOnlineEventExtra`

**变化主体**

此事件主体是事件中的 [用户ID] `GuildMemberOnlineStatusChangedEventBody.userId`

**子类型**

此事件是密封的，如果你只想监听某人的上线或下线中的其中一种事件，则考虑监听此事件类的具体子类型。

</def>
<def title="KookMemberOnlineEvent" id="love_forte_simbot_component_kook_event_KookMemberOnlineEvent">

`love.forte.simbot.component.kook.event.KookMemberOnlineEvent`

`KookUserOnlineStatusChangedEvent` 对于用户上线的事件子类型。

</def>
<def title="KookMemberOfflineEvent" id="love_forte_simbot_component_kook_event_KookMemberOfflineEvent">

`love.forte.simbot.component.kook.event.KookMemberOfflineEvent`

`KookUserOnlineStatusChangedEvent` 对于用户离线的事件子类型。

</def>
<def title="KookMessageBtnClickEvent" id="love_forte_simbot_component_kook_event_KookMessageBtnClickEvent">

`love.forte.simbot.component.kook.event.KookMessageBtnClickEvent`

一个 `Card` 中的按钮被按下的事件。

</def>
<def title="KookMessageEvent" id="love_forte_simbot_component_kook_event_KookMessageEvent">

`love.forte.simbot.component.kook.event.KookMessageEvent`

KOOK 中与消息相关的事件, 即当 `KEvent.extra` 类型为 `TextExtra` 时所触发的事件。
大部分消息事件都可能由同一个格式衍生为两种类型：私聊与群聊（频道消息），
这由 `KEvent.channelType` 所决定。当 `KEvent.channelType`
值为 `KEvent.ChannelType.GROUP` 时则代表为 [频道消息事件] `ChatChannelMessageEvent` ，
而如果为  `KEvent.ChannelType.PERSON` 则代表为
[联系人消息事件] `ContactMessageEvent` 。

**来源**

KOOK 的消息推送同样会推送bot自己所发送的消息。在stdlib模块下，
你可能需要自己手动处理对于消息来自bot自身的情况。
但是在当前组件下， `KookMessageEvent` 中:
- 来自其他人的事件: `KookChannelMessageEvent` , `KookContactMessageEvent`
- 来自bot自己的事件: `KookBotSelfChannelMessageEvent` , `KookBotSelfMessageEvent`

</def>
<def title="KookChannelMessageEvent" id="love_forte_simbot_component_kook_event_KookChannelMessageEvent">

`love.forte.simbot.component.kook.event.KookChannelMessageEvent`

Kook 普通频道消息事件。即来自bot以外的人发送的消息的类型。
此事件只会由 bot 自身以外的人触发。

</def>
<def title="KookContactMessageEvent" id="love_forte_simbot_component_kook_event_KookContactMessageEvent">

`love.forte.simbot.component.kook.event.KookContactMessageEvent`

Kook 普通私聊消息事件。即来自bot以外的人发送的消息的类型。
此事件只会由 bot 以外的人触发。

</def>
<def title="KookBotSelfChannelMessageEvent" id="love_forte_simbot_component_kook_event_KookBotSelfChannelMessageEvent">

`love.forte.simbot.component.kook.event.KookBotSelfChannelMessageEvent`

Kook bot频道消息事件。即来自bot自身发送的消息的类型。
此事件只会由 bot 自身触发。

</def>
<def title="KookBotSelfMessageEvent" id="love_forte_simbot_component_kook_event_KookBotSelfMessageEvent">

`love.forte.simbot.component.kook.event.KookBotSelfMessageEvent`

私聊消息事件。
此事件只会由 bot 自身触发，代表bot在私聊会话中发出的消息。

</def>
<def title="KookMessagePinEvent" id="love_forte_simbot_component_kook_event_KookMessagePinEvent">

`love.forte.simbot.component.kook.event.KookMessagePinEvent`

与频道消息置顶相关的事件。
涉及的原始事件有：
- `PinnedMessageEventExtra`
- `UnpinnedMessageEventExtra`

</def>
<def title="KookPinnedMessageEvent" id="love_forte_simbot_component_kook_event_KookPinnedMessageEvent">

`love.forte.simbot.component.kook.event.KookPinnedMessageEvent`

新消息置顶事件。
代表一个新的消息被设置为了目标频道的置顶消息。

</def>
<def title="KookUnpinnedMessageEvent" id="love_forte_simbot_component_kook_event_KookUnpinnedMessageEvent">

`love.forte.simbot.component.kook.event.KookUnpinnedMessageEvent`

消息取消置顶事件。代表一个新的消息被设置为了目标频道的置顶消息。

</def>
<def title="KookUpdatedMessageEvent" id="love_forte_simbot_component_kook_event_KookUpdatedMessageEvent">

`love.forte.simbot.component.kook.event.KookUpdatedMessageEvent`

KOOK 系统事件中与 _消息更新_ 相关的事件的simbot事件基准类。
涉及的 KOOK 原始事件 (的 `SystemExtra` 子类型) 有：
- `UpdatedMessageEventExtra`
- `UpdatedPrivateMessageEventExtra`

</def>
<def title="KookUpdatedChannelMessageEvent" id="love_forte_simbot_component_kook_event_KookUpdatedChannelMessageEvent">

`love.forte.simbot.component.kook.event.KookUpdatedChannelMessageEvent`

KOOK中一个频道消息被更新的事件。

</def>
<def title="KookUpdatedPrivateMessageEvent" id="love_forte_simbot_component_kook_event_KookUpdatedPrivateMessageEvent">

`love.forte.simbot.component.kook.event.KookUpdatedPrivateMessageEvent`

KOOK中一个私聊消息被更新的事件。

</def>
<def title="KookUserUpdatedEvent" id="love_forte_simbot_component_kook_event_KookUserUpdatedEvent">

`love.forte.simbot.component.kook.event.KookUserUpdatedEvent`

Kook 用户信息更新事件。
此事件属于一个 `ChangeEvent` ,
`ChangeEvent.content` 为用户变更事件的内容本体，
即 `sourceBody` 。
此事件不一定是某个具体频道服务器中的用户，
只要有好友关系即会推送。

</def>
<def title="UnsupportedKookEvent" id="love_forte_simbot_component_kook_event_UnsupportedKookEvent">

`love.forte.simbot.component.kook.event.UnsupportedKookEvent`

所有未提供针对性实现的其他 KOOK 事件。
`UnsupportedKookEvent` 不实现任何其他事件类型，
仅实现 KOOK 组件中的事件父类型 `KookBotEvent` ，是一个完全独立的事件类型。
`UnsupportedKookEvent` 会将所有 **尚未支持** 的事件通过此类型进行推送。
如果要监听 `UnsupportedKookEvent` , 你需要谨慎处理其中的一切，
因为 `UnsupportedKookEvent` 能够提供的事件会随着当前组件支持的特定事件的增多而减少，
这种减少可能会伴随着版本更新而产生，且可能不会有任何说明或错误提示。
因此你应当首先查看 `KookBotEvent` 下是否有所需的已经实现的事件类型，并且不应当过分依赖 `UnsupportedKookEvent` .

**`UnknownExtra` **

[`sourceEvent.extra`] `Event.extra` 中（理所应当地）有可能会出现 `UnknownExtra` 。
`UnknownExtra` 的含义与其他 `EventExtra` 的含义略有区别。详细说明可参考 `UnknownExtra` 的文档描述。

</def>

</deflist>
