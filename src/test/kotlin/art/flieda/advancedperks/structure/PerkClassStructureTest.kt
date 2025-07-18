package art.flieda.advancedperks.structure

import art.flieda.advancedperks.AbstractArchRuleTest
import art.flieda.advancedperks.perk.AbstractPerk
import com.tngtech.archunit.core.domain.JavaClass
import com.tngtech.archunit.lang.ArchCondition
import com.tngtech.archunit.lang.ConditionEvents
import com.tngtech.archunit.lang.SimpleConditionEvent
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition
import org.junit.jupiter.api.Test

class PerkClassStructureTest : AbstractArchRuleTest() {

    @Test
    fun `test that all perk classes have a perk test class`() {
        val rule = ArchRuleDefinition.classes()
            .that().areAssignableTo(AbstractPerk::class.java)
            .and().areNotInterfaces()
            .and().areNotAnonymousClasses()
            .should(TestCheckCondition())
            .because("All perk classes should have a corresponding test class named <PerkClassName>Test")

        executeArchRuleTest(rule)
    }

}

private class TestCheckCondition : ArchCondition<JavaClass>(
    "have a test class"
) {
    override fun check(
        item: JavaClass,
        events: ConditionEvents
    ) {
        val expectedClassName = "${item.fullName}Test"
        SimpleConditionEvent(
            item,
            runCatching { Class.forName(expectedClassName) }.isSuccess,
            "Class <${item.name}> does not have an equivalent Test class located at <$expectedClassName>"
        ).also(events::add)
    }

}
