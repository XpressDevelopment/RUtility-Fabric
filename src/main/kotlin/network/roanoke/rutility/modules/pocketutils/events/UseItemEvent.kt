package network.roanoke.rutility.modules.pocketutils.events

import com.cobblemon.mod.common.CobblemonItems
import com.cobblemon.mod.common.CobblemonSounds
import com.cobblemon.mod.common.api.permission.CobblemonPermission
import com.cobblemon.mod.common.api.permission.PermissionLevel
import com.cobblemon.mod.common.api.storage.pc.link.PCLinkManager
import com.cobblemon.mod.common.api.storage.pc.link.PermissiblePcLink
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
import network.roanoke.rutility.modules.pocketutils.PocketUtils
import network.roanoke.rutility.utils.Utils

class UseItemEvent(private val module: PocketUtils): UseItemCallback {
    override fun interact(player: PlayerEntity?, world: World?, hand: Hand?): TypedActionResult<ItemStack> {
        if (!module.isEnabled())
            return TypedActionResult.pass(ItemStack.EMPTY)

        if (player == null || world == null || hand == null)
            return TypedActionResult.pass(ItemStack.EMPTY)

        if (hand != Hand.MAIN_HAND)
            return TypedActionResult.pass(player.getStackInHand(hand))

        if (!Permissions.check(player, "rutility.pocketutils.*"))
            if (!Permissions.check(player, "rutility.pocketutils.healer")
                && !Permissions.check(player, "rutility.pocketutils.pc")
                && !player.hasPermissionLevel(2))
                return TypedActionResult.pass(player.getStackInHand(hand))

        val itemStack = player.getStackInHand(hand)

        val item = itemStack.item

        if (item != CobblemonItems.HEALING_MACHINE && item != CobblemonItems.PC)
            return TypedActionResult.pass(player.getStackInHand(hand))

        if (module.isOnCooldown(player.uuid.toString()))
            return TypedActionResult.pass(player.getStackInHand(hand))

        val p = Utils.getPlayerByUUID(player.uuid)!!

        if (p.isInBattle()) {
            p.sendMessage(Text.literal("§cYou cannot use this while in battle!"))
            return TypedActionResult.pass(player.getStackInHand(hand))
        }

        when (item) {
            CobblemonItems.HEALING_MACHINE -> {
                if (!Permissions.check(player, "rutility.pocketutils.healer"))
                    return TypedActionResult.pass(player.getStackInHand(hand))

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
                if (!Permissions.check(player, "rutility.pocketutils.pc"))
                    return TypedActionResult.pass(player.getStackInHand(hand))

                val pc = p.pc()
                PCLinkManager.addLink(PermissiblePcLink(pc, p, CobblemonPermission("rutility.pocketutils.pc", PermissionLevel.NONE)))
                OpenPCPacket(p.pc().uuid).sendToPlayer(p)
                p.world.playSoundServer(position = p.pos,
                    sound = CobblemonSounds.PC_ON,
                    volume = 0.5F,
                    pitch = 1F)
                module.setCooldownUser(p.uuid.toString(), 1)
            }
        }

        return TypedActionResult.pass(player.getStackInHand(hand))
    }
}