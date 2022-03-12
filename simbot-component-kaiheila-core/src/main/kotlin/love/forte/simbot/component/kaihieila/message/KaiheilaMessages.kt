package love.forte.simbot.component.kaihieila.message

import love.forte.simbot.message.*

/**
 * 提供开黑啦组件中一些会用到的信息。
 *
 * @author ForteScarlet
 */
@Suppress("MemberVisibilityCanBePrivate")
public object KaiheilaMessages {

    /**
     * 当at(mention)的目标为用户时，[At.atType] 所使用的值。[AT_TYPE_USER] 也是 [At.atType] 的默认值。
     */
    public const val AT_TYPE_USER: String = "user"

    /**
     * 当at(mention)的目标为角色时，[At.atType] 所使用的值。
     */
    public const val AT_TYPE_ROLE: String = "role"

}
