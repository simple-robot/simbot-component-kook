package love.forte.simbot.component.kaihieila


/**
 * 一个开黑啦组件的对象定义，实现此接口并提供一个可以获取 [源][source] 对象的api。
 *
 * @author ForteScarlet
 */
public interface KaiheilaComponentDefinition<out S> {

    /**
     * 得到当前定义类型下的源对象。
     */
    public val source: S


}