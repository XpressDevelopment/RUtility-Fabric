package network.roanoke.rutility.modules.bonkstick.items

import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.nbt.StringNbtReader

class Stick {

    companion object {
        fun getBonkStick(): ItemStack {
            val item = Items.BLAZE_ROD.defaultStack
            val nbt =
                StringNbtReader.parse(
                    "{display:{Name:'[\"\",{\"text\":\"Bonk Stick\",\"italic\":false,\"bold\":true,\"color\":\"dark_purple\"}]'," +
                            "Lore:['[\"\",{\"text\":\"Hitting a pokemon with this will send it to \\\\\"a better place\\\\\"\",\"italic\":false,\"color\":\"gray\"}]']}," +
                            "Enchantments:[{}]}"
                )

            nbt.putString("id", "roanoke:bonk_stick")
            item.nbt = nbt

            return item
        }
    }

}