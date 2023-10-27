package network.roanoke.rutility.utils

import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import net.fabricmc.loader.api.FabricLoader
import network.roanoke.rutility.RUtility
import network.roanoke.rutility.modules.levellock.LevelLock
import java.io.*

class LevelLockConfig(private val module: LevelLock) {

    init {
        createFolder()

        loadConfig()
    }

    private fun createFolder() {
        val folderPath = FabricLoader.getInstance().configDir.resolve("rutility/levellock")
        val folder = folderPath.toFile()
        if (!folder.exists())
            folder.mkdir()
    }

    private fun getConfigFile(): File {
        val savePath = FabricLoader.getInstance().configDir.resolve("rutility/levellock/levellockConfig.json")
        val saveFile = savePath.toFile()
        if (!saveFile.exists())
            if (saveFile.createNewFile()) {
                loadStartingConfig().let { obj ->
                    FileWriter(saveFile).use { fw ->
                        GsonBuilder().setPrettyPrinting().create().toJson(obj, fw)
                    }
                }
            }
        return saveFile
    }

    fun loadConfig() {
        val file = getConfigFile()

        FileReader(file).use {
            val jsonObject = JsonParser.parseReader(it).asJsonObject
            module.levelLockCost = jsonObject.get("levelLockCost").asBigDecimal
        }
    }

    private fun loadStartingConfig(): JsonObject? {
        val jsonStream: InputStream? = RUtility::class.java.getResourceAsStream("/levellockConfig.json")
        return jsonStream?.use {
            InputStreamReader(it).use { reader ->
                JsonParser.parseReader(reader).asJsonObject
            }
        }
    }

}