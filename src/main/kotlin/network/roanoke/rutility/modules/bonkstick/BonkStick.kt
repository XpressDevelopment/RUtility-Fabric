package network.roanoke.rutility.modules.bonkstick

import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.nbt.NbtList
import net.minecraft.nbt.NbtString
import net.minecraft.text.Text
import network.roanoke.rutility.RModule
import network.roanoke.rutility.RUtility
import network.roanoke.rutility.modules.bonkstick.commands.BonkStickCommand
import network.roanoke.rutility.modules.bonkstick.events.EntityDamageEvent

class BonkStick(override val main: RUtility, override val name: String) : RModule {
    private var enabled = false

    init {
        ServerLivingEntityEvents.ALLOW_DAMAGE.register(EntityDamageEvent(this))
        BonkStickCommand(this)
    }

    override fun isEnabled(): Boolean {
        return enabled
    }

    override fun enable(enabled: Boolean) {
        this.enabled = enabled
    }

    fun isBonkStick(item: ItemStack): Boolean {
        val nbt = item.nbt ?: return false
        return nbt.getString("id") == "roanoke:bonk_stick"
    }

    fun isDimmsStick(item: ItemStack): Boolean {
        if (item.item == Items.STICK)
            if (item.hasEnchantments()) {
                item.enchantments.forEach { enchantment ->
                    if ((enchantment.toString().contains("id:\"minecraft:knockback\"") || enchantment.toString()
                            .contains("id:\"knockback\""))
                        && enchantment.toString().contains("lvl:255")
                    )
                        return true
                }
            }
        return false
    }

    fun isUpdatedDimmsStick(item: ItemStack): Boolean {
        val nbt = item.nbt ?: return false

        val display = item.getOrCreateSubNbt("display")

        val nbtLore = display.get("Lore") as NbtList
        nbtLore.forEach { lore ->
            if (lore.toString().contains("§6Relic"))
                return nbt.getString("id") == "roanoke:dimms_stick"
        }

        return false
    }

    fun updateDimmsStick(item: ItemStack) {
        val nbt = item.nbt ?: return
        nbt.putString("id", "roanoke:dimms_stick")
        val display = item.getOrCreateSubNbt("display")

        val nbtLore = display.get("Lore") as NbtList
        nbtLore.add(NbtString.of(Text.Serializer.toJson(Text.literal(" "))))
        nbtLore.add(NbtString.of(Text.Serializer.toJson(Text.literal("§6Relic"))))
        display.put("Lore", nbtLore)
        nbt.put("display", display)
        item.nbt = nbt
    }
}