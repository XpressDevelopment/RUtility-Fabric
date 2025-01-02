package network.roanoke.rutility.modules.runningshoes.items

import net.minecraft.component.DataComponentTypes
import net.minecraft.component.type.*
import net.minecraft.enchantment.Enchantment
import net.minecraft.enchantment.Enchantments
import net.minecraft.entity.attribute.EntityAttribute
import net.minecraft.entity.attribute.EntityAttributeModifier
import net.minecraft.entity.attribute.EntityAttributes
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
import net.minecraft.util.Formatting
import net.minecraft.util.Identifier
import net.minecraft.util.Rarity
import net.minecraft.util.Unit
import network.roanoke.rutility.RUtility

class ShoesItem {

    companion object {
        fun getOldRunningShoes(): ItemStack {
            val item = Items.LEATHER_BOOTS.defaultStack
            item.set(DataComponentTypes.CUSTOM_NAME, Text.literal("Old Running Shoes"))

            /*
            val enchantBuilder = ItemEnchantmentsComponent.Builder(ItemEnchantmentsComponent.DEFAULT)
            val enchantment = RUtility.serverInstance.registryManager.getWrapperOrThrow(RegistryKeys.ENCHANTMENT).getOrThrow(Enchantments.UNBREAKING)
            enchantBuilder.add(enchantment, 255)
            val enchantComponent = enchantBuilder.build()
            item.set(DataComponentTypes.ENCHANTMENTS, enchantComponent)
            */

            val compound = NbtCompound()
            compound.putString("id", "roanoke:old_running_shoes")
            item.set(DataComponentTypes.UNBREAKABLE, UnbreakableComponent(true))
            item.set(DataComponentTypes.CUSTOM_DATA, NbtComponent.of(compound))
            item.set(DataComponentTypes.HIDE_ADDITIONAL_TOOLTIP, Unit.INSTANCE)
            item.set(DataComponentTypes.ENCHANTMENT_GLINT_OVERRIDE, true)
            item.set(DataComponentTypes.RARITY, Rarity.RARE)
            item.set(DataComponentTypes.ATTRIBUTE_MODIFIERS, AttributeModifiersComponent(
                mutableListOf(
                    AttributeModifiersComponent.Entry(
                        EntityAttributes.GENERIC_MOVEMENT_SPEED,
                        EntityAttributeModifier(Identifier.of("movement_speed"), 0.2, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE),
                        AttributeModifierSlot.FEET
                    ),
                    AttributeModifiersComponent.Entry(
                        EntityAttributes.GENERIC_ARMOR,
                        EntityAttributeModifier(Identifier.of("armor"), 1.0, EntityAttributeModifier.Operation.ADD_VALUE),
                        AttributeModifierSlot.FEET
                    )
                ), true
            ))

            return item
        }

        fun getRunningShoes(): ItemStack {
            val item = Items.LEATHER_BOOTS.defaultStack
            item.set(DataComponentTypes.CUSTOM_NAME, Text.literal("Running Shoes"))

            val compound = NbtCompound()
            compound.putString("id", "roanoke:running_shoes")
            item.set(DataComponentTypes.UNBREAKABLE, UnbreakableComponent(true))
            item.set(DataComponentTypes.CUSTOM_DATA, NbtComponent.of(compound))
            item.set(DataComponentTypes.HIDE_ADDITIONAL_TOOLTIP, Unit.INSTANCE)
            item.set(DataComponentTypes.ENCHANTMENT_GLINT_OVERRIDE, true)
            item.set(DataComponentTypes.RARITY, Rarity.RARE)
            item.set(DataComponentTypes.ATTRIBUTE_MODIFIERS, AttributeModifiersComponent(
                mutableListOf(
                    AttributeModifiersComponent.Entry(
                        EntityAttributes.GENERIC_MOVEMENT_SPEED,
                        EntityAttributeModifier(Identifier.of("movement_speed"), 0.4, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE),
                        AttributeModifierSlot.FEET
                    ),
                    AttributeModifiersComponent.Entry(
                        EntityAttributes.GENERIC_ARMOR,
                        EntityAttributeModifier(Identifier.of("armor"), 1.0, EntityAttributeModifier.Operation.ADD_VALUE),
                        AttributeModifierSlot.FEET
                    )
                ), true
            ))

            return item
        }
    }

}