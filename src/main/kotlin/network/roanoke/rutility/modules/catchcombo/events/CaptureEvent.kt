package network.roanoke.rutility.modules.catchcombo.events

import com.cobblemon.mod.common.api.Priority
import com.cobblemon.mod.common.api.events.CobblemonEvents
import com.cobblemon.mod.common.pokemon.IVs
import net.minecraft.text.Text
import network.roanoke.rutility.modules.catchcombo.CatchCombo
import network.roanoke.rutility.modules.catchcombo.ComboCategory

class CaptureEvent(private val module: CatchCombo) {
    init {
        CobblemonEvents.POKEMON_CAPTURED.subscribe(priority = Priority.NORMAL) { it ->
            if (!module.isEnabled())
                return@subscribe

            val comboAmount = module.comboAmount
            val comboPokemon = module.comboPokemon
            val uuid = it.player.uuid

            if (!comboAmount.containsKey(uuid) || comboPokemon[uuid] != it.pokemon.species.name) {
                comboAmount[uuid] = 1
                comboPokemon[uuid] = it.pokemon.species.name
            } else if (comboPokemon[uuid] == it.pokemon.species.name) {
                comboAmount[uuid] = comboAmount[uuid]!!.plus(1)
            }

            it.player.sendMessage(it.pokemon.displayName.append(Text.literal(" Catch Combo: ${comboAmount[uuid]}")))

            // Apply combo effects

            val combo = comboAmount[uuid]
            var category: ComboCategory? = null
            module.comboCategories.forEach { cat ->
                if (cat.inCategory(combo!!))
                    category = cat
            }

            if (category == null)
                return@subscribe

            module.comboCategory[uuid] = category!!

            var perfectIvs = category!!.getPerfectIvs()
            if (it.pokemon.species.name.lowercase() == "ditto")
                perfectIvs = if (perfectIvs == 0) 0 else perfectIvs - 1

            val ivs = IVs.createRandomIVs(perfectIvs)
            ivs.forEach {iv ->
                it.pokemon.ivs[iv.key] = iv.value
            }

            module.comboConfig.savePlayerCombo(uuid)

        }
    }

}