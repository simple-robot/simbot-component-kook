package util

import org.gradle.api.*
import org.gradle.kotlin.dsl.*


fun Project.getProp(key: String): Any? = if (extra.has(key)) extra.get(key) else null