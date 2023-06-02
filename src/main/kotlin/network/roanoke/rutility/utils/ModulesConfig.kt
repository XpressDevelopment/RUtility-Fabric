package network.roanoke.rutility.utils

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import net.fabricmc.loader.api.FabricLoader
import network.roanoke.rutility.RUtility
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.FileWriter

class ModulesConfig(private val main: RUtility) {

    fun createFolder() {
        val folderPath = FabricLoader.getInstance().configDir.resolve("rutility")
        val folder = folderPath.toFile()
        if (!folder.exists())
            folder.mkdir()
    }

    private fun getModuleFile(): File {
        val savePath = FabricLoader.getInstance().configDir.resolve("rutility/modules.json")
        val saveFile = savePath.toFile()
        saveFile.createNewFile()
        return saveFile
    }

    fun saveModules() {
        val gson = GsonBuilder().setPrettyPrinting().create()
        val file = getModuleFile()

        FileWriter(file).use {
            gson.toJson(main.moduleStatus, it)
        }
    }

    fun loadModules() {
        val gson = Gson()
        val typeToken = object : TypeToken<MutableMap<String, Boolean>>() {}.type
        val filePath = FabricLoader.getInstance().configDir.resolve("rutility/modules.json")
        val file = filePath.toFile()
        if (!file.exists())
            return
        val reader = FileReader(file)
        main.setModulesMap(gson.fromJson(reader, typeToken))
    }

}