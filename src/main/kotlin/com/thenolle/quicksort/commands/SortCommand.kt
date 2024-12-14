package com.thenolle.quicksort.commands

import com.thenolle.quicksort.QuickSortPlugin
import com.thenolle.quicksort.utils.Sorter
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable

class SortCommand(private val plugin: QuickSortPlugin) : CommandExecutor, TabCompleter {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender !is Player) {
            sender.sendMessage(Component.text("Only players can use this command.").color(NamedTextColor.RED))
            return true
        }

        if (args.isNotEmpty() && args[0].equals("container", ignoreCase = true)) {
            sender.sendMessage(Component.text("Please open the container you want to sort.").color(NamedTextColor.GRAY))
            plugin.taskManager.cancelTask(sender)
            val task = object : BukkitRunnable() {
                override fun run() {
                    if (sender.openInventory.topInventory.holder == null) {
                        sender.sendMessage(
                            Component.text("No container found or it's not open.").color(NamedTextColor.RED)
                        )
                        plugin.taskManager.removeTask(sender)
                        return
                    }
                    val sorted = Sorter.sortInventory(sender.openInventory.topInventory)
                    if (!sorted.first) {
                        sender.sendMessage(Component.text(sorted.second).color(NamedTextColor.RED))
                        plugin.taskManager.removeTask(sender)
                        return
                    } else {
                        sender.sendMessage(Component.text(sorted.second).color(NamedTextColor.GREEN))
                        plugin.taskManager.removeTask(sender)
                    }
                }
            }
            plugin.taskManager.addTask(sender, task)
            task.runTaskLater(plugin, 5 * 20L) // 5 seconds
        } else {
            val sorted = Sorter.sortPlayerInventory(sender)
            if (!sorted.first) {
                sender.sendMessage(Component.text(sorted.second).color(NamedTextColor.RED))
            } else {
                sender.sendMessage(Component.text(sorted.second).color(NamedTextColor.GREEN))
            }
        }
        return true
    }

    override fun onTabComplete(
        sender: CommandSender, command: Command, alias: String, args: Array<out String>
    ): List<String> {
        if (args.size == 1) {
            return listOf("container").filter { it.startsWith(args[0], ignoreCase = true) }
        }
        return emptyList()
    }
}