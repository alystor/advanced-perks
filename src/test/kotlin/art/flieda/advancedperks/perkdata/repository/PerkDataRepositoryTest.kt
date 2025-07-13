package art.flieda.advancedperks.perkdata.repository

import art.flieda.advancedperks.AbstractDatabaseTest
import art.flieda.advancedperks.testutil.testdatageneration.PerkDataTestDataGeneration
import art.flieda.advancedperks.testutil.testdatageneration.TestDataGeneration
import io.kotest.matchers.collections.shouldContainAll
import io.kotest.matchers.collections.shouldNotContainAll
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.util.UUID

class PerkDataRepositoryTest : AbstractDatabaseTest() {

    private val sut = PerkDataRepository()

    @Nested
    inner class FindPerkDataByIdTest {

        @Test
        fun `find perk data only for playerId that has entries in the database`() {
            val playerId = UUID.randomUUID()
            val perkData = PerkDataTestDataGeneration.aPerkData(playerId = playerId)
            sut.savePerkData(perkData)
            TestDataGeneration.some { sut.savePerkData(PerkDataTestDataGeneration.aPerkData()) }


            val result = sut.findPerkDataById(playerId)!!


            result.playerId shouldBe perkData.playerId
            result.enabledPerks shouldContainAll perkData.enabledPerks
            result.boughtPerks shouldContainAll perkData.boughtPerks
        }


        @Test
        fun `return null when no entry was found`() {
            TestDataGeneration.some { sut.savePerkData(PerkDataTestDataGeneration.aPerkData()) }


            val result = sut.findPerkDataById(UUID.randomUUID())


            result shouldBe null
        }

    }

    @Nested
    inner class SavePerkDataTest {

        @Test
        fun `test if saving new perk data correctly overwrites old perk data for the same playerId`() {
            val playerId = UUID.randomUUID()
            val oldPerkData = PerkDataTestDataGeneration.aPerkData(playerId = playerId)
            val newPerkData = PerkDataTestDataGeneration.aPerkData(playerId = playerId)
            sut.savePerkData(oldPerkData)
            sut.savePerkData(newPerkData)


            val newResult = sut.findPerkDataById(playerId)!!


            newResult.playerId shouldBe newPerkData.playerId
            newResult.enabledPerks shouldNotContainAll oldPerkData.enabledPerks
            newResult.boughtPerks shouldNotContainAll oldPerkData.boughtPerks
            newResult.enabledPerks shouldContainAll newPerkData.enabledPerks
            newResult.boughtPerks shouldContainAll newPerkData.boughtPerks
        }

    }

}