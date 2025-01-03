package network.roanoke.rutility.utils

import com.cobblemon.mod.common.entity.pokemon.PokemonEntity
import net.minecraft.component.DataComponentTypes
import net.minecraft.entity.attribute.EntityAttributeModifier
import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.network.packet.s2c.play.SubtitleS2CPacket
import net.minecraft.network.packet.s2c.play.TitleS2CPacket
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvent
import net.minecraft.text.Text
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos
import network.roanoke.rutility.RUtility
import java.util.*

class Utils {

    companion object {
        fun broadcast(message: Any) {
            val server = RUtility.serverInstance

            for (player in server.playerManager.playerList) {
                if (player is ServerPlayerEntity) {
                    if (message !is Text)
                        player.sendMessage(Text.literal(message.toString()), false)
                    else
                        player.sendMessage(message, false)
                }
            }
        }

        fun broadcastTitle(message: Any) {
            val server = RUtility.serverInstance

            for (player in server.playerManager.playerList) {
                if (player is ServerPlayerEntity) {
                    if (message !is Text)
                        player.networkHandler.sendPacket(TitleS2CPacket(Text.literal(message.toString())))
                    else
                        player.networkHandler.sendPacket(TitleS2CPacket(message))
                }
            }
        }

        fun broadcastSubtitle(message: Any) {
            val server = RUtility.serverInstance

            for (player in server.playerManager.playerList) {
                if (player is ServerPlayerEntity) {
                    if (message !is Text) {
                        player.networkHandler.sendPacket(TitleS2CPacket(Text.literal("")))
                        player.networkHandler.sendPacket(SubtitleS2CPacket(Text.literal(message.toString())))
                    } else {
                        player.networkHandler.sendPacket(TitleS2CPacket(Text.literal("")))
                        player.networkHandler.sendPacket(SubtitleS2CPacket(message))
                    }
                }
            }
        }

        fun broadcastSubtitleAllBut(message: Any, player: ServerPlayerEntity) {
            val server = RUtility.serverInstance

            for (p in server.playerManager.playerList) {
                if (p is ServerPlayerEntity && p.uuid != player.uuid) {
                    if (message !is Text) {
                        p.networkHandler.sendPacket(TitleS2CPacket(Text.literal("")))
                        p.networkHandler.sendPacket(SubtitleS2CPacket(Text.literal(message.toString())))
                    } else {
                        p.networkHandler.sendPacket(TitleS2CPacket(Text.literal("")))
                        p.networkHandler.sendPacket(SubtitleS2CPacket(message))
                    }
                }
            }
        }

        fun sendTitle(target: ServerPlayerEntity, message: Any) {
            if (message !is Text)
                target.networkHandler.sendPacket(TitleS2CPacket(Text.literal(message.toString())))
            else
                target.networkHandler.sendPacket(TitleS2CPacket(message))
        }

        fun playSoundAll(sound: SoundEvent) {
            val server = RUtility.serverInstance

            for (player in server.playerManager.playerList) {
                if (player is ServerPlayerEntity) {
                    player.world!!.playSound(
                        null,
                        player.steppingPos,
                        sound,
                        SoundCategory.NEUTRAL,
                        1f,
                        1f
                    )
                }
            }
        }

        fun playSoundPlayer(player: ServerPlayerEntity, sound: SoundEvent) {
            player.world!!.playSound(
                null,
                player.steppingPos,
                sound,
                SoundCategory.NEUTRAL,
                1f,
                1f
            )
        }

        fun getPlayerByUUID(uuid: UUID): ServerPlayerEntity? {
            val server = RUtility.serverInstance
            val playerManager = server.playerManager

            return playerManager.getPlayer(uuid)
        }

        fun getPlayerByName(name: String): ServerPlayerEntity? {
            val server = RUtility.serverInstance
            val playerManager = server.playerManager

            return playerManager.getPlayer(name)
        }

        fun getAllPlayerNames(): Array<out String>? {
            val server = RUtility.serverInstance
            val playerManager = server.playerManager

            return playerManager.playerNames
        }

        fun checkBlocksAbove(pokemon: PokemonEntity): BlockPos {
            val world = pokemon.world
            val pos = pokemon.blockPos
            val height = 10

            for (i in 1..height) {
                if (!world.isAir(pos.up(i))) {
                    return pos
                }
            }
            return pos.up(height)
        }

        fun isRunningShoes(item: ItemStack): Boolean {
            return isOldRunningShoes(item) || isNewRunningShoes(item)
        }

        fun isOldRunningShoes(item: ItemStack?): Boolean {
            val nbt = item?.copy()?.components?.get(DataComponentTypes.CUSTOM_DATA)?.copyNbt() ?: return false
            return nbt.getString("id") == "roanoke:old_running_shoes"
        }

        fun isNewRunningShoes(item: ItemStack?): Boolean {
            val nbt = item?.copy()?.components?.get(DataComponentTypes.CUSTOM_DATA)?.copyNbt() ?: return false
            return nbt.getString("id") == "roanoke:running_shoes"
        }

        fun addSpeedModifier(player: PlayerEntity, speedMultiplier: Double) {
            val modifierId = Identifier.of(player.uuid.toString())
            val attribute = player.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED)
            val modifier = EntityAttributeModifier(modifierId, speedMultiplier - 1, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE)
            attribute?.removeModifier(modifier)
            attribute?.addPersistentModifier(modifier)

        }

        fun removeSpeedModifier(player: PlayerEntity) {
            val modifierId = Identifier.of(player.uuid.toString())
            val attribute = player.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED)
            val modifier = EntityAttributeModifier(modifierId, 0.0, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE)
            attribute?.removeModifier(modifier)
        }

    }


}