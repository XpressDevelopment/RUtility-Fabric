package network.roanoke.rutility.modules.magikarp.managers

import com.cobblemon.mod.common.util.removeIf
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.server.MinecraftServer
import network.roanoke.rutility.modules.magikarp.Magikarp
import network.roanoke.rutility.modules.magikarp.objects.Magikarpd
import java.io.FileReader
import java.io.FileWriter
import java.util.LinkedList
import java.util.Queue

class MagikarpManager(private val module: Magikarp) {
    private val gson = GsonBuilder().setPrettyPrinting().create()
    private val magikarpFolder = FabricLoader.getInstance().configDir.resolve("rutility/magikarp")
    private val globalFile = FabricLoader.getInstance().configDir.resolve("rutility/magikarp/globalMagikarps.json")
    private val playerFile = FabricLoader.getInstance().configDir.resolve("rutility/magikarp/playerMagikarps.json")
    private val saveInterval = 20 * 60 * 5
    private var saveCounter = 0
    var globalQueue: Queue<Magikarpd> = LinkedList()
    var playerQueues: MutableMap<String, Queue<Magikarpd>> = mutableMapOf()

    init {
        if (!magikarpFolder.toFile().exists())
            magikarpFolder.toFile().mkdirs()

        if (!globalFile.toFile().exists()) {
            globalFile.toFile().createNewFile()
        } else {
            loadMagikarp()
        }
    }

    fun update(server: MinecraftServer) {
        if (globalQueue.isNotEmpty()) {
            val m = globalQueue.peek() // Look at the boost at the front of the queue
            if (!m.activated) {
                m.activate()
            }
            m.incrementTick()
            if (m.isDone()) {
                m.kill()
                globalQueue.remove() // Remove the boost from the front of the queue
                saveMagikarp()
            }
        }

        playerQueues.forEach { (uuid, queue) ->
            if (queue.isNotEmpty()) {
                val m = queue.peek() // Look at the boost at the front of the queue
                if (!m.activated) {
                    m.activate()
                }
                m.incrementTick()
                if (m.isDone()) {
                    m.kill()
                    queue.remove() // Remove the boost from the front of the queue
                    saveMagikarp()
                }
            }
        }

        playerQueues.removeIf { (_, queue) -> queue.isEmpty() }

        saveCounter++
        if (saveCounter > saveInterval) {
            saveCounter = 0
            saveMagikarp()
        }
    }

    fun globalMagikarpdPurchasers(): String {
        if (globalQueue.isEmpty()) { return "" }
        return globalQueue.map { it.purchaser }.toSet().joinToString(", ") + " have bought Magikarp'd in the queue!"
    }

    fun playerMagikarpdPurchasers(uuid: String): String {
        if (!playerQueues.containsKey(uuid) || playerQueues[uuid]!!.isEmpty()) { return "" }
        return playerQueues[uuid]!!.map { it.purchaser }.toSet().joinToString(", ") + " have bought Magikarp'd in the queue!"
    }

    fun createMagikarpd(duration: Int, purchaser: String, target: String) {
        val magikarpd = Magikarpd(duration, 0, purchaser, target)
        if (target == "global") {
            globalQueue.add(magikarpd)
        } else {
            if (!playerQueues.containsKey(target)) {
                playerQueues[target] = LinkedList()
            }
            playerQueues[target]!!.add(magikarpd)
        }
        saveMagikarp()
    }

    fun getGlobalMagikarpdDuration(): Int {
        return globalQueue.sumOf { it.duration - it.completedTicks }
    }

    fun getPlayerMagikarpdDuration(uuid: String): Int {
        return playerQueues[uuid]?.sumOf { it.duration - it.completedTicks } ?: 0
    }

    private fun loadMagikarp() {
        FileReader(globalFile.toFile()).use { reader ->
            val loadedMagikarpds: Array<Magikarpd> = gson.fromJson(reader, Array<Magikarpd>::class.java)
            globalQueue = LinkedList(loadedMagikarpds.asList())
            if (globalQueue.isNotEmpty()) {
                globalQueue.peek().activated = false
            }
        }

        FileReader(playerFile.toFile()).use { reader ->
            val type = object : TypeToken<MutableMap<String, Array<Magikarpd>>>() {}.type
            val loadedPlayerQueues: MutableMap<String, Array<Magikarpd>> = gson.fromJson(reader, type)
            val playerQueues = loadedPlayerQueues.mapValues { LinkedList(it.value.asList()) }.toMutableMap()
            playerQueues.forEach { (_, queue) ->
                if (queue.isNotEmpty()) {
                    queue.peek().activated = false
                }
            }
        }
    }

    private fun saveMagikarp() {
        FileWriter(globalFile.toFile()).use { writer ->
            gson.toJson(globalQueue.toTypedArray(), writer)
        }

        FileWriter(playerFile.toFile()).use { writer ->
            gson.toJson(playerQueues, writer)
        }
    }


}