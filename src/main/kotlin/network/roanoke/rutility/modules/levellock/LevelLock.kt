package network.roanoke.rutility.modules.levellock

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
    }

    fun loadConfig() {
        config.loadConfig()
    }
}