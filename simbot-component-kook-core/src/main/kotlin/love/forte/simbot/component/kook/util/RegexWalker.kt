package love.forte.simbot.component.kook.util

/**
 * @return true if find([input]) != null
 */
public inline fun Regex.walk(input: String, onOther: (input: String, start: Int, end: Int) -> Unit, onMatch: (MatchResult) -> Unit): Boolean {
    var match: MatchResult? = find(input) ?: return false
    
    var lastStart = 0
    val length = input.length
    do {
        val foundMatch = match!!
        onOther(input, lastStart, foundMatch.range.first)
        onMatch(foundMatch)
        lastStart = foundMatch.range.last + 1
        match = foundMatch.next()
    } while (lastStart < length && match != null)
    
    if (lastStart < length) {
        onOther(input, lastStart, length)
    }
    
    return true
}