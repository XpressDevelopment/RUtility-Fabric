package network.roanoke.rutility.modules.runningshoes.events

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents
import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.server.network.ServerPlayerEntity
import network.roanoke.rutility.modules.runningshoes.RunningShoes
import network.roanoke.rutility.utils.Utils

class EquipmentChangeEvent(private val module: RunningShoes): ServerEntityEvents.EquipmentChange {
    override fun onChange(
        livingEntity: LivingEntity?,
        equipmentSlot: EquipmentSlot?,
        previousStack: ItemStack?,
        currentStack: ItemStack?
    ) {
        if (!module.isEnabled()) return

        if (livingEntity == null || equipmentSlot == null || currentStack == null) return
        if (livingEntity !is PlayerEntity || livingEntity !is ServerPlayerEntity) return
        val player = livingEntity as ServerPlayerEntity
        if (equipmentSlot == EquipmentSlot.FEET) {
            if (Utils.isOldRunningShoes(currentStack)) {
                Utils.removeSpeedModifier(player)
                Utils.addSpeedModifier(player, 1.2)
            } else if (Utils.isOldRunningShoes(previousStack)) {
                Utils.removeSpeedModifier(player)
            }

            if (Utils.isNewRunningShoes(currentStack)) {
                Utils.removeSpeedModifier(player)
                Utils.addSpeedModifier(player, 1.4)
            } else if (Utils.isNewRunningShoes(previousStack)) {
                Utils.removeSpeedModifier(player)
            }
        } else {
            return
        }
    }
}