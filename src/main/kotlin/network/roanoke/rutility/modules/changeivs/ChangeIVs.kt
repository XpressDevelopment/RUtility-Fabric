package network.roanoke.rutility.modules.changeivs

import network.roanoke.rutility.RModule
import network.roanoke.rutility.RUtility
import network.roanoke.rutility.modules.changeivs.commands.Ivs

class ChangeIVs(override val main: RUtility, override val name: String) : RModule {
    private var enabled = false

    init {
        Ivs(this)
    }

    override fun isEnabled(): Boolean {
        return enabled
    }

    override fun enable(enabled: Boolean) {
        this.enabled = enabled
    }
}