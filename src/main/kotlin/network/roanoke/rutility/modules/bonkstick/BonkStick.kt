package network.roanoke.rutility.modules.bonkstick

import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents
import net.minecraft.component.DataComponentTypes
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.nbt.NbtList
import net.minecraft.nbt.NbtString
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.Text
import net.minecraft.util.Hand
import network.roanoke.rutility.RModule
import network.roanoke.rutility.RUtility
import network.roanoke.rutility.modules.bonkstick.commands.BonkStickCommand
import network.roanoke.rutility.modules.bonkstick.events.BlockBreakEvent
import network.roanoke.rutility.modules.bonkstick.events.EntityDamageEvent
import network.roanoke.rutility.modules.bonkstick.items.Stick

class BonkStick(override val main: RUtility, override val name: String) : RModule {
    private var enabled = false

    init {
        ServerLivingEntityEvents.ALLOW_DAMAGE.register(EntityDamageEvent(this))
        PlayerBlockBreakEvents.BEFORE.register(BlockBreakEvent(this))
        BonkStickCommand(this)
    }

    override fun isEnabled(): Boolean {
        return enabled
    }

    override fun enable(enabled: Boolean) {
        this.enabled = enabled
    }

    fun isBonkStick(item: ItemStack): Boolean {
        val nbt = item.copy().components.get(DataComponentTypes.CUSTOM_DATA)?.copyNbt() ?: return false
        return nbt.getString("id") == "roanoke:bonk_stick"
    }
}