package network.roanoke.rutility

import com.mojang.brigadier.Command
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.context.CommandContext
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
import net.minecraft.server.command.CommandManager
import net.minecraft.server.command.CommandManager.argument
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.text.Text

class CRUtility(private val main: RUtility) {

    init {
        CommandRegistrationCallback.EVENT.register(CommandRegistrationCallback { dispatcher, _, _ ->
            dispatcher.register(CommandManager.literal("rutility")
                .then(CommandManager.literal("list").requires { it.hasPermissionLevel(2) }.executes(listModules()))
                .then(CommandManager.literal("enable").requires { it.hasPermissionLevel(2) }
                    .then(argument("module", StringArgumentType.string()).executes(toggleModule(true))))
                .then(CommandManager.literal("disable").requires { it.hasPermissionLevel(2) }
                    .then(argument("module", StringArgumentType.string()).executes(toggleModule(false))))
            )
        })
    }

    private fun listModules(): Command<ServerCommandSource> {
        return Command { context: CommandContext<ServerCommandSource> ->
            val source = context.source

            source.sendMessage(Text.literal("§eLoaded Modules: §7(§aEnabled§7/§cDisabled§7)"))
            main.getModuleNames().forEach {
                val module = main.getModuleFromName(it)!!
                val enabled = module.isEnabled()
                source.sendMessage(Text.literal("§7- ${if (enabled) "§a" else "§c"}${module.name}"))
            }
            1
        }
    }

    private fun toggleModule(enabled: Boolean): Command<ServerCommandSource> {
        return Command { context: CommandContext<ServerCommandSource> ->
            val source = context.source
            val moduleName = StringArgumentType.getString(context, "module") // Get the value of the "module" argument

            val module = main.getModuleFromName(moduleName)

            if (module == null) {
                source.sendFeedback(Text.literal("§cModule $moduleName does not exist"), true)
                return@Command 0
            }

            module.enable(enabled)
            source.sendFeedback(Text.literal(if (enabled) "§aEnabled module: $moduleName" else "§cDisabled module: $moduleName"), true)

            main.modulesConfig.saveModules()
            1
        }
    }

}