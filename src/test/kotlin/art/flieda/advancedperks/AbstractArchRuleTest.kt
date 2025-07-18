package art.flieda.advancedperks

import com.tngtech.archunit.core.domain.JavaClasses
import com.tngtech.archunit.core.importer.ClassFileImporter
import com.tngtech.archunit.lang.ArchRule

abstract class AbstractArchRuleTest {

    internal fun executeArchRuleTest(archRule: ArchRule) {
        archRule.check(getAllClassesToTest())
    }

    private fun getAllClassesToTest(): JavaClasses = ClassFileImporter()
        .importPackages("art.flieda.advancedperks")

}
