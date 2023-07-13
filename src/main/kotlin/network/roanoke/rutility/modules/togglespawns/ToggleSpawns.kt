package network.roanoke.rutility.modules.togglespawns

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents
import network.roanoke.rutility.RModule
import network.roanoke.rutility.RUtility
import network.roanoke.rutility.modules.togglespawns.commands.CToggleSpawns
import network.roanoke.rutility.modules.togglespawns.events.ServerTickHandler
import java.util.*
import kotlin.collections.ArrayList

class ToggleSpawns(override val main: RUtility, override val name: String) : RModule {
    private var enabled: Boolean = false

    private var toggledPlayers = ArrayList<UUID>()
    private val config: ToggleSpawnsConfig = ToggleSpawnsConfig(this)

    init {
        config.loadToggledPlayers()
        ServerTickEvents.START_SERVER_TICK.register(ServerTickHandler(main, this))
        CToggleSpawns(this)
    }

    override fun isEnabled(): Boolean {
        return enabled
    }

    override fun enable(enabled: Boolean) {
        this.enabled = enabled
        main.setModuleStatus(name, enabled)
    }

    fun togglePlayer(player: UUID) {
        if (isToggled(player)) toggledPlayers.remove(player) else toggledPlayers.add(player)
    }

    fun isToggled(player: UUID): Boolean {
        return toggledPlayers.contains(player)
    }

    fun getToggledPlayers(): ArrayList<UUID> {
        return toggledPlayers
    }

    fun setToggledPlayers(toggledPlayers: ArrayList<UUID>) {
        this.toggledPlayers = toggledPlayers
    }

    fun saveToggledPlayers() {
        config.saveToggledPlayers()
    }

}