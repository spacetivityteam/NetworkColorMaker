package net.spacetivity.colormaker.plugin.spigot.inventory;

import net.spacetivity.colormaker.api.CacheAPI;
import net.spacetivity.colormaker.api.color.NetworkColor;
import net.spacetivity.colormaker.plugin.spigot.SpigotInitializer;
import net.spacetivity.colormaker.plugin.spigot.inventoryapi.ClickableItem;
import net.spacetivity.colormaker.plugin.spigot.inventoryapi.InventoryUtils;
import net.spacetivity.colormaker.plugin.spigot.inventoryapi.SmartInventory;
import net.spacetivity.colormaker.plugin.spigot.inventoryapi.content.InventoryContents;
import net.spacetivity.colormaker.plugin.spigot.inventoryapi.content.InventoryProvider;
import net.spacetivity.colormaker.plugin.spigot.inventoryapi.content.SlotPos;
import net.spacetivity.colormaker.plugin.spigot.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ColorInventory implements InventoryProvider {

    private final int[] availableSlots = new int[]{10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25};

    private Player player;
    private SpigotInitializer plugin;

    public ColorInventory(Player player) {
        this.player = player;
        this.plugin = SpigotInitializer.getPlugin(SpigotInitializer.class);
    }

    public static SmartInventory getInventory(Player player) {
        return SmartInventory.builder()
                .provider(new ColorInventory(player))
                .size(6, 9)
                .title(InventoryUtils.title("Friends"))
                .build();
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        setPlaceholders(contents);

        List<SlotPos> slots = new ArrayList<>();

        for (int row = 1; row <= 4; ++row) {
            for (int column = 1; column <= 7; ++column) {
                slots.add(SlotPos.of(row, column));
            }
        }

        List<NetworkColor> networkColors = new ArrayList<>(CacheAPI.Colors.getCachedColors().stream().toList());
        networkColors.sort(Comparator.comparingInt(value -> value.isUseHexadecimal() ? 1 : 0));

        for (int i = 0; i <= networkColors.size()-1; i++) {
            NetworkColor color = networkColors.get(i);
            SlotPos slot = slots.get(i);
            contents.set(slot, ClickableItem.empty(plugin.getDisplayItem(color)));
        }
    }

    private void setPlaceholders(InventoryContents contents) {
        contents.fill(ClickableItem.empty(new ItemBuilder(Material.CYAN_STAINED_GLASS_PANE).setDisplayName(" ").build()));
        contents.set(0, 0, ClickableItem.empty(new ItemBuilder(Material.BLUE_STAINED_GLASS_PANE).setDisplayName(" ").build()));
        contents.set(0, 8, ClickableItem.empty(new ItemBuilder(Material.BLUE_STAINED_GLASS_PANE).setDisplayName(" ").build()));
        contents.set(5, 0, ClickableItem.empty(new ItemBuilder(Material.BLUE_STAINED_GLASS_PANE).setDisplayName(" ").build()));
        contents.set(5, 8, ClickableItem.empty(new ItemBuilder(Material.BLUE_STAINED_GLASS_PANE).setDisplayName(" ").build()));
    }
}
