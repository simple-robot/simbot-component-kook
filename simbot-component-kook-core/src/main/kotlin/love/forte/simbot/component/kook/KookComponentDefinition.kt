package love.forte.simbot.component.kook


/**
 * 一个 Kook 组件的对象定义，实现此接口并提供一个可以获取 [源][source] 对象的api。
 *
 * @author ForteScarlet
 */
public interface KookComponentDefinition<out S> {

    /**
     * 得到当前定义类型下的源对象。
     */
    public val source: S


}