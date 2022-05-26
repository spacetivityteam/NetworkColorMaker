package net.spacetivity.colormaker.plugin.spigot.inventory;

import net.spacetivity.colormaker.plugin.spigot.SpigotInitializer;
import net.spacetivity.colormaker.plugin.spigot.inventoryapi.ClickableItem;
import net.spacetivity.colormaker.plugin.spigot.inventoryapi.InventoryUtils;
import net.spacetivity.colormaker.plugin.spigot.inventoryapi.SmartInventory;
import net.spacetivity.colormaker.plugin.spigot.inventoryapi.content.InventoryContents;
import net.spacetivity.colormaker.plugin.spigot.inventoryapi.content.InventoryProvider;
import net.spacetivity.colormaker.plugin.spigot.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class ColorSelectionInventory implements InventoryProvider {

    private Player player;
    private SpigotInitializer plugin;

    public ColorSelectionInventory(Player player) {
        this.player = player;
        this.plugin = SpigotInitializer.getPlugin(SpigotInitializer.class);
    }

    public static SmartInventory getInventory(Player player) {
        return SmartInventory.builder()
                .provider(new ColorSelectionInventory(player))
                .size(3, 9)
                .title(InventoryUtils.title("§8Set §bprimary §8or §bsecondary §8color"))
                .build();
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        contents.fillRow(0, ClickableItem.empty(ItemBuilder.placeHolder(Material.LIGHT_BLUE_STAINED_GLASS_PANE)));
        contents.fillRow(2, ClickableItem.empty(ItemBuilder.placeHolder(Material.LIGHT_BLUE_STAINED_GLASS_PANE)));
        contents.set(1, 1, ClickableItem.empty(ItemBuilder.of(Material.CYAN_DYE, "§b§lPrimary Color")));
        contents.set(1, 7, ClickableItem.empty(ItemBuilder.of(Material.LIGHT_BLUE_DYE, "§b§lSecondary Color")));
    }
}
