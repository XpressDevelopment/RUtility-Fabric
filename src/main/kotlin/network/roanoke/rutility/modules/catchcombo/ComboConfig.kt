package network.roanoke.rutility.modules.catchcombo

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import net.fabricmc.loader.api.FabricLoader
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.util.*

class ComboConfig(private val main: CatchCombo) {

    fun createFolders() {
        var folderPath = FabricLoader.getInstance().configDir.resolve("rutility")
        var folder = folderPath.toFile()
        if (!folder.exists())
            folder.mkdir()
        folderPath = FabricLoader.getInstance().configDir.resolve("rutility/catchcombo")
        folder = folderPath.toFile()
        if (!folder.exists())
            folder.mkdir()
        folderPath = FabricLoader.getInstance().configDir.resolve("rutility/catchcombo/saves")
        folder = folderPath.toFile()
        if (!folder.exists())
            folder.mkdir()
    }

    private fun getPlayerSave(uuid: UUID): File {
        val savePath = FabricLoader.getInstance().configDir.resolve("rutility/catchcombo/saves/$uuid.json")
        val saveFile = savePath.toFile()
        saveFile.createNewFile()
        return saveFile
    }

    fun savePlayerCombo(uuid: UUID) {
        val gson = GsonBuilder().setPrettyPrinting().create()
        val file = getPlayerSave(uuid)

        val saveMap = mutableMapOf<String, String>()
        saveMap["amount"] = main.comboAmount[uuid].toString()
        saveMap["species"] = main.comboPokemon[uuid]!!

        FileWriter(file).use {
            gson.toJson(saveMap, it)
        }
    }

    fun loadPlayerCombos() {
        val gson = Gson()
        val typeToken = object : TypeToken<Map<String, String>>() {}.type
        val folderPath = FabricLoader.getInstance().configDir.resolve("rutility/catchcombo/saves")
        val folder = folderPath.toFile()
        folder.listFiles()?.forEach {
            val uuid = UUID.fromString(it.name.split('.')[0])
            val reader = FileReader(it)
            val map: Map<String, String> = gson.fromJson(reader, typeToken)
            main.comboAmount[uuid] = map["amount"]!!.toInt()
            main.comboPokemon[uuid] = map["species"]!!

            main.comboCategories.forEach { cat ->
                if (cat.inCategory(main.comboAmount[uuid]!!))
                    main.comboCategory[uuid] = cat
            }
        }
    }

}