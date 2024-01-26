/*
 * Copyright (c) 2024. ForteScarlet.
 *
 * This file is part of simbot-component-kook.
 *
 * simbot-component-kook is free software: you can redistribute it and/or modify it under the terms of
 * the GNU Lesser General Public License as published by the Free Software Foundation,
 * either version 3 of the License, or (at your option) any later version.
 *
 * simbot-component-kook is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with simbot-component-kook,
 * If not, see <https://www.gnu.org/licenses/>.
 */

package kook.internal.processors.apireader

import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.getKotlinClassByName
import com.google.devtools.ksp.isAbstract
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.squareup.kotlinpoet.ClassName
import java.io.File

private val KookApiClassName = ClassName("love.forte.simbot.kook.api", "KookApi")

const val TARGET_FILE_OPTION_KEY = "kook.api.finder.output"

/**
 *
 * @author ForteScarlet
 */
class ApiReaderProcessor(private val environment: SymbolProcessorEnvironment) : SymbolProcessor {
    private val targetFilePath: String? = environment.options[TARGET_FILE_OPTION_KEY]

    @OptIn(KspExperimental::class)
    override fun process(resolver: Resolver): List<KSAnnotated> {
        val targetFile = File(targetFilePath ?: run {
            val msg = "target output file option ['$TARGET_FILE_OPTION_KEY'] is null!"
            environment.logger.error(msg)
            throw NullPointerException(msg)
        })

        environment.logger.info("target output file: ${targetFile.absolutePath}")

        val apiClass = resolver.getKotlinClassByName("love.forte.simbot.kook.api.KookApi")
        environment.logger.info("apiClass: $apiClass")
        apiClass ?: return emptyList()

        val targetClasses = resolver.getAllFiles().flatMap { it.declarations }
            .filterIsInstance<KSClassDeclaration>()
            .filter {
                apiClass.asStarProjectedType().isAssignableFrom(it.asStarProjectedType())
            }
            .filter { !it.isAbstract() }
            .toList()



        for (targetClass in targetClasses) {

        }

        return emptyList()
    }
}

private fun writeDeflistTo(file: File, list: List<KSClassDeclaration>) {
    if (!file.exists()) {
        file.mkdirs()
        file.createNewFile()
    }

    file.printWriter().use { writer ->
        println("<deflist>\n")
        println("\n</deflist>")
    }

}
