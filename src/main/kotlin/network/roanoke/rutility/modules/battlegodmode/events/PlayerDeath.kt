package network.roanoke.rutility.modules.battlegodmode.events

import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.player.PlayerEntity
import network.roanoke.rutility.modules.battlegodmode.BattleGodMode

class PlayerDeath(private val module: BattleGodMode): ServerLivingEntityEvents.AfterDeath {
    override fun afterDeath(entity: LivingEntity?, damageSource: DamageSource?) {
        if (module.isEnabled()) {
            if (entity != null && entity is PlayerEntity) {
                if (module.playersInBattle.contains(entity.uuid)) {
                    module.playersInBattle.remove(entity.uuid)
                }
            }
        }
    }
}