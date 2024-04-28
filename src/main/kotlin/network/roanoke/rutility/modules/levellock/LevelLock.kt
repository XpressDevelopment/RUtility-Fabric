package network.roanoke.rutility.modules.levellock

import net.fabricmc.loader.api.FabricLoader
import net.impactdev.impactor.api.economy.EconomyService
import network.roanoke.rutility.RModule
import network.roanoke.rutility.RUtility
import network.roanoke.rutility.modules.levellock.commands.LevelLockCommand
import network.roanoke.rutility.modules.levellock.events.LevelUpEvent
import network.roanoke.rutility.utils.LevelLockConfig
import java.math.BigDecimal

class LevelLock(override val main: RUtility, override val name: String) : RModule {
    private var enabled = false

    var levelLockCost: BigDecimal = 100.toBigDecimal()
    private var config: LevelLockConfig = LevelLockConfig(this)

    private var hasEconomy: Boolean = false

    init {
        LevelLockCommand(this)
        LevelUpEvent(this)
    }

    override fun isEnabled(): Boolean {
        return enabled
    }

    override fun enable(enabled: Boolean) {
        this.enabled = enabled
        main.setModuleStatus(name, enabled)

        if (FabricLoader.getInstance().isModLoaded("impactor"))
            hasEconomy = true
        else
            RUtility.LOGGER.info("Economy service not found.")
    }

    fun loadConfig() {
        config.loadConfig()
    }

    fun hasEconomy(): Boolean {
        return hasEconomy
    }
}