package network.roanoke.rutility.modules.shinyalert

import com.cobblemon.mod.common.entity.pokemon.PokemonEntity
import com.cobblemon.mod.common.pokemon.Pokemon
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents
import network.roanoke.rutility.RModule
import network.roanoke.rutility.RUtility
import network.roanoke.rutility.modules.shinyalert.events.CaptureEvent
import network.roanoke.rutility.modules.shinyalert.events.ServerTickHandler
import java.util.UUID

class ShinyAlert(override val main: RUtility, override val name: String) : RModule {
    private var enabled = false
    private val _shinyEntities: MutableMap<UUID, MutableList<Pokemon>> = mutableMapOf()
    val shinyEntities: MutableMap<UUID, MutableList<Pokemon>>
        get() = _shinyEntities

    init {
        ServerTickEvents.START_SERVER_TICK.register(ServerTickHandler(this))
        CaptureEvent(this)
    }

    override fun isEnabled(): Boolean {
        return enabled
    }

    override fun enable(enabled: Boolean) {
        this.enabled = enabled
        main.setModuleStatus(name, enabled)
    }
}