package network.roanoke.rutility.utils

import com.cobblemon.mod.common.api.pokemon.stats.Stats
import com.cobblemon.mod.common.api.storage.party.PlayerPartyStore
import com.cobblemon.mod.common.pokemon.Pokemon
import com.cobblemon.mod.common.util.lang
import net.minecraft.text.*
import net.minecraft.util.Formatting
import java.util.*
import java.util.function.Consumer
import kotlin.String



object ShowUtils {
    fun getHoverText(toSend: MutableText, pokemon: Pokemon): Text {
        val infoHoverText = Text.literal("").fillStyle(Style.EMPTY)
        infoHoverText.append(
            pokemon.getDisplayName().setStyle(Style.EMPTY.withColor(Formatting.DARK_GREEN).withUnderline(false))
        )
        if (pokemon.shiny) {
            infoHoverText.append(Text.literal(" ★").formatted(Formatting.GOLD))
        }
        infoHoverText.append(Text.literal("\n"))
        infoHoverText.append(
            Text.literal("Level: ").formatted(Formatting.RED).append(
                Text.literal(pokemon.level.toString()).formatted(
                    Formatting.WHITE
                )
            )
        )
        infoHoverText.append(Text.literal("\n"))
        infoHoverText.append(
            Text.literal("Nature: ").formatted(Formatting.GOLD).append(
                lang(pokemon.nature.displayName.replace("cobblemon.", "")).formatted(
                    Formatting.WHITE
                )
            )
        )
        infoHoverText.append(Text.literal("\n"))
        infoHoverText.append(
            Text.literal("Ability: ").formatted(Formatting.YELLOW).append(
                lang(pokemon.ability.displayName.replace("cobblemon.", "")).formatted(
                    Formatting.WHITE
                )
            )
        )
        infoHoverText.append(Text.literal("\n"))
        infoHoverText.append(
            Text.literal("Form: ").formatted(Formatting.GREEN).append(
                Text.literal(pokemon.form.name).formatted(
                    Formatting.WHITE
                )
            )
        )
        infoHoverText.append(Text.literal("\n"))
        val genderText = formatGenderName(pokemon.gender.name)
        infoHoverText.append(
            Text.literal("Gender: ").formatted(Formatting.AQUA).append(
                Text.literal(genderText).formatted(
                    Formatting.WHITE
                )
            )
        )
        infoHoverText.append(Text.literal("\n"))
        val formattedBallName = formatBallName(pokemon.caughtBall.name.toString().replace("cobblemon:", ""))
        infoHoverText.append(
            Text.literal("Ball: ").formatted(Formatting.BLUE).append(
                Text.literal(formattedBallName).formatted(
                    Formatting.WHITE
                )
            )
        )
        val infoText = Texts.join(
            Text.literal("[Info]").formatted(Formatting.DARK_GREEN)
                .getWithStyle(
                    Style.EMPTY.withColor(Formatting.DARK_GREEN)
                        .withHoverEvent(HoverEvent(HoverEvent.Action.SHOW_TEXT, infoHoverText))
                ), Text.of("")
        )
        toSend.append(infoText)

        // EV and IV combined list
        val statsText = Text.literal(" [EVs/IVs]").formatted(Formatting.GOLD)
        val statsHoverText = Text.literal("")

        // EVs
        statsHoverText.append(
            Text.literal("EVs: ").formatted(Formatting.GOLD)
                .append("\n")
                .append(
                    Text.literal("HP: ").formatted(Formatting.GREEN).append(
                        Text.literal(pokemon.evs.getOrDefault(Stats.HP).toString()).formatted(
                            Formatting.WHITE
                        )
                    )
                )
                .append("\n")
                .append(
                    Text.literal("Attack: ").formatted(Formatting.RED).append(
                        Text.literal(pokemon.evs.getOrDefault(Stats.ATTACK).toString()).formatted(
                            Formatting.WHITE
                        )
                    )
                )
                .append("\n")
                .append(
                    Text.literal("Defense: ").formatted(Formatting.GOLD).append(
                        Text.literal(pokemon.evs.getOrDefault(Stats.DEFENCE).toString()).formatted(
                            Formatting.WHITE
                        )
                    )
                )
                .append("\n")
                .append(
                    Text.literal("Sp. Attack: ").formatted(Formatting.LIGHT_PURPLE).append(
                        Text.literal(pokemon.evs.getOrDefault(Stats.SPECIAL_ATTACK).toString()).formatted(
                            Formatting.WHITE
                        )
                    )
                )
                .append("\n")
                .append(
                    Text.literal("Sp. Defense: ").formatted(Formatting.YELLOW).append(
                        Text.literal(pokemon.evs.getOrDefault(Stats.SPECIAL_DEFENCE).toString()).formatted(
                            Formatting.WHITE
                        )
                    )
                )
                .append("\n")
                .append(
                    Text.literal("Speed: ").formatted(Formatting.AQUA).append(
                        Text.literal(pokemon.evs.getOrDefault(Stats.SPEED).toString()).formatted(
                            Formatting.WHITE
                        )
                    )
                )
                .append("\n\n")
        ) // Adding two newlines to separate EVs and IVs

        // IVs
        statsHoverText.append(
            Text.literal("IVs: ").formatted(Formatting.GOLD)
                .append("\n")
                .append(
                    Text.literal("HP: ").formatted(Formatting.GREEN).append(
                        Text.literal(pokemon.ivs.getOrDefault(Stats.HP).toString()).formatted(
                            Formatting.WHITE
                        )
                    )
                )
                .append("\n")
                .append(
                    Text.literal("Attack: ").formatted(Formatting.RED).append(
                        Text.literal(pokemon.ivs.getOrDefault(Stats.ATTACK).toString()).formatted(
                            Formatting.WHITE
                        )
                    )
                )
                .append("\n")
                .append(
                    Text.literal("Defense: ").formatted(Formatting.GOLD).append(
                        Text.literal(pokemon.ivs.getOrDefault(Stats.DEFENCE).toString()).formatted(
                            Formatting.WHITE
                        )
                    )
                )
                .append("\n")
                .append(
                    Text.literal("Sp. Attack: ").formatted(Formatting.LIGHT_PURPLE).append(
                        Text.literal(pokemon.ivs.getOrDefault(Stats.SPECIAL_ATTACK).toString()).formatted(
                            Formatting.WHITE
                        )
                    )
                )
                .append("\n")
                .append(
                    Text.literal("Sp. Defense: ").formatted(Formatting.YELLOW).append(
                        Text.literal(pokemon.ivs.getOrDefault(Stats.SPECIAL_DEFENCE).toString()).formatted(
                            Formatting.WHITE
                        )
                    )
                )
                .append("\n")
                .append(
                    Text.literal("Speed: ").formatted(Formatting.AQUA).append(
                        Text.literal(pokemon.ivs.getOrDefault(Stats.SPEED).toString()).formatted(
                            Formatting.WHITE
                        )
                    )
                )
        )
        val statsList = statsText.getWithStyle(
            statsText.style.withHoverEvent(
                HoverEvent(
                    HoverEvent.Action.SHOW_TEXT,
                    statsHoverText
                )
            )
        )
        statsList.forEach(Consumer { text: Text? ->
            toSend.append(
                text
            )
        })
        val movesText = Text.literal(" [Moves]").formatted(Formatting.RED)
        val movesHoverText = Text.literal("")
        val movesHoverTextList = Text.literal("Moves").formatted(Formatting.RED).getWithStyle(
            Style.EMPTY.withUnderline(true)
        )

        movesHoverTextList.add(Text.literal("\n"))
        val moveOne = if (pokemon.moveSet.getMoves().size >= 1) pokemon.moveSet[0]!!.displayName.string else "Empty"
        val moveTwo = if (pokemon.moveSet.getMoves().size >= 2) pokemon.moveSet[1]!!.displayName.string else "Empty"
        val moveThree = if (pokemon.moveSet.getMoves().size >= 3) pokemon.moveSet[2]!!.displayName.string else "Empty"
        val moveFour = if (pokemon.moveSet.getMoves().size >= 4) pokemon.moveSet[3]!!.displayName.string else "Empty"
        movesHoverTextList.add(
            Text.literal("Move 1: ").formatted(Formatting.RED).append(
                Text.literal(moveOne).formatted(
                    Formatting.WHITE
                )
            )
        )
        movesHoverTextList.add(Text.literal("\n"))
        movesHoverTextList.add(
            Text.literal("Move 2: ").formatted(Formatting.RED).append(
                Text.literal(moveTwo).formatted(
                    Formatting.WHITE
                )
            )
        )
        movesHoverTextList.add(Text.literal("\n"))
        movesHoverTextList.add(
            Text.literal("Move 3: ").formatted(Formatting.RED).append(
                Text.literal(moveThree).formatted(
                    Formatting.WHITE
                )
            )
        )
        movesHoverTextList.add(Text.literal("\n"))
        movesHoverTextList.add(
            Text.literal("Move 4: ").formatted(Formatting.RED).append(
                Text.literal(moveFour).formatted(
                    Formatting.WHITE
                )
            )
        )
        movesHoverTextList.forEach(Consumer { text: Text? ->
            movesHoverText.append(
                text
            )
        })
        val movesList = movesText.getWithStyle(
            movesText.style.withHoverEvent(
                HoverEvent(
                    HoverEvent.Action.SHOW_TEXT,
                    movesHoverText
                )
            )
        )
        movesList.forEach(Consumer { text: Text? ->
            toSend.append(
                text
            )
        })
        return toSend
    }

    fun getPartyHoverText(toSend: MutableText, pokemon: Pokemon): Text {
        // Display name and Shiny status
        val infoHoverText =
            Text.literal("Info:").formatted(Formatting.DARK_GREEN)
                .fillStyle(Style.EMPTY)
        if (pokemon.shiny) {
            infoHoverText.append(Text.literal(" ★").formatted(Formatting.GOLD))
        }
        infoHoverText.append(Text.literal("\n"))

        // Level, Nature, Ability, Form, Gender, Ball
        infoHoverText.append(
            Text.literal("Level: ").formatted(Formatting.RED).append(
                Text.literal(pokemon.level.toString()).formatted(Formatting.WHITE)
            )
        )
        infoHoverText.append(Text.literal("\n"))
        infoHoverText.append(
            Text.literal("Nature: ").formatted(Formatting.GOLD).append(
                lang(
                    pokemon.nature.displayName.replace(
                        "cobblemon.",
                        ""
                    )
                ).formatted(Formatting.WHITE)
            )
        )
        infoHoverText.append(Text.literal("\n"))
        infoHoverText.append(
            Text.literal("Ability: ").formatted(Formatting.YELLOW).append(
                lang(
                    pokemon.ability.displayName.replace(
                        "cobblemon.",
                        ""
                    )
                ).formatted(Formatting.WHITE)
            )
        )
        infoHoverText.append(Text.literal("\n"))
        infoHoverText.append(
            Text.literal("Form: ").formatted(Formatting.GREEN).append(
                Text.literal(pokemon.form.name).formatted(Formatting.WHITE)
            )
        )
        infoHoverText.append(Text.literal("\n"))
        val genderText = formatGenderName(pokemon.gender.name)
        infoHoverText.append(
            Text.literal("Gender: ").formatted(Formatting.AQUA)
                .append(Text.literal(genderText).formatted(Formatting.WHITE))
        )
        infoHoverText.append(Text.literal("\n"))
        val formattedBallName =
            formatBallName(pokemon.caughtBall.name.toString().replace("cobblemon:", ""))
        infoHoverText.append(
            Text.literal("Ball: ").formatted(Formatting.BLUE).append(
                Text.literal(formattedBallName).formatted(Formatting.WHITE)
            )
                .append("\n\n")
        )

        // EVs
        infoHoverText.append(Text.literal("EVs:\n").formatted(Formatting.GOLD))
        infoHoverText.append(
            Text.literal("HP: ").formatted(Formatting.GREEN).append(
                Text.literal(
                    pokemon.evs.getOrDefault(Stats.HP).toString()
                ).formatted(Formatting.WHITE)
            )
        )
            .append("\n")
            .append(
                Text.literal("Attack: ").formatted(Formatting.RED).append(
                    Text.literal(
                        pokemon.evs.getOrDefault(Stats.ATTACK).toString()
                    ).formatted(Formatting.WHITE)
                )
            )
            .append("\n")
            .append(
                Text.literal("Defense: ").formatted(Formatting.GOLD).append(
                    Text.literal(
                        pokemon.evs.getOrDefault(Stats.DEFENCE).toString()
                    ).formatted(Formatting.WHITE)
                )
            )
            .append("\n")
            .append(
                Text.literal("Sp. Attack: ").formatted(Formatting.LIGHT_PURPLE)
                    .append(
                        Text.literal(
                            pokemon.evs.getOrDefault(Stats.SPECIAL_ATTACK)
                                .toString()
                        ).formatted(Formatting.WHITE)
                    )
            )
            .append("\n")
            .append(
                Text.literal("Sp. Defense: ").formatted(Formatting.YELLOW).append(
                    Text.literal(
                        pokemon.evs.getOrDefault(Stats.SPECIAL_DEFENCE)
                            .toString()
                    ).formatted(Formatting.WHITE)
                )
            )
            .append("\n")
            .append(
                Text.literal("Speed: ").formatted(Formatting.AQUA).append(
                    Text.literal(
                        pokemon.evs.getOrDefault(Stats.SPEED).toString()
                    ).formatted(Formatting.WHITE)
                )
            )
            .append("\n\n")

        // IVs
        infoHoverText.append(Text.literal("IVs:\n").formatted(Formatting.GOLD))
        infoHoverText.append(
            Text.literal("HP: ").formatted(Formatting.GREEN).append(
                Text.literal(
                    pokemon.ivs.getOrDefault(Stats.HP).toString()
                ).formatted(Formatting.WHITE)
            )
        ).append(
            Text.literal("\n").append(
                Text.literal("Attack: ").formatted(Formatting.RED).append(
                    Text.literal(
                        pokemon.ivs.getOrDefault(Stats.ATTACK).toString()
                    ).formatted(Formatting.WHITE)
                )
            )
                .append("\n")
                .append(
                    Text.literal("Defense: ").formatted(Formatting.GOLD).append(
                        Text.literal(
                            pokemon.ivs.getOrDefault(Stats.DEFENCE)
                                .toString()
                        ).formatted(Formatting.WHITE)
                    )
                )
                .append("\n")
                .append(
                    Text.literal("Sp. Attack: ")
                        .formatted(Formatting.LIGHT_PURPLE).append(
                            Text.literal(
                                pokemon.ivs.getOrDefault(Stats.SPECIAL_ATTACK)
                                    .toString()
                            ).formatted(Formatting.WHITE)
                        )
                )
                .append("\n")
                .append(
                    Text.literal("Sp. Defense: ").formatted(Formatting.YELLOW)
                        .append(
                            Text.literal(
                                pokemon.ivs.getOrDefault(Stats.SPECIAL_DEFENCE)
                                    .toString()
                            ).formatted(Formatting.WHITE)
                        )
                )
                .append("\n")
                .append(
                    Text.literal("Speed: ").formatted(Formatting.AQUA).append(
                        Text.literal(
                            pokemon.ivs.getOrDefault(Stats.SPEED).toString()
                        ).formatted(Formatting.WHITE)
                    )
                )
                .append("\n\n")
        )
        // Moves
        infoHoverText.append(Text.literal("Moves:\n").formatted(Formatting.RED))
        val moveOne =
            if (pokemon.moveSet.getMoves().size >= 1) pokemon.moveSet[0]!!.displayName.string else "Empty"
        val moveTwo =
            if (pokemon.moveSet.getMoves().size >= 2) pokemon.moveSet[1]!!.displayName.string else "Empty"
        val moveThree =
            if (pokemon.moveSet.getMoves().size >= 3) pokemon.moveSet[2]!!.displayName.string else "Empty"
        val moveFour =
            if (pokemon.moveSet.getMoves().size >= 4) pokemon.moveSet[3]!!.displayName.string else "Empty"
        infoHoverText.append(
            Text.literal("Move 1: ").formatted(Formatting.RED)
                .append(Text.literal(moveOne).formatted(Formatting.WHITE))
        ).append(Text.literal("\n"))
        infoHoverText.append(
            Text.literal("Move 2: ").formatted(Formatting.RED)
                .append(Text.literal(moveTwo).formatted(Formatting.WHITE))
        ).append(Text.literal("\n"))
        infoHoverText.append(
            Text.literal("Move 3: ").formatted(Formatting.RED)
                .append(Text.literal(moveThree).formatted(Formatting.WHITE))
        ).append(Text.literal("\n"))
        infoHoverText.append(
            Text.literal("Move 4: ").formatted(Formatting.RED)
                .append(Text.literal(moveFour).formatted(Formatting.WHITE))
        )
        return Texts.join(
            toSend
                .getWithStyle(
                    Style.EMPTY.withColor(Formatting.DARK_GREEN)
                        .withHoverEvent(HoverEvent(HoverEvent.Action.SHOW_TEXT, infoHoverText))
                ), Text.of("")
        )
    }

    fun displayParty(partyStore: PlayerPartyStore): MutableText {
        val partyText = Text.literal("")
        for (i in 0..5) {
            val pokemon = partyStore.get(i)
            if (pokemon != null) {
                // Wrap Pokémon name with brackets and add hover details.
                val pokemonName = Text.literal("[" + pokemon.getDisplayName().string + "]").formatted(Formatting.GOLD)
                partyText.append(getPartyHoverText(pokemonName, pokemon))
            }
            if (i != 5) { // don't add space after the last slot
                partyText.append(Text.literal(" "))
            }
        }
        return partyText
    }

    // String Formatters
    private fun formatBallName(name: String): String {
        val parts = name.split("_".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        for (i in parts.indices) {
            parts[i] = parts[i].lowercase(Locale.getDefault()) // Convert to lowercase first
            parts[i] = parts[i][0].uppercaseChar().toString() + parts[i].substring(1)
        }
        return java.lang.String.join(" ", *parts)
    }

    private fun formatGenderName(gender: String): String {
        return gender[0].uppercaseChar().toString() + gender.substring(1).lowercase(Locale.getDefault())
    }
}

