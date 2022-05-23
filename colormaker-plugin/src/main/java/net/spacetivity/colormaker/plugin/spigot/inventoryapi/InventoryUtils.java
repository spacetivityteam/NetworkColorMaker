package net.spacetivity.colormaker.plugin.spigot.inventoryapi;

import net.kyori.adventure.text.Component;
import net.spacetivity.colormaker.plugin.spigot.inventoryapi.content.InventoryContents;
import net.spacetivity.colormaker.plugin.spigot.inventoryapi.content.Pagination;
import net.spacetivity.colormaker.plugin.spigot.inventoryapi.content.SlotPos;
import net.spacetivity.colormaker.plugin.spigot.item.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class InventoryUtils {

    public static String title(String mainPageName) {
        return ChatColor.DARK_GRAY + mainPageName;
    }

    public static String subTitle(String mainPageTitle, String page) {
        return title(mainPageTitle) + ChatColor.GRAY + "/" + ChatColor.GRAY + page;
    }

    public static void fillRectangle(InventoryContents contents, SlotPos start, SlotPos end, ClickableItem item) {
        fillRectangle(contents, start.getRow(), start.getColumn(), end.getRow(), end.getColumn(), item);
    }

    public static void fillRectangle(InventoryContents contents, int fromRow, int fromColumn, int toRow, int toColumn, ClickableItem item) {
        for (int row = fromRow; row <= toRow; ++row) {
            for (int column = fromColumn; column <= toColumn; ++column) {
                contents.set(row, column, item);
            }
        }
    }

    public static void backToMainPageItem(InventoryContents contents, SlotPos slotPos, Player player, SmartInventory inventoryToOpen) {
        contents.set(slotPos, ClickableItem.of(new ItemBuilder(Material.SLIME_BALL)
                .setDisplayName("§8Back §7(Click)")
                .build(), event -> {
            inventoryToOpen.open(player);
            player.playSound(player.getLocation(), Sound.BLOCK_LEVER_CLICK, 1, 1);
        }));
    }

    public static void loadNavigators(SlotPos previousPosition, SlotPos nextPosition, Player player, SmartInventory inventory, Pagination pagination) {
        inventory.getManager().getContents(player).ifPresent(contents -> {
            if (!pagination.isFirst()) contents.set(previousPosition, previousPageItem(inventory, player, pagination));
            if (!pagination.isLast()) contents.set(nextPosition, nextPageItem(inventory, player, pagination));
        });
    }

    private static ClickableItem previousPageItem(SmartInventory inventory, Player player, Pagination pagination) {
        return ClickableItem.of(new ItemBuilder(Material.RED_BANNER).setDisplayName("§b§lPrevious").build(),
                event -> inventory.open(player, pagination.previous().getPage()));
    }

    private static ClickableItem nextPageItem(SmartInventory inventory, Player player, Pagination pagination) {
        return ClickableItem.of(new ItemBuilder(Material.GREEN_BANNER).setDisplayName("§b§lNext").build(),
                event -> inventory.open(player, pagination.next().getPage()));
    }

    public static void updateClickedItem(ItemStack itemToUpdate, boolean newValue) {
        if (itemToUpdate.getItemMeta() == null) return;
        if (itemToUpdate.getItemMeta().getLore() == null) return;
        if (itemToUpdate.getItemMeta().getLore().isEmpty()) return;

        ItemMeta itemMeta = itemToUpdate.getItemMeta();
        itemMeta.lore(generateLore(newValue));
        itemToUpdate.setItemMeta(itemMeta);
    }

    public static List<Component> generateLore(boolean value) {
        return List.of(Component.text("§8Status: " + (value ? "§aon" : "§coff")));
    }

    public static List<String> generateLoreAsStrings(boolean value) {
        return List.of("§8Status: " + (value ? "§aon" : "§coff"));
    }
}
