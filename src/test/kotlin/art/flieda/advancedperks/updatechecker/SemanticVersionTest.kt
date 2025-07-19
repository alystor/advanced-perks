package art.flieda.advancedperks.updatechecker

import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class SemanticVersionTest {

    @Test
    fun `test the parsing of different valid and invalid semantic version strings`() {
        shouldNotThrowAny {
            "1.0.0".toSemanticVersion()
            "1.0.0-alpha".toSemanticVersion()
            "1.0.0-beta.1".toSemanticVersion()
        }

        shouldThrow<IllegalArgumentException> {
            "1.0".toSemanticVersion()
            "no version string".toSemanticVersion()
        }
    }


    @Test
    fun `check if semantic version instances are being compared correctly`() {
        val smallVersion = "1.0.0".toSemanticVersion()
        val smallIshVersion = "1.1.0".toSemanticVersion()
        val largeVersion = "2.0.0".toSemanticVersion()

        (smallVersion > largeVersion) shouldBe false
        (smallVersion < largeVersion) shouldBe true
        (smallVersion == largeVersion) shouldBe false
        (smallVersion >= largeVersion) shouldBe false
        (smallVersion <= largeVersion) shouldBe true
        (smallVersion < smallIshVersion) shouldBe true
        (smallVersion > smallIshVersion) shouldBe false
    }

}
