package network.roanoke.rutility.modules.bonkstick.events

import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import network.roanoke.rutility.modules.bonkstick.BonkStick

class BlockBreakEvent(private val module: BonkStick): PlayerBlockBreakEvents.Before {
    override fun beforeBlockBreak(
        world: World?,
        player: PlayerEntity?,
        pos: BlockPos?,
        state: BlockState?,
        blockEntity: BlockEntity?
    ): Boolean {
        if (!module.isEnabled())
            return true

        if (player != null && module.isBonkStick(player.mainHandStack))
            return false

        return true
    }
}