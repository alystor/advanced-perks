package art.flieda.advancedperks.structure

import art.flieda.advancedperks.AbstractArchRuleTest
import art.flieda.advancedperks.perk.AbstractPerk
import com.tngtech.archunit.core.domain.JavaClass
import com.tngtech.archunit.lang.ArchCondition
import com.tngtech.archunit.lang.ConditionEvents
import com.tngtech.archunit.lang.SimpleConditionEvent
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition
import io.kotest.assertions.withClue
import io.kotest.matchers.nulls.beNull
import io.kotest.matchers.shouldNot
import org.junit.jupiter.api.Test
import org.reflections.Reflections
import org.reflections.scanners.Scanners

class PerkClassStructureTest : AbstractArchRuleTest() {

    @Test
    fun `all perk classes should have a corresponding test class`() {
        val rule = ArchRuleDefinition.classes()
            .that().areAssignableTo(AbstractPerk::class.java)
            .and().areNotInterfaces()
            .and().areNotAnonymousClasses()
            .should(TestCheckCondition())
            .because("All perk classes should have a corresponding test class named <PerkClassName>Test")

        executeArchRuleTest(rule)
    }

    @Test
    fun `test that all perk classes have a yml file in the resource perks folder`() {
        val classes =
            Reflections("art.flieda.advancedperks.perk", Scanners.SubTypes).getSubTypesOf(AbstractPerk::class.java)

        classes.map {
            it.simpleName.removeSuffix("Perk").replace(Regex("([a-z])([A-Z])"), "$1_$2").lowercase()
        }.map {
            "perks/$it.yml"
        }.forEach {
            val resource = this.javaClass.classLoader.getResource(it)
            withClue("For each perk should exist a perk.yml file in the resources/perks folder: <$it> was expected but is missing") {
                resource shouldNot beNull()
            }
        }
    }

}

private class TestCheckCondition : ArchCondition<JavaClass>(
    "have a test class"
) {
    override fun check(
        item: JavaClass, events: ConditionEvents
    ) {
        val expectedClassName = "${item.fullName}Test"
        SimpleConditionEvent(
            item,
            runCatching { Class.forName(expectedClassName) }.isSuccess,
            "Class <${item.name}> does not have an equivalent Test class located at <$expectedClassName>"
        ).also(events::add)
    }

}
