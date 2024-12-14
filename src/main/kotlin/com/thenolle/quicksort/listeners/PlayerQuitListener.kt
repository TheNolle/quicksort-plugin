package com.thenolle.quicksort.listeners

import com.thenolle.quicksort.QuickSortPlugin
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerQuitEvent

class PlayerQuitListener(private val plugin: QuickSortPlugin): Listener {
    @EventHandler
    fun onPlayerQuit(event: PlayerQuitEvent) {
        plugin.taskManager.cancelTask(event.player)
    }
}