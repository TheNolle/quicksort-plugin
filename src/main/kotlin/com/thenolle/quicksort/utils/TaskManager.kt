package com.thenolle.quicksort.utils

import com.thenolle.quicksort.QuickSortPlugin
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable
import java.util.concurrent.ConcurrentHashMap

class TaskManager(private val plugin: QuickSortPlugin) {
    private val tasks = ConcurrentHashMap<Player, BukkitRunnable>()

    fun addTask(player: Player, task: BukkitRunnable) {
        cancelTask(player)
        tasks[player] = task
    }

    fun cancelTask(player: Player) {
        tasks[player]?.cancel()
        tasks.remove(player)
        plugin.logger.sendMessage("Task cancelled for ${player.name}")
    }

    fun removeTask(player: Player) {
        tasks.remove(player)
    }

    fun cancelAllTasks() {
        tasks.values.forEach { it.cancel() }
        tasks.clear()
        plugin.logger.sendMessage("All tasks cancelled")
    }
}