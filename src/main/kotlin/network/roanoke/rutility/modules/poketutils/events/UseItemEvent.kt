package network.roanoke.rutility.modules.poketutils.events

import com.cobblemon.mod.common.CobblemonItems
import com.cobblemon.mod.common.CobblemonSounds
import com.cobblemon.mod.common.net.messages.client.storage.pc.OpenPCPacket
import com.cobblemon.mod.common.util.*
import me.lucko.fabric.api.permissions.v0.Permissions
import net.fabricmc.fabric.api.event.player.UseItemCallback
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.text.Text
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult
import net.minecraft.world.World
import network.roanoke.rutility.RUtility
import network.roanoke.rutility.modules.poketutils.PocketUtils
import network.roanoke.rutility.utils.Utils

class UseItemEvent(private val module: PocketUtils): UseItemCallback {
    override fun interact(player: PlayerEntity?, world: World?, hand: Hand?): TypedActionResult<ItemStack> {
        if (!module.isEnabled())
            return TypedActionResult.success(ItemStack.EMPTY)

        if (player == null || world == null || hand == null)
            return TypedActionResult.success(ItemStack.EMPTY)

        if (hand != Hand.MAIN_HAND)
            return TypedActionResult.success(player.getStackInHand(hand))

        if (!Permissions.check(player, "rutility.pocketutils.*"))
            if (!Permissions.check(player, "rutility.pocketutils.healer")
                && !Permissions.check(player, "rutility.pocketutils.pc")
                && !player.hasPermissionLevel(2))
                return TypedActionResult.success(player.getStackInHand(hand))

        val itemStack = player.getStackInHand(hand)

        val item = itemStack.item

        if (item != CobblemonItems.HEALING_MACHINE && item != CobblemonItems.PC)
            return TypedActionResult.success(player.getStackInHand(hand))

        if (module.isOnCooldown(player.uuid.toString()))
            return TypedActionResult.success(player.getStackInHand(hand))

        val p = Utils.getPlayerByUUID(player.uuid)!!

        if (p.isInBattle()) {
            p.sendMessage(Text.literal("§cYou cannot use this while in battle!"))
            return TypedActionResult.success(player.getStackInHand(hand))
        }

        when (item) {
            CobblemonItems.HEALING_MACHINE -> {
                if (!Permissions.check(player, "rutility.pocketutils.use.healer"))
                    return TypedActionResult.success(player.getStackInHand(hand))

                if (!module.isHealingOnCooldown(p.uuid.toString())) {
                    if (!p.party().none { it.canBeHealed() }) {
                        p.party().heal()

                        p.world.playSoundServer(
                            position = p.pos,
                            sound = CobblemonSounds.HEALING_MACHINE_ACTIVE,
                            volume = 0.5F,
                            pitch = 1F
                        )
                        module.setHealingCooldownUser(p.uuid.toString(), 20 * 5)
                    } else
                        p.sendMessage(Text.literal("§cYour Pokémon party is already fully healed!"))
                } else
                    p.sendMessage(Text.literal("§cHealer is on cooldown! Wait a few seconds before using it again."))
                module.setCooldownUser(p.uuid.toString(), 1)
            }
            CobblemonItems.PC -> {
                if (!Permissions.check(player, "rutility.pocketutils.use.pc"))
                    return TypedActionResult.success(player.getStackInHand(hand))

                OpenPCPacket(p.pc().uuid).sendToPlayer(p)
                p.world.playSoundServer(position = p.pos,
                    sound = CobblemonSounds.PC_ON,
                    volume = 0.5F,
                    pitch = 1F)
                module.setCooldownUser(p.uuid.toString(), 1)
            }
        }

        return TypedActionResult.success(player.getStackInHand(hand))
    }
}