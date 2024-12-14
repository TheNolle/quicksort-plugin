package com.thenolle.quicksort.utils

import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryType
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack

object Sorter {
    private val containerTypes = setOf(
        InventoryType.BARREL,
        InventoryType.CHEST,
        InventoryType.DISPENSER,
        InventoryType.DROPPER,
        InventoryType.HOPPER,
        InventoryType.SHULKER_BOX
    )

    fun sortInventory(inventory: Inventory): Pair<Boolean, String> {
        if (inventory.type !in containerTypes) {
            return false to "This inventory type is not supported."
        }

        val (success, sortedItems) = performSorting(inventory.storageContents)
        if (success) {
            inventory.storageContents = sortedItems
            return true to "Inventory sorted successfully."
        }
        return false to "An error occurred while sorting the inventory."
    }

    fun sortPlayerInventory(player: Player): Pair<Boolean, String> {
        val inventory = player.inventory

        val armorContents = inventory.armorContents
        val offHandItem = inventory.itemInOffHand

        val (success, sortedItems) = performSorting(inventory.storageContents)
        if (success) {
            inventory.storageContents = sortedItems
            inventory.armorContents = armorContents
            inventory.setItemInOffHand(offHandItem)
            return true to "Player inventory sorted successfully."
        }
        return false to "An error occurred while sorting the player's inventory."
    }

    private fun performSorting(contents: Array<ItemStack?>): Pair<Boolean, Array<ItemStack>> {
        val items = contents.filterNotNull()

        val stackedItems = items.groupBy { item ->
            Triple(item.type, item.itemMeta?.displayName() ?: "", item.itemMeta)
        }.flatMap { (key, stacks) ->
            val totalAmount = stacks.sumOf { it.amount }
            val maxStackSize = key.first.maxStackSize

            val result = mutableListOf<ItemStack>()
            var remaining = totalAmount

            while (remaining > 0) {
                val stackSize = remaining.coerceAtMost(maxStackSize)
                val newItem = ItemStack(key.first).apply {
                    itemMeta = key.third
                    amount = stackSize
                }
                result.add(newItem)
                remaining -= stackSize
            }
            result
        }

        val sortedItems = stackedItems.sortedWith(compareBy({ it.type.name }, { it.amount }))
        val adjustedItems = Array(contents.size) { ItemStack(Material.AIR) }
        sortedItems.forEachIndexed { index, item -> adjustedItems[index] = item }

        return true to adjustedItems
    }
}