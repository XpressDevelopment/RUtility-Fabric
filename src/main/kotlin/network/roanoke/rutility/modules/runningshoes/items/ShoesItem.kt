package network.roanoke.rutility.modules.runningshoes.items

import net.minecraft.component.DataComponentTypes
import net.minecraft.component.type.ItemEnchantmentsComponent
import net.minecraft.component.type.LoreComponent
import net.minecraft.component.type.NbtComponent
import net.minecraft.enchantment.Enchantment
import net.minecraft.enchantment.Enchantments
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtList
import net.minecraft.nbt.NbtString
import net.minecraft.registry.BuiltinRegistries
import net.minecraft.registry.Registries
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.RegistryWrapper
import net.minecraft.registry.entry.RegistryEntry
import net.minecraft.text.Text
import net.minecraft.util.Unit
import network.roanoke.rutility.RUtility

class ShoesItem {

    companion object {
        fun getOldRunningShoes(): ItemStack {
            val item = Items.LEATHER_BOOTS.defaultStack
            item.set(DataComponentTypes.CUSTOM_NAME, Text.literal("Old Running Shoes"))

            val enchantBuilder = ItemEnchantmentsComponent.Builder(ItemEnchantmentsComponent.DEFAULT)
            val enchantment = RUtility.serverInstance.registryManager.getWrapperOrThrow(RegistryKeys.ENCHANTMENT).getOrThrow(Enchantments.UNBREAKING)
            enchantBuilder.add(enchantment, 255)
            val enchantComponent = enchantBuilder.build()
            item.set(DataComponentTypes.ENCHANTMENTS, enchantComponent)

            item.set(DataComponentTypes.LORE, LoreComponent(listOf(
                Text.literal(""),
                Text.literal("§7When on Feet:"),
                Text.literal("§9+20% Speed")
            )))

            val compound = NbtCompound()
            compound.putString("id", "roanoke:old_running_shoes")
            compound.putInt("HideFlags", 63)
            item.set(DataComponentTypes.CUSTOM_DATA, NbtComponent.of(compound))
            item.set(DataComponentTypes.HIDE_ADDITIONAL_TOOLTIP, Unit.INSTANCE)

            return item
        }

        fun getRunningShoes(): ItemStack {
            val item = Items.LEATHER_BOOTS.defaultStack
            item.set(DataComponentTypes.CUSTOM_NAME, Text.literal("Running Shoes"))

            val enchantBuilder = ItemEnchantmentsComponent.Builder(ItemEnchantmentsComponent.DEFAULT)
            val enchantment = RUtility.serverInstance.registryManager.getWrapperOrThrow(RegistryKeys.ENCHANTMENT).getOrThrow(Enchantments.UNBREAKING)
            enchantBuilder.add(enchantment, 255)
            val enchantComponent = enchantBuilder.build()
            item.set(DataComponentTypes.ENCHANTMENTS, enchantComponent)

            item.set(DataComponentTypes.LORE, LoreComponent(listOf(
                Text.literal(""),
                Text.literal("§7When on Feet:"),
                Text.literal("§9+40% Speed")
            )))

            val compound = NbtCompound()
            compound.putString("id", "roanoke:running_shoes")
            compound.putInt("HideFlags", 63)
            item.set(DataComponentTypes.CUSTOM_DATA, NbtComponent.of(compound))
            item.set(DataComponentTypes.HIDE_ADDITIONAL_TOOLTIP, Unit.INSTANCE)

            return item
        }
    }

}