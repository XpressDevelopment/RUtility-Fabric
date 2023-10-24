package network.roanoke.rutility.modules.catchcombo

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents
import network.roanoke.rutility.modules.catchcombo.events.ServerTickHandler
import network.roanoke.rutility.RModule
import network.roanoke.rutility.RUtility
import network.roanoke.rutility.modules.catchcombo.commands.Combo
import network.roanoke.rutility.modules.catchcombo.events.CaptureEvent
import network.roanoke.rutility.modules.catchcombo.events.PlayerJoin
import network.roanoke.rutility.modules.catchcombo.events.SpawnEvent
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
        ComboCategory(0, 10, 1.1, 0, 0.0),
        ComboCategory(11, 20, 1.5, 2, 2.0),
        ComboCategory(21, 30, 2.0, 3, 4.0),
        ComboCategory(31, Int.MAX_VALUE, 3.0, 4, 6.0)
        //ComboCategory(31, Int.MAX_VALUE, 3.0, 4, 8192.0)
    )

    private val _comboConfig: ComboConfig = ComboConfig(this)
    val comboConfig: ComboConfig
        get() = _comboConfig

    init {
        Combo(this)
        _comboConfig.createFolders()
        CaptureEvent(this)
        SpawnEvent(this)
        ServerPlayConnectionEvents.JOIN.register(PlayerJoin(this))
        //ServerTickEvents.START_SERVER_TICK.register(ServerTickHandler(main, this))
        _comboConfig.loadPlayerCombos()
    }

    override fun isEnabled(): Boolean {
        return enabled
    }

    override fun enable(enabled: Boolean) {
        this.enabled = enabled
        main.setModuleStatus(name, enabled)
    }

    fun setComboAmount(uuid: UUID, amount: Int) {
        _comboAmount[uuid] = amount
    }

    fun setComboSpecies(uuid: UUID, pokemon: String) {
        _comboPokemon[uuid] = pokemon
    }

    fun getPlayerCombo(uuid: UUID): MutableMap<String, Int> {
        val combo = mutableMapOf<String, Int>()
        if (comboPokemon[uuid].isNullOrBlank()) {
            combo["null"] = 0
        } else {
            combo[comboPokemon[uuid]!!] = comboAmount[uuid]!!
        }
        return combo
    }
}