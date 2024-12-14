package com.thenolle.quicksort

import com.thenolle.quicksort.commands.SortCommand
import com.thenolle.quicksort.listeners.PlayerQuitListener
import com.thenolle.quicksort.utils.TaskManager
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin

class QuickSortPlugin : JavaPlugin() {
    val logger = Bukkit.getConsoleSender()
    val taskManager = TaskManager(this)

    override fun onEnable() {
        val sortCommand = SortCommand(this)
        getCommand("quicksort")?.apply {
            setExecutor(sortCommand)
            tabCompleter = sortCommand
        }

        server.pluginManager.registerEvents(PlayerQuitListener(this), this)

        logger.sendMessage("QuickSort plugin enabled")
    }

    override fun onDisable() {
        taskManager.cancelAllTasks()

        logger.sendMessage("QuickSort plugin disabled")
    }
}
