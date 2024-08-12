package network.roanoke.rutility.modules.runningshoes.items

import net.minecraft.enchantment.Enchantment
import net.minecraft.enchantment.Enchantments
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.nbt.NbtList
import net.minecraft.nbt.NbtString
import net.minecraft.text.Text

class ShoesItem {

    companion object {
        fun getOldRunningShoes(): ItemStack {
            val item = Items.LEATHER_BOOTS.defaultStack
            val nbt = item.orCreateNbt
            val displayNbt = item.getOrCreateSubNbt("display")

            item.addEnchantment(Enchantments.UNBREAKING, 255)

            item.setCustomName(Text.literal("Old Running Shoes"))
            val loreList = mutableListOf<Text>()
            loreList.add(Text.literal(""))
            loreList.add(Text.literal("§7When on Feet:"))
            loreList.add(Text.literal("§9+20% Speed"))

            val nbtLore = NbtList()

            for (line in loreList)
                nbtLore.add(NbtString.of(Text.Serializer.toJson(line)))

            displayNbt.put("Lore", nbtLore)

            nbt.put("display", displayNbt)
            nbt.putString("id", "roanoke:old_running_shoes")
            nbt.putInt("HideFlags", 63)
            item.nbt = nbt

            return item
        }

        fun getRunningShoes(): ItemStack {
            val item = Items.LEATHER_BOOTS.defaultStack
            val nbt = item.orCreateNbt
            val displayNbt = item.getOrCreateSubNbt("display")

            item.addEnchantment(Enchantments.UNBREAKING, 255)

            item.setCustomName(Text.literal("Running Shoes"))
            val loreList = mutableListOf<Text>()
            loreList.add(Text.literal(""))
            loreList.add(Text.literal("§7When on Feet:"))
            loreList.add(Text.literal("§9+40% Speed"))

            val nbtLore = NbtList()

            for (line in loreList)
                nbtLore.add(NbtString.of(Text.Serializer.toJson(line)))

            displayNbt.put("Lore", nbtLore)

            nbt.put("display", displayNbt)
            nbt.putString("id", "roanoke:running_shoes")
            nbt.putInt("HideFlags", 63)
            item.nbt = nbt

            return item
        }
    }

}