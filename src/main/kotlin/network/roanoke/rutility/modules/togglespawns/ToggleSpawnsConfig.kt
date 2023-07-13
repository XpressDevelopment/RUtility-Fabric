package network.roanoke.rutility.modules.togglespawns

import com.cobblemon.mod.common.util.fromJson
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import net.fabricmc.loader.api.FabricLoader
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.util.*
import kotlin.collections.ArrayList

class ToggleSpawnsConfig(private val module: ToggleSpawns) {

    init {
        createFolders()
    }

    private fun createFolders() {
        var folderPath = FabricLoader.getInstance().configDir.resolve("rutility")
        var folder = folderPath.toFile()
        if (!folder.exists())
            folder.mkdir()
        folderPath = FabricLoader.getInstance().configDir.resolve("rutility/togglespawns")
        folder = folderPath.toFile()
        if (!folder.exists())
            folder.mkdir()
    }

    private fun getToggledPlayersFile(): File {
        val savePath = FabricLoader.getInstance().configDir.resolve("rutility/togglespawns/toggledPlayers.json")
        val saveFile = savePath.toFile()
        if (!saveFile.exists())
            saveFile.createNewFile()
        return saveFile
    }

    fun saveToggledPlayers() {
        val gson = GsonBuilder().setPrettyPrinting().create()
        val file = getToggledPlayersFile()

        FileWriter(file).use {
            gson.toJson(module.getToggledPlayers(), it)
        }
    }

    fun loadToggledPlayers() {
        val gson = Gson()
        val file = getToggledPlayersFile()
        FileReader(file).use {
            val typeToken = object : TypeToken<ArrayList<UUID>>() {}.type
            val list = gson.fromJson<ArrayList<UUID>>(it, typeToken)
            if (list != null)
                module.setToggledPlayers(list)
        }
    }

}