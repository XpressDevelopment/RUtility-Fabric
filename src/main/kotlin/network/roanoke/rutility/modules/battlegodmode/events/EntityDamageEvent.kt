package network.roanoke.rutility.modules.battlegodmode.events

import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.player.PlayerEntity
import network.roanoke.rutility.modules.battlegodmode.BattleGodMode

class EntityDamageEvent(private val module: BattleGodMode) : ServerLivingEntityEvents.AllowDamage {

    override fun allowDamage(entity: LivingEntity?, source: DamageSource?, amount: Float): Boolean {
        if (module.isEnabled())
            if (entity is PlayerEntity) {
                if (module.playersInBattle.contains(entity.uuid))
                    return false
            }
        return true
    }

}