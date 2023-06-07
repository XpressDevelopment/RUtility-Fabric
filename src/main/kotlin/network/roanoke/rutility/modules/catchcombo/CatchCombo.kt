package network.roanoke.rutility.modules.catchcombo

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents
import network.roanoke.rutility.modules.catchcombo.events.ServerTickHandler
import network.roanoke.rutility.RModule
import network.roanoke.rutility.RUtility
import network.roanoke.rutility.modules.catchcombo.events.CaptureEvent
import network.roanoke.rutility.modules.catchcombo.events.PlayerJoin
import java.util.*

class CatchCombo(override val main: RUtility, override val name: String) : RModule {
    private var enabled: Boolean = false

    private val _comboAmount: MutableMap<UUID, Int> = mutableMapOf()
    val comboAmount: MutableMap<UUID, Int>
        get() = _comboAmount

    private val _comboPokemon: MutableMap<UUID, String> = mutableMapOf()
    val comboPokemon: MutableMap<UUID, String>
        get() = _comboPokemon

    private val _comboCategory: MutableMap<UUID, ComboCategory> = mutableMapOf()
    val comboCategory: MutableMap<UUID, ComboCategory>
        get() = _comboCategory


    val comboCategories: List<ComboCategory> = listOf(
        //ComboCategory(0, 10, 1.1, 0, 1.0),
        ComboCategory(11, 20, 1.5, 2, 1.4),
        ComboCategory(21, 30, 2.0, 3, 1.8),
        ComboCategory(31, Int.MAX_VALUE, 3.0, 4, 2.2)
    )

    private val _comboConfig: ComboConfig = ComboConfig(this)
    val comboConfig: ComboConfig
        get() = _comboConfig

    init {
        _comboConfig.createFolders()
        CaptureEvent(this)
        ServerPlayConnectionEvents.JOIN.register(PlayerJoin(this))
        ServerTickEvents.START_SERVER_TICK.register(ServerTickHandler(main, this))
        _comboConfig.loadPlayerCombos()
    }

    override fun isEnabled(): Boolean {
        return enabled
    }

    override fun enable(enabled: Boolean) {
        this.enabled = enabled
        main.setModuleStatus(name, enabled)
    }
}