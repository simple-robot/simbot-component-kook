package love.forte.simbot.kook.api.guild.emoji

import io.ktor.http.*
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import love.forte.simbot.CharSequenceID
import love.forte.simbot.ID
import love.forte.simbot.kook.api.*
import love.forte.simbot.kook.objects.User
import love.forte.simbot.kook.objects.UserImpl
import love.forte.simbot.literal


/**
 *
 * [获取服务器表情列表](https://developer.kookapp.cn/doc/http/guild-emoji#%E8%8E%B7%E5%8F%96%E6%9C%8D%E5%8A%A1%E5%99%A8%E8%A1%A8%E6%83%85%E5%88%97%E8%A1%A8)
 *
 * @author ForteScarlet
 */
public class GuildEmojiListRequest(
    private val guildId: ID,
    private val pageRequest: PageRequestParameters? = null,
) : KookGetRequest<KookApiResult.ListData<GuildEmojiData>>() {
    public constructor(guildId: ID): this(guildId, null)
    
    public companion object Key : BaseKookApiRequestKey("guild-emoji", "list") {
        private val serializer = KookApiResult.ListData.serializer(GuildEmojiDataImpl.serializer())
        
    }
    
    override val resultDeserializer: DeserializationStrategy<out KookApiResult.ListData<GuildEmojiData>>
        get() = serializer
    
    override val apiPaths: List<String>
        get() = apiPathList
    
    override fun ParametersBuilder.buildParameters() {
        append("guild_id", guildId.literal)
        pageRequest?.appendTo(this)
    }
    
}


public interface GuildEmojiData {
    
    /**
     * 表情的 ID
     */
    public val id: ID
    
    /**
     * 表情的名称
     */
    public val name: String
    
    /**
     * 上传用户。
     */
    public val userInfo: User/*
    "user_info": {
          "id": "10000",
          "username": "用户名",
          "identify_num": "0123",
          "online": true,
          "os": "Websocket",
          "status": 1,
          "avatar": "https://XXXXXXXXXXX"
        }
     */
}

@Serializable
internal data class GuildEmojiDataImpl(
    override val id: CharSequenceID,
    override val name: String,
    @SerialName("user_info")
    override val userInfo: UserImpl,
) : GuildEmojiData