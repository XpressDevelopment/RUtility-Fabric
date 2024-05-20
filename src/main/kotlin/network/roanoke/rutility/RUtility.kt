package network.roanoke.rutility

import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents
import net.luckperms.api.LuckPerms
import net.luckperms.api.LuckPermsProvider
import net.minecraft.server.MinecraftServer
import network.roanoke.rutility.modules.battlegodmode.BattleGodMode
import network.roanoke.rutility.modules.levellock.LevelLock
import network.roanoke.rutility.modules.magikarp.Magikarp
import network.roanoke.rutility.modules.pokegift.PokeGift
import network.roanoke.rutility.modules.pocketutils.PocketUtils
import network.roanoke.rutility.modules.show.ShowParty
import network.roanoke.rutility.modules.show.ShowSlot
import network.roanoke.rutility.modules.shinyalert.ShinyAlert
import network.roanoke.rutility.modules.togglespawns.ToggleSpawns
import network.roanoke.rutility.utils.ModulesConfig
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class RUtility : ModInitializer {

    companion object {
        private lateinit var _serverInstance: MinecraftServer

        val serverInstance: MinecraftServer
            get() = _serverInstance

        var luckPerms: LuckPerms? = null

        var  LOGGER: Logger = LoggerFactory.getLogger("RUtility")

        var onCooldown: Boolean = false
    }

    private val _modules: MutableList<RModule> = mutableListOf()
    val modules: MutableList<RModule>
        get() = _modules

    private var _moduleStatus: MutableMap<String, Boolean> = mutableMapOf()
    val moduleStatus: MutableMap<String, Boolean>
        get() = _moduleStatus

    private lateinit var _modulesCfg: ModulesConfig
    val modulesConfig: ModulesConfig
        get() = _modulesCfg


    override fun onInitialize() {
        _modulesCfg = ModulesConfig(this)
        _modulesCfg.createFolder()

        CRUtility(this)
        ServerLifecycleEvents.SERVER_STARTED.register { server: MinecraftServer ->
            _serverInstance = server

            try {
                luckPerms = LuckPermsProvider.get()
            } catch (e: Exception) {
                println("LuckPerms not found.")
            }
        }

        addModules()
        _modulesCfg.loadModules()

        _modules.forEach {
            if (_moduleStatus.isNotEmpty() && _moduleStatus.containsKey(it.name))
                it.enable(_moduleStatus[it.name]!!)
            else
                it.enable(true)
        }
    }

    private fun addModules() {
        _modules.add(ShinyAlert(this, "ShinyAlert"))
        _modules.add(ToggleSpawns(this, "ToggleSpawns"))
        _modules.add(LevelLock(this, "LevelLock"))
        _modules.add(PokeGift(this,"PokeGift"))
        _modules.add(ShowParty(this,"ShowParty"))
        _modules.add(ShowSlot(this,"ShowSlot"))
        _modules.add(BattleGodMode(this,"BattleGodMode"))
        _modules.add(PocketUtils(this, "PocketUtils"))
        _modules.add(Magikarp(this, "Magikarp"))
    }

    fun getModuleNames(): MutableList<String> {
        val names = mutableListOf<String>()
        _modules.forEach {
            names.add(it.name)
        }
        return names
    }

    fun getModuleFromName(name: String): RModule? {
        _modules.forEach {
            if (it.name.equals(name, ignoreCase = true))
                return it
        }
        return null
    }

    fun setModuleStatus(module: String, status: Boolean) {
        _moduleStatus[module] = status
    }

    fun setModulesMap(map: MutableMap<String, Boolean>) {
        _moduleStatus = map
    }

}