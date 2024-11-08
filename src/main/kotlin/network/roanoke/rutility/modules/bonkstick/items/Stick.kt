package network.roanoke.rutility.modules.bonkstick.items

import net.minecraft.component.DataComponentTypes
import net.minecraft.component.type.LoreComponent
import net.minecraft.component.type.NbtComponent
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.StringNbtReader
import net.minecraft.text.Text

class Stick {

    companion object {
        fun getBonkStick(): ItemStack {
            val item = Items.BLAZE_ROD.defaultStack
            item.set(DataComponentTypes.CUSTOM_NAME, Text.literal("§l§5Bonk Stick"))
            item.set(DataComponentTypes.LORE, LoreComponent(listOf(Text.literal("§7Hitting a pokemon with this will send it to \"a better place\""))))
            item.set(DataComponentTypes.ENCHANTMENT_GLINT_OVERRIDE, true)

            val compound = NbtCompound()
            compound.putString("id", "roanoke:bonk_stick")
            item.set(DataComponentTypes.CUSTOM_DATA, NbtComponent.of(compound))

            return item
        }
    }

}