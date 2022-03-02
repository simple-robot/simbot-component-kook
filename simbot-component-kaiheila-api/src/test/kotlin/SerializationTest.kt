import kotlinx.serialization.*
import kotlinx.serialization.json.*
import kotlin.test.*

/**
 *
 * @author ForteScarlet
 */
class SerializationTest {

    @Test
    fun jsonTest() {
        val user = User("forte")
        val newUser = Json.decodeFromString(User.serializer(), encode(Json, user))
        assert(user == newUser)
    }

    @Suppress("UNCHECKED_CAST")
    @OptIn(InternalSerializationApi::class)
    fun encode(json: Json, any: Any): String {
        val serializer = any::class.serializer() as KSerializer<Any>
        return json.encodeToString(serializer, any)
    }

    @Serializable
    data class User(val name: String)
}