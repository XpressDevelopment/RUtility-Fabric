package network.roanoke.rutility.modules.bonkstick.events

import com.cobblemon.mod.common.CobblemonSounds
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents
import net.minecraft.entity.Entity
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.damage.DamageSource
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.Text
import network.roanoke.rutility.modules.bonkstick.BonkStick
import network.roanoke.rutility.modules.bonkstick.items.Stick

class EntityDamageEvent(private val module: BonkStick): ServerLivingEntityEvents.AllowDamage {
    override fun allowDamage(entity: LivingEntity?, source: DamageSource?, amount: Float): Boolean {
        if (module.isEnabled()) {
            if (entity is PokemonEntity) {
                if (!entity.pokemon.isWild())
                    return false
                if (source != null && source.attacker != null && source.attacker!!.isPlayer) {
                    val player = source.attacker as ServerPlayerEntity
                    val itemStack = player.mainHandStack
                    if (module.isBonkStick(itemStack)) {
                        if (entity.pokemon.shiny || entity.pokemon.isLegendary() || entity.pokemon.isPlayerOwned())
                            return false
                        entity.remove(Entity.RemovalReason.DISCARDED)
                        player.world.playSound(null, player.blockPos, CobblemonSounds.DISPLAY_CASE_REMOVE_ITEM, player.soundCategory, 1.0f, 1.0f)
                        return false
                    } else if (module.isDimmsStick(itemStack)) {
                        if (module.isUpdatedDimmsStick(itemStack))
                            return false

                        if (player.inventory.emptySlot == -1) {
                            player.sendMessage(Text.literal("§6Dimms Stick is out of commission.\n" +
                                    "§eFree up an inventory slot to get the updated version."), false)
                        } else {
                            player.sendMessage(Text.literal("§6Dimms Stick is out of commission.\n" +
                                    "§6You've been given a §5§lBonk Stick §6that does essentially the same.\n" +
                                    "§6Have Fun!"), false)
                            module.updateDimmsStick(itemStack)
                            player.giveItemStack(Stick.getBonkStick())
                        }
                        return false
                    }
                }
            }
        }
        return true
    }
}