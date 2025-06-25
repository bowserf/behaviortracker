package fr.bowser.behaviortracker.project

import com.lemonappdev.konsist.api.Konsist
import com.lemonappdev.konsist.api.ext.list.withNameEndingWith
import com.lemonappdev.konsist.api.verify.assertTrue
import org.junit.Test

class PackageViewNamingConventionTest {

    @Test
    fun `class with 'View' suffix must be in a package with the '_view' suffix`() {
        Konsist.scopeFromProduction()
            .classes()
            .withNameEndingWith("View")
            .assertTrue {
                it.packagee!!.hasNameEndingWith("_view")
            }
    }

    @Test
    fun `package with the '_view' suffix must contain a class whose name has the suffix 'View'`() {
        val packagesNotMatchingRules = Konsist.scopeFromProduction()
            .packages
            .withNameEndingWith("_view")
            .groupBy { it.name }
            .filter {
                !it.value.any { packageDeclaration ->
                    packageDeclaration.containingFile.hasNameEndingWith("View")
                }
            }.map { it.key }

        if (packagesNotMatchingRules.isNotEmpty()) {
            val errorMessage = StringBuilder()
            errorMessage.append("The following view packages don't have a class with the suffix name \"View\":\n")
            packagesNotMatchingRules.forEach { errorMessage.append(it).append("\n") }
            assert(false) { errorMessage }
        }
    }
}
