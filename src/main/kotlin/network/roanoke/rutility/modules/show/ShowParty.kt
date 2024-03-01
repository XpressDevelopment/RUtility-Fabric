package network.roanoke.rutility.modules.show

import com.cobblemon.mod.common.Cobblemon.storage
import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.context.CommandContext
import me.lucko.fabric.api.permissions.v0.Permissions
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
import net.minecraft.server.command.CommandManager
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import network.roanoke.rutility.RModule
import network.roanoke.rutility.RUtility
import network.roanoke.rutility.modules.show.commands.CShowParty
import network.roanoke.rutility.utils.ShowUtils
import java.util.function.Consumer


class ShowParty (override val main: RUtility, override val name: String) : RModule {
    private var enabled = false

    init {
        CShowParty(this)
    }

    override fun isEnabled(): Boolean {
        return enabled
    }

    override fun enable(enabled: Boolean) {
        this.enabled = enabled
        main.setModuleStatus(name, enabled)
    }
}

