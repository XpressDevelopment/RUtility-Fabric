package network.roanoke.rutility.modules.shinyalert

import com.cobblemon.mod.common.pokemon.Pokemon
import network.roanoke.rutility.RModule
import network.roanoke.rutility.RUtility
import network.roanoke.rutility.modules.shinyalert.events.CaptureEvent
import network.roanoke.rutility.modules.shinyalert.events.FaintEvent
import network.roanoke.rutility.modules.shinyalert.events.ServerTickHandler
import network.roanoke.rutility.modules.shinyalert.events.SpawnEvent

class ShinyAlert(override val main: RUtility, override val name: String) : RModule {
    private var enabled = false
    private val _faintedPokemon: MutableMap<String, Int> = mutableMapOf()
    val faintedPokemon: MutableMap<String, Int>
        get() = _faintedPokemon

    init {
        SpawnEvent(this)
        FaintEvent(this)
        CaptureEvent(this)
        ServerTickHandler(this)
    }

    override fun isEnabled(): Boolean {
        return enabled
    }

    override fun enable(enabled: Boolean) {
        this.enabled = enabled
        main.setModuleStatus(name, enabled)
    }
}