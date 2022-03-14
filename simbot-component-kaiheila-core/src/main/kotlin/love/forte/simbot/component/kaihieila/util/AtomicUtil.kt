package love.forte.simbot.component.kaihieila.util

import java.util.concurrent.atomic.*


internal inline fun <T, V> AtomicReferenceFieldUpdater<T, V>.update(obj: T, updateBlock: (V) -> V): V {
    var prev: V?
    var next: V
    do {
        prev = get(obj)
        next = updateBlock(prev)
    } while (!compareAndSet(obj, prev, next))

    return next
}
