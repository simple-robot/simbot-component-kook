# API定义列表

此处会列举 `API 模块` 中、`love.forte.simbot.kook.api` 包下定义的所有 `KookApi` 实现。

> 对于一个具体的API的详细说明，我们建议你前往 [API 文档](https://docs.simbot.forte.love/) 或源码注释查阅，
> 因为那是最贴合真实情况且最全面的。
> 
> 此处的列表是自动生成的，会不定期更新。

<deflist>
<def title="GetGatewayApi">

`love.forte.simbot.kook.api.GetGatewayApi`

<a ignore-vars="true" href="https://developer.kaiheila.cn/doc/http/gateway">获取网关连接地址</a>。




<deflist>
<def title="Compress">

`love.forte.simbot.kook.api.GetGatewayApi.Compress`

压缩数据


</def>
<def title="NotCompress">

`love.forte.simbot.kook.api.GetGatewayApi.NotCompress`

不进行数据压缩


</def>
<def title="Resume">

`love.forte.simbot.kook.api.GetGatewayApi.Resume`

用于内部重连恢复的 `Resume` API。



</def>

</deflist>
</def>
<def title="CreateAssetApi">

`love.forte.simbot.kook.api.asset.CreateAssetApi`

<a ignore-vars="true" href="https://developer.kookapp.cn/doc/http/asset#%E4%B8%8A%E4%BC%A0%E5%AA%92%E4%BD%93%E6%96%87%E4%BB%B6">上传文件/图片</a>

与其他 API 实现不太一样的是, `CreateAssetApi.body` 每次获取都会构建一个新的实例。

> `Header` 中 `Content-Type` 必须为 `form-data`

在 JVM 平台中，还可以通过 `AssetApis.xxx` 使用更多平台特定的构建方式，
例如使用 `File` 或 `Path`。



</def>
<def title="CreateChannelApi">

`love.forte.simbot.kook.api.channel.CreateChannelApi`

<a ignore-vars="true" href="https://developer.kookapp.cn/doc/http/channel#%E5%88%9B%E5%BB%BA%E9%A2%91%E9%81%93">创建频道</a>



</def>
<def title="DeleteChannelApi">

`love.forte.simbot.kook.api.channel.DeleteChannelApi`

<a ignore-vars="true" href="https://developer.kookapp.cn/doc/http/channel#%E5%88%A0%E9%99%A4%E9%A2%91%E9%81%93">删除频道</a>




</def>
<def title="GetChannelListApi">

`love.forte.simbot.kook.api.channel.GetChannelListApi`

<a ignore-vars="true" href="https://developer.kookapp.cn/doc/http/channel#%E8%8E%B7%E5%8F%96%E9%A2%91%E9%81%93%E5%88%97%E8%A1%A8">获取频道列表</a>



</def>
<def title="GetChannelViewApi">

`love.forte.simbot.kook.api.channel.GetChannelViewApi`

<a ignore-vars="true" href="https://developer.kookapp.cn/doc/http/channel#%E8%8E%B7%E5%8F%96%E9%A2%91%E9%81%93%E8%AF%A6%E6%83%85">获取频道详情</a>



</def>
<def title="CreateGuildMuteApi">

`love.forte.simbot.kook.api.guild.CreateGuildMuteApi`

<a ignore-vars="true" href="https://developer.kookapp.cn/doc/http/guild#%E6%B7%BB%E5%8A%A0%E6%9C%8D%E5%8A%A1%E5%99%A8%E9%9D%99%E9%9F%B3%E6%88%96%E9%97%AD%E9%BA%A6">添加服务器静音或闭麦</a>



</def>
<def title="DeleteGuildMuteApi">

`love.forte.simbot.kook.api.guild.DeleteGuildMuteApi`

<a ignore-vars="true" href="https://developer.kookapp.cn/doc/http/guild#%E5%88%A0%E9%99%A4%E6%9C%8D%E5%8A%A1%E5%99%A8%E9%9D%99%E9%9F%B3%E6%88%96%E9%97%AD%E9%BA%A6">删除服务器静音或闭麦</a>



</def>
<def title="GetGuildBoostHistoryListApi">

`love.forte.simbot.kook.api.guild.GetGuildBoostHistoryListApi`

<a ignore-vars="true" href="https://developer.kookapp.cn/doc/http/guild#%E6%9C%8D%E5%8A%A1%E5%99%A8%E5%8A%A9%E5%8A%9B%E5%8E%86%E5%8F%B2">服务器助力历史</a>

> 需要有 `服务器管理` 权限



</def>
<def title="GetGuildListApi">

`love.forte.simbot.kook.api.guild.GetGuildListApi`

<a ignore-vars="true" href="https://developer.kookapp.cn/doc/http/guild#%E8%8E%B7%E5%8F%96%E5%BD%93%E5%89%8D%E7%94%A8%E6%88%B7%E5%8A%A0%E5%85%A5%E7%9A%84%E6%9C%8D%E5%8A%A1%E5%99%A8%E5%88%97%E8%A1%A8">获取当前用户加入的服务器列表</a>



</def>
<def title="GetGuildMuteListApi">

`love.forte.simbot.kook.api.guild.GetGuildMuteListApi`

<a ignore-vars="true" href="https://developer.kookapp.cn/doc/http/guild#%E6%9C%8D%E5%8A%A1%E5%99%A8%E9%9D%99%E9%9F%B3%E9%97%AD%E9%BA%A6%E5%88%97%E8%A1%A8">服务器静音闭麦列表</a>



</def>
<def title="GetGuildViewApi">

`love.forte.simbot.kook.api.guild.GetGuildViewApi`

<a ignore-vars="true" href="https://developer.kookapp.cn/doc/http/guild#%E8%8E%B7%E5%8F%96%E6%9C%8D%E5%8A%A1%E5%99%A8%E8%AF%A6%E6%83%85">获取服务器详情</a>



</def>
<def title="LeaveGuildApi">

`love.forte.simbot.kook.api.guild.LeaveGuildApi`

<a ignore-vars="true" href="https://developer.kookapp.cn/doc/http/guild#%E7%A6%BB%E5%BC%80%E6%9C%8D%E5%8A%A1%E5%99%A8">离开服务器</a>



</def>
<def title="CreateInviteApi">

`love.forte.simbot.kook.api.invite.CreateInviteApi`

<a ignore-vars="true" href="https://developer.kookapp.cn/doc/http/invite#%E5%88%9B%E5%BB%BA%E9%82%80%E8%AF%B7%E9%93%BE%E6%8E%A5">创建邀请链接</a>



</def>
<def title="DeleteInviteApi">

`love.forte.simbot.kook.api.invite.DeleteInviteApi`

<a ignore-vars="true" href="https://developer.kookapp.cn/doc/http/invite#%E5%88%A0%E9%99%A4%E9%82%80%E8%AF%B7%E9%93%BE%E6%8E%A5">删除邀请链接</a>



</def>
<def title="GetInviteListApi">

`love.forte.simbot.kook.api.invite.GetInviteListApi`

<a ignore-vars="true" href="https://developer.kookapp.cn/doc/http/invite#%E8%8E%B7%E5%8F%96%E9%82%80%E8%AF%B7%E5%88%97%E8%A1%A8">获取邀请列表</a>



</def>
<def title="GetGuildMemberListApi">

`love.forte.simbot.kook.api.member.GetGuildMemberListApi`

<a ignore-vars="true" href="https://developer.kookapp.cn/doc/http/guild#%E8%8E%B7%E5%8F%96%E6%9C%8D%E5%8A%A1%E5%99%A8%E4%B8%AD%E7%9A%84%E7%94%A8%E6%88%B7%E5%88%97%E8%A1%A8">获取服务器中的用户列表</a>



</def>
<def title="KickoutMemberApi">

`love.forte.simbot.kook.api.member.KickoutMemberApi`

<a ignore-vars="true" href="https://developer.kookapp.cn/doc/http/guild#%E8%B8%A2%E5%87%BA%E6%9C%8D%E5%8A%A1%E5%99%A8">踢出服务器</a>



</def>
<def title="ModifyMemberNicknameApi">

`love.forte.simbot.kook.api.member.ModifyMemberNicknameApi`

<a ignore-vars="true" href="https://developer.kookapp.cn/doc/http/guild#%E4%BF%AE%E6%94%B9%E6%9C%8D%E5%8A%A1%E5%99%A8%E4%B8%AD%E7%94%A8%E6%88%B7%E7%9A%84%E6%98%B5%E7%A7%B0">修改服务器中用户的昵称</a>



</def>
<def title="AddChannelReactionApi">

`love.forte.simbot.kook.api.message.AddChannelReactionApi`

<a ignore-vars="true" href="https://developer.kookapp.cn/doc/http/message#%E7%BB%99%E6%9F%90%E4%B8%AA%E6%B6%88%E6%81%AF%E6%B7%BB%E5%8A%A0%E5%9B%9E%E5%BA%94">给某个消息添加回应</a>



</def>
<def title="DeleteChannelMessageApi">

`love.forte.simbot.kook.api.message.DeleteChannelMessageApi`

<a ignore-vars="true" href="https://developer.kookapp.cn/doc/http/message#%E5%8F%91%E9%80%81%E9%A2%91%E9%81%93%E8%81%8A%E5%A4%A9%E6%B6%88%E6%81%AF">删除频道聊天消息</a>

> 普通用户只能删除自己的消息，有权限的用户可以删除权限范围内他人的消息。



</def>
<def title="DeleteChannelReactionApi">

`love.forte.simbot.kook.api.message.DeleteChannelReactionApi`

<a ignore-vars="true" href="https://developer.kookapp.cn/doc/http/message#%E5%88%A0%E9%99%A4%E6%B6%88%E6%81%AF%E7%9A%84%E6%9F%90%E4%B8%AA%E5%9B%9E%E5%BA%94">删除消息的某个回应</a>



</def>
<def title="DeleteDirectMessageApi">

`love.forte.simbot.kook.api.message.DeleteDirectMessageApi`

<a ignore-vars="true" href="https://developer.kookapp.cn/doc/http/direct-message#%E5%88%A0%E9%99%A4%E7%A7%81%E4%BF%A1%E8%81%8A%E5%A4%A9%E6%B6%88%E6%81%AF">删除私信聊天消息</a>

> 只能删除自己的消息。



</def>
<def title="GetChannelMessageListApi">

`love.forte.simbot.kook.api.message.GetChannelMessageListApi`

<a ignore-vars="true" href="https://developer.kookapp.cn/doc/http/message#%E8%8E%B7%E5%8F%96%E9%A2%91%E9%81%93%E8%81%8A%E5%A4%A9%E6%B6%88%E6%81%AF%E5%88%97%E8%A1%A8">获取频道聊天消息列表</a>

> 此接口非标准分页，需要根据参考消息来查询相邻分页的消息



</def>
<def title="GetChannelMessageReactorListApi">

`love.forte.simbot.kook.api.message.GetChannelMessageReactorListApi`

<a ignore-vars="true" href="https://developer.kookapp.cn/doc/http/message#%E8%8E%B7%E5%8F%96%E9%A2%91%E9%81%93%E6%B6%88%E6%81%AF%E6%9F%90%E5%9B%9E%E5%BA%94%E7%9A%84%E7%94%A8%E6%88%B7%E5%88%97%E8%A1%A8">获取频道消息某回应的用户列表</a>



</def>
<def title="GetChannelMessageViewApi">

`love.forte.simbot.kook.api.message.GetChannelMessageViewApi`

<a ignore-vars="true" href="https://developer.kookapp.cn/doc/http/message#%E8%8E%B7%E5%8F%96%E9%A2%91%E9%81%93%E8%81%8A%E5%A4%A9%E6%B6%88%E6%81%AF%E8%AF%A6%E6%83%85">获取频道聊天消息详情</a>



</def>
<def title="GetDirectMessageListApi">

`love.forte.simbot.kook.api.message.GetDirectMessageListApi`

<a ignore-vars="true" href="https://developer.kookapp.cn/doc/http/direct-message#%E8%8E%B7%E5%8F%96%E7%A7%81%E4%BF%A1%E8%81%8A%E5%A4%A9%E6%B6%88%E6%81%AF%E5%88%97%E8%A1%A8">获取私信聊天消息列表</a>



</def>
<def title="GetDirectMessageViewApi">

`love.forte.simbot.kook.api.message.GetDirectMessageViewApi`

<a ignore-vars="true" href="https://developer.kookapp.cn/doc/http/direct-message#%E8%8E%B7%E5%8F%96%E7%A7%81%E4%BF%A1%E8%81%8A%E5%A4%A9%E6%B6%88%E6%81%AF%E8%AF%A6%E6%83%85">获取私信聊天消息详情</a>



</def>
<def title="SendChannelMessageApi">

`love.forte.simbot.kook.api.message.SendChannelMessageApi`

<a ignore-vars="true" href="https://developer.kookapp.cn/doc/http/message#%E5%8F%91%E9%80%81%E9%A2%91%E9%81%93%E8%81%8A%E5%A4%A9%E6%B6%88%E6%81%AF">发送频道聊天消息</a>

> **接口说明**
> 注意：强烈建议过滤掉机器人发送的消息，再进行回应。
> 否则会很容易形成两个机器人循环自言自语导致发送量过大，进而导致机器人被封禁。如果确实需要机器人联动的情况，慎重进行处理，防止形成循环。
> 若发送的消息为诸如图片一类的资源，消息内容必须由机器人创建，否则会提示: "找不到资源"。



</def>
<def title="SendDirectMessageApi">

`love.forte.simbot.kook.api.message.SendDirectMessageApi`

<a ignore-vars="true" href="https://developer.kookapp.cn/doc/http/direct-message#%E5%8F%91%E9%80%81%E7%A7%81%E4%BF%A1%E8%81%8A%E5%A4%A9%E6%B6%88%E6%81%AF">发送私信聊天消息</a>



</def>
<def title="UpdateChannelMessageApi">

`love.forte.simbot.kook.api.message.UpdateChannelMessageApi`

<a ignore-vars="true" href="https://developer.kookapp.cn/doc/http/message#%E6%9B%B4%E6%96%B0%E9%A2%91%E9%81%93%E8%81%8A%E5%A4%A9%E6%B6%88%E6%81%AF">更新频道聊天消息</a>



</def>
<def title="UpdateDirectMessageApi">

`love.forte.simbot.kook.api.message.UpdateDirectMessageApi`

<a ignore-vars="true" href="https://developer.kookapp.cn/doc/http/direct-message#%E6%9B%B4%E6%96%B0%E7%A7%81%E4%BF%A1%E8%81%8A%E5%A4%A9%E6%B6%88%E6%81%AF">更新私信聊天消息</a>

> 目前支持消息 type为 `9`、`10` 的修改，即 `KMarkdown` 和 `CardMessage`



</def>
<def title="CreateGuildRoleApi">

`love.forte.simbot.kook.api.role.CreateGuildRoleApi`

<a ignore-vars="true" href="https://developer.kookapp.cn/doc/http/guild-role#%E5%88%9B%E5%BB%BA%E6%9C%8D%E5%8A%A1%E5%99%A8%E8%A7%92%E8%89%B2">创建服务器角色</a>



</def>
<def title="DeleteGuildRoleApi">

`love.forte.simbot.kook.api.role.DeleteGuildRoleApi`

<a ignore-vars="true" href="https://developer.kookapp.cn/doc/http/guild-role#%E5%88%A0%E9%99%A4%E6%9C%8D%E5%8A%A1%E5%99%A8%E8%A7%92%E8%89%B2">删除服务器角色</a>



</def>
<def title="GetGuildRoleListApi">

`love.forte.simbot.kook.api.role.GetGuildRoleListApi`

<a ignore-vars="true" href="https://developer.kookapp.cn/doc/http/guild-role#%E8%8E%B7%E5%8F%96%E6%9C%8D%E5%8A%A1%E5%99%A8%E8%A7%92%E8%89%B2%E5%88%97%E8%A1%A8">获取服务器角色列表</a>



</def>
<def title="GrantGuildRoleApi">

`love.forte.simbot.kook.api.role.GrantGuildRoleApi`

<a ignore-vars="true" href="https://developer.kookapp.cn/doc/http/guild-role#%E8%B5%8B%E4%BA%88%E7%94%A8%E6%88%B7%E8%A7%92%E8%89%B2">赋予用户角色</a>


</def>
<def title="RevokeGuildRoleApi">

`love.forte.simbot.kook.api.role.RevokeGuildRoleApi`

<a ignore-vars="true" href="https://developer.kookapp.cn/doc/http/guild-role#%E5%88%A0%E9%99%A4%E7%94%A8%E6%88%B7%E8%A7%92%E8%89%B2">删除用户角色</a>



</def>
<def title="UpdateGuildRoleApi">

`love.forte.simbot.kook.api.role.UpdateGuildRoleApi`

<a ignore-vars="true" href="https://developer.kaiheila.cn/doc/http/guild-role#更新服务器角色">更新服务器角色</a>



</def>
<def title="GetMeApi">

`love.forte.simbot.kook.api.user.GetMeApi`

<a ignore-vars="true" href="https://developer.kaiheila.cn/doc/http/user#%E8%8E%B7%E5%8F%96%E5%BD%93%E5%89%8D%E7%94%A8%E6%88%B7%E4%BF%A1%E6%81%AF">获取当前用户信息</a>



</def>
<def title="GetUserViewApi">

`love.forte.simbot.kook.api.user.GetUserViewApi`

<a ignore-vars="true" href="https://developer.kookapp.cn/doc/http/user#%E8%8E%B7%E5%8F%96%E7%9B%AE%E6%A0%87%E7%94%A8%E6%88%B7%E4%BF%A1%E6%81%AF">获取目标用户信息</a>



</def>
<def title="OfflineApi">

`love.forte.simbot.kook.api.user.OfflineApi`

<a ignore-vars="true" href="https://developer.kookapp.cn/doc/http/user#%E4%B8%8B%E7%BA%BF%E6%9C%BA%E5%99%A8%E4%BA%BA">下线机器人</a>



</def>
<def title="CreateUserChatApi">

`love.forte.simbot.kook.api.userchat.CreateUserChatApi`

<a ignore-vars="true" href="https://developer.kookapp.cn/doc/http/user-chat#%E5%88%9B%E5%BB%BA%E7%A7%81%E4%BF%A1%E8%81%8A%E5%A4%A9%E4%BC%9A%E8%AF%9D">创建私信聊天会话</a>



</def>
<def title="DeleteUserChatApi">

`love.forte.simbot.kook.api.userchat.DeleteUserChatApi`

<a ignore-vars="true" href="https://developer.kookapp.cn/doc/http/user-chat#%E5%88%A0%E9%99%A4%E7%A7%81%E4%BF%A1%E8%81%8A%E5%A4%A9%E4%BC%9A%E8%AF%9D">删除私信聊天会话</a>


</def>
<def title="GetUserChatListApi">

`love.forte.simbot.kook.api.userchat.GetUserChatListApi`

<a ignore-vars="true" href="https://developer.kookapp.cn/doc/http/user-chat#%E8%8E%B7%E5%8F%96%E7%A7%81%E4%BF%A1%E8%81%8A%E5%A4%A9%E4%BC%9A%E8%AF%9D%E5%88%97%E8%A1%A8">获取私信聊天会话列表</a>



</def>
<def title="GetUserChatViewApi">

`love.forte.simbot.kook.api.userchat.GetUserChatViewApi`

<a ignore-vars="true" href="https://developer.kookapp.cn/doc/http/user-chat#%E8%8E%B7%E5%8F%96%E7%A7%81%E4%BF%A1%E8%81%8A%E5%A4%A9%E4%BC%9A%E8%AF%9D%E8%AF%A6%E6%83%85">获取私信聊天会话详情</a>


</def>

</deflist>

