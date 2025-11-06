package art.flieda.advancedperks.structure

import art.flieda.advancedperks.command.AbstractPerkSubCommand
import art.flieda.advancedperks.command.PerkCommandModule
import io.github.classgraph.ClassGraph
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.fail
import org.koin.core.annotation.KoinInternalApi

class PerkSubCommandBoundTest {

    @OptIn(KoinInternalApi::class)
    @Test
    fun `all implemented sub command classes are properly defined in the respective koin module`() {
        val subCommandClasses = ClassGraph().enableClassInfo().scan().use { scanResult ->
            scanResult.getSubclasses(AbstractPerkSubCommand::class.java)
                .filterNot { classInfo -> classInfo.isAbstract || classInfo.isInterface }
                .mapNotNull { classInfo -> classInfo.loadClass().kotlin.simpleName }
                .toList()
        }


        val subCommandsMissingFromModule = subCommandClasses.filter { className ->
            PerkCommandModule.module.mappings.keys.none { key ->
                key.contains(className)
            }
        }

        if (subCommandsMissingFromModule.isNotEmpty()) {
            fail {
                "Every implemented sub command class should be bound in the PerkCommandModule. " +
                        "There are missing bindings for the following class(es): $subCommandsMissingFromModule."
            }
        }
    }

}
