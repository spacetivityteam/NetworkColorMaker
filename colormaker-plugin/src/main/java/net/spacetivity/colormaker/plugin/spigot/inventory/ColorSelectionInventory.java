package net.spacetivity.colormaker.plugin.spigot.inventory;

import net.spacetivity.colormaker.api.ColorAPI;
import net.spacetivity.colormaker.api.color.NetworkColor;
import net.spacetivity.colormaker.plugin.spigot.SpigotInitializer;
import net.spacetivity.colormaker.plugin.spigot.inventoryapi.ClickableItem;
import net.spacetivity.colormaker.plugin.spigot.inventoryapi.InventoryUtils;
import net.spacetivity.colormaker.plugin.spigot.inventoryapi.SmartInventory;
import net.spacetivity.colormaker.plugin.spigot.inventoryapi.content.InventoryContents;
import net.spacetivity.colormaker.plugin.spigot.inventoryapi.content.InventoryProvider;
import net.spacetivity.colormaker.plugin.spigot.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.List;

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
                .title(InventoryUtils.title("Choose coloring"))
                .build();
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        contents.fillRow(0, ClickableItem.empty(ItemBuilder.placeHolder(Material.LIGHT_BLUE_STAINED_GLASS_PANE)));
        contents.fillRow(2, ClickableItem.empty(ItemBuilder.placeHolder(Material.LIGHT_BLUE_STAINED_GLASS_PANE)));

        contents.set(1, 1, ClickableItem.of(ItemBuilder.of(Material.CYAN_DYE, "§b§lPrimary Color"), event -> {
            ColorInventory.getInventory(player, ColorType.PRIMARY).open(player);
        }));

        contents.set(1, 7, ClickableItem.of(ItemBuilder.of(Material.LIGHT_BLUE_DYE, "§b§lSecondary Color"), event -> {
            ColorInventory.getInventory(player, ColorType.SECONDARY).open(player);
        }));

        NetworkColor primaryColor = ColorAPI.getPrimaryColor(player.getUniqueId());
        NetworkColor secondaryColor = ColorAPI.getSecondaryColor(player.getUniqueId());
        contents.set(1, 4, ClickableItem.empty(ItemBuilder.builder(Material.END_CRYSTAL, "§7Your colors:")
                .setLoresSpigot(List.of(
                        "§7Primary: " + primaryColor.toSpigot() + primaryColor.getColorName(),
                        "§7Secondary: " + secondaryColor.toSpigot() + secondaryColor.getColorName()
                ))
                .build()));
    }
}
