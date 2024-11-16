package fr.bowser.behaviortracker.project

import com.lemonappdev.konsist.api.Konsist
import com.lemonappdev.konsist.api.verify.assertTrue
import org.junit.Test

class ClassNamingConventionTest {

    @Test
    fun `file name starts with package name`() {
        Konsist.scopeFromProduction()
            .files
            .filter { !it.packagee!!.hasNameContaining("internal") }
            .assertTrue {
                val lastPackageLevel = it.packagee!!.name.split(".").last()
                val camelCase = lastPackageLevel.convertToCamelCase()
                it.hasNameStartingWith(camelCase)
            }
    }

    private fun String.convertToCamelCase(): String {
        return this.split("_").joinToString("") { it.capitalize() }
    }
}
