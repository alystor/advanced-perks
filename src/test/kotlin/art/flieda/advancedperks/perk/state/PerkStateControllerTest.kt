package art.flieda.advancedperks.perk.state

import art.flieda.advancedperks.AbstractMockBukkitTest
import art.flieda.advancedperks.perk.registry.PerkRegistry
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.koin.core.component.get

class PerkStateControllerTest : AbstractMockBukkitTest() {

    @Nested
    inner class EnablePerkTest {

        @Test
        fun `forceEnablePerk always enables the perk given but never enables when its already enabled`() {
            val perk = get<PerkRegistry>().perkCache.firstNotNullOf { it.value }
            val player = server.addPlayer()
            

            val firstEnable = get<PerkStateController>().forceEnablePerk(player, perk)
            val secondEnable = get<PerkStateController>().forceEnablePerk(player, perk)


            firstEnable.shouldBeInstanceOf<PerkToggleStatus.Enabled>()
            secondEnable.shouldBeInstanceOf<PerkToggleStatus.Unchanged>()
        }

        @Test
        fun `dont enable perk for player that doesnt have the permission for the perk`() {
            val perk = get<PerkRegistry>().perkCache.firstNotNullOf { it.value }
            val player = server.addPlayer()


            val result = get<PerkStateController>().enablePerk(player, perk) as PerkToggleStatus.Restricted


            result.reason shouldBe PerkUsageRestriction.MISSING_PERMISSION
        }

        @Test
        fun `dont enable perk for player that has the permission for the perk but already hast the max amount of perk enabled`() {
            val perk = get<PerkRegistry>().perkCache.firstNotNullOf { it.value }
            val player = server.addPlayer()
                .withPermission(perk.getPermission()!!)
                .withPermission("advanced_perks.max_active_perks.0")


            val result = get<PerkStateController>().enablePerk(player, perk) as PerkToggleStatus.Restricted


            result.reason shouldBe PerkUsageRestriction.TOO_MANY_PERKS_ENABLED
        }

        @Test
        fun `dont enable perk for player that has the permission for the perk but is in a world the perk is disallowed in`() {
            val perk = get<PerkRegistry>().perkCache.firstNotNullOf { it.value }
            val player = server.addPlayer()
                .withPermission(perk.getPermission()!!)
            perk.setDisallowedWorlds(listOf(player.world.name))


            val result = get<PerkStateController>().enablePerk(player, perk) as PerkToggleStatus.Restricted


            result.reason shouldBe PerkUsageRestriction.DISALLOWED_WORLD
        }

        @Test
        fun `enable a perk for a player if all criteria are met but mark as unchanged if already active`() {
            val perk = get<PerkRegistry>().perkCache.firstNotNullOf { it.value }
            val player = server.addPlayer()
                .withPermission(perk.getPermission()!!)


            val firstEnable = get<PerkStateController>().enablePerk(player, perk)
            val secondEnable = get<PerkStateController>().enablePerk(player, perk)


            firstEnable.shouldBeInstanceOf<PerkToggleStatus.Enabled>()
            secondEnable.shouldBeInstanceOf<PerkToggleStatus.Unchanged>()
        }
    }

    @Nested
    inner class DisablePerkTest {

        @Test
        fun `forceDisablePerk always disables the perk given but never disables when its already disabled`() {
            val perk = get<PerkRegistry>().perkCache.firstNotNullOf { it.value }
            val player = server.addPlayer()

            get<PerkStateController>().forceEnablePerk(player, perk)
            val firstDisable = get<PerkStateController>().forceDisablePerk(player, perk)
            val secondDisable = get<PerkStateController>().forceDisablePerk(player, perk)


            firstDisable.shouldBeInstanceOf<PerkToggleStatus.Disabled>()
            secondDisable.shouldBeInstanceOf<PerkToggleStatus.Unchanged>()
        }

        @Test
        fun `dont disable perk for player that doesnt have the permission to use the perk`() {
            val perk = get<PerkRegistry>().perkCache.firstNotNullOf { it.value }
            val player = server.addPlayer()

            get<PerkStateController>().forceEnablePerk(player, perk)
            val result = get<PerkStateController>().disablePerk(player, perk) as PerkToggleStatus.Restricted


            result.reason shouldBe PerkUsageRestriction.MISSING_PERMISSION
        }

        @Test
        fun `still disable perk for player that has the permission for the perk but already hast the max amount of perk enabled`() {
            val perk = get<PerkRegistry>().perkCache.firstNotNullOf { it.value }
            val player = server.addPlayer()
                .withPermission(perk.getPermission()!!)
                .withPermission("advanced_perks.max_active_perks.0")


            get<PerkStateController>().forceEnablePerk(player, perk)
            val result = get<PerkStateController>().disablePerk(player, perk)


            result.shouldBeInstanceOf<PerkToggleStatus.Disabled>()
        }

        @Test
        fun `still disable perk for player that has the permission for the perk but is in a world the perk is disallowed in`() {
            val perk = get<PerkRegistry>().perkCache.firstNotNullOf { it.value }
            val player = server.addPlayer()
                .withPermission(perk.getPermission()!!)
            perk.setDisallowedWorlds(listOf(player.world.name))


            get<PerkStateController>().forceEnablePerk(player, perk)
            val result = get<PerkStateController>().disablePerk(player, perk)


            result.shouldBeInstanceOf<PerkToggleStatus.Disabled>()
        }

        @Test
        fun `disable a perk for a player if all criteria are met but mark as unchanged if already disabled`() {
            val perk = get<PerkRegistry>().perkCache.firstNotNullOf { it.value }
            val player = server.addPlayer()
                .withPermission(perk.getPermission()!!)


            get<PerkStateController>().forceEnablePerk(player, perk)
            val firstEnable = get<PerkStateController>().disablePerk(player, perk)
            val secondEnable = get<PerkStateController>().disablePerk(player, perk)


            firstEnable.shouldBeInstanceOf<PerkToggleStatus.Disabled>()
            secondEnable.shouldBeInstanceOf<PerkToggleStatus.Unchanged>()
        }
    }

    @Nested
    inner class TogglePerkTest {

        @Test
        fun `forceTogglePerk toggles the perk for the player even if not all criteria are met`() {
            val perk = get<PerkRegistry>().perkCache.firstNotNullOf { it.value }
            val player = server.addPlayer()


            val firstToggle = get<PerkStateController>().forceTogglePerk(player, perk)
            val secondToggle = get<PerkStateController>().forceTogglePerk(player, perk)


            firstToggle.shouldBeInstanceOf<PerkToggleStatus.Enabled>()
            secondToggle.shouldBeInstanceOf<PerkToggleStatus.Disabled>()
        }

        @Test
        fun `togglePerk does not toggle the perk for the player if hes missing the permission to use the perk`() {
            val perk = get<PerkRegistry>().perkCache.firstNotNullOf { it.value }
            val player = server.addPlayer()


            val enableToggle = get<PerkStateController>().togglePerk(player, perk) as PerkToggleStatus.Restricted
            get<PerkStateController>().forceTogglePerk(player, perk)
            val disableToggle = get<PerkStateController>().togglePerk(player, perk) as PerkToggleStatus.Restricted


            enableToggle.reason shouldBe PerkUsageRestriction.MISSING_PERMISSION
            disableToggle.reason shouldBe PerkUsageRestriction.MISSING_PERMISSION
        }

        @Test
        fun `togglePerk does toggle the perk off for the player even if he has reached the max amount of perks active but doesnt toggle it on`() {
            val perk = get<PerkRegistry>().perkCache.firstNotNullOf { it.value }
            val player = server.addPlayer()
                .withPermission(perk.getPermission()!!)
                .withPermission("advanced_perks.max_active_perks.0")

            get<PerkStateController>().forceEnablePerk(player, perk)
            val disableToggle = get<PerkStateController>().togglePerk(player, perk)
            val enableToggle = get<PerkStateController>().togglePerk(player, perk) as PerkToggleStatus.Restricted


            disableToggle.shouldBeInstanceOf<PerkToggleStatus.Disabled>()
            enableToggle.reason shouldBe PerkUsageRestriction.TOO_MANY_PERKS_ENABLED
        }

        @Test
        fun `togglePerk does toggle the perk off for the player even if he is in a disallowed world for the perk but doesnt toggle it on`() {
            val perk = get<PerkRegistry>().perkCache.firstNotNullOf { it.value }
            val player = server.addPlayer()
                .withPermission(perk.getPermission()!!)
            perk.setDisallowedWorlds(listOf(player.world.name))

            get<PerkStateController>().forceEnablePerk(player, perk)
            val disableToggle = get<PerkStateController>().togglePerk(player, perk)
            val enableToggle = get<PerkStateController>().togglePerk(player, perk) as PerkToggleStatus.Restricted


            disableToggle.shouldBeInstanceOf<PerkToggleStatus.Disabled>()
            enableToggle.reason shouldBe PerkUsageRestriction.DISALLOWED_WORLD
        }

    }

}
