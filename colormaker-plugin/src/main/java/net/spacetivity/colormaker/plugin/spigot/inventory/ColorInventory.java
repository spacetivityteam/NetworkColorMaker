package net.spacetivity.colormaker.plugin.spigot.inventory;

import net.spacetivity.colormaker.api.CacheAPI;
import net.spacetivity.colormaker.api.color.NetworkColor;
import net.spacetivity.colormaker.plugin.spigot.SpigotInitializer;
import net.spacetivity.colormaker.plugin.spigot.inventoryapi.ClickableItem;
import net.spacetivity.colormaker.plugin.spigot.inventoryapi.InventoryUtils;
import net.spacetivity.colormaker.plugin.spigot.inventoryapi.SmartInventory;
import net.spacetivity.colormaker.plugin.spigot.inventoryapi.content.*;
import net.spacetivity.colormaker.plugin.spigot.item.ItemBuilder;
import net.spacetivity.colormaker.plugin.spigot.item.SkullBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;

public class ColorInventory implements InventoryProvider {

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
        InventoryUtils.backToMainPageItem(contents, SlotPos.of(0, 4), player, ColorSelectionInventory.getInventory(player));

        Pagination pagination = contents.pagination();

        loadAllColors(player, clickableItems -> {
            if (clickableItems.isEmpty()) {
                contents.set(SlotPos.of(2, 4), ClickableItem.empty(new ItemBuilder(Material.BARRIER)
                        .setDisplayName("§cYou dont have any friends! :(")
                        .build()));
            } else {
                pagination.setItems(clickableItems.toArray(new ClickableItem[0]));
                pagination.setItemsPerPage(36);
                pagination.addToIterator(contents.newIterator(SlotIterator.Type.HORIZONTAL, 1, 0));
            }

            InventoryUtils.loadNavigators(SlotPos.of(0, 1), SlotPos.of(0, 7), player, getInventory(player), pagination);
        });
    }

    private void loadAllColors(Player player, Consumer<List<ClickableItem>> result) {
        List<NetworkColor> colors = CacheAPI.Color.getCachedColors();
        List<ItemBuilder> itemBuilders = new ArrayList<>();
        List<ClickableItem> items = new ArrayList<>();

        for (NetworkColor color : colors) getDisplayItem(color, itemBuilders::add);

        itemBuilders.sort(Comparator.comparingInt(value -> value.getData("isHexColor", boolean.class) ? 0 : 1));
        itemBuilders.forEach(itemBuilder -> items.add(ClickableItem.empty(itemBuilder.build())));
        result.accept(items);
    }

    public void getDisplayItem(NetworkColor color, Consumer<ItemBuilder> result) {
        if (color.isUseHexadecimal()) {
            ItemBuilder itemBuilder = new ItemBuilder(Material.valueOf(plugin.getConfigurationFile().getCustomColorDisplayItem()))
                    .setDisplayNameSpigot(color.toSpigot() + color.getColorName())
                    .setLores(List.of("§7Permission: §b" + color.getPermission(), " ", color.isUseHexadecimal() ? "§8§oCustom" : "§8§oChatColor"))
                    .addItemFlag(ItemFlag.HIDE_ATTRIBUTES)
                    .addItemFlag(ItemFlag.HIDE_DYE);

            itemBuilder.setData("isHexColor", true);

            result.accept(itemBuilder);
            return;
        }

        ItemBuilder itemBuilder = new SkullBuilder()
                .setOwnerWithGameProfile(plugin.getValueForChatColor(color.getColorName()))
                .setDisplayNameSpigot(color.toSpigot() + color.getColorName())
                .setLores(List.of("§7Permission: §b" + color.getPermission(), " ", color.isUseHexadecimal() ? "§8§oCustom" : "§8§oChatColor"))
                .addItemFlag(ItemFlag.HIDE_ATTRIBUTES)
                .addItemFlag(ItemFlag.HIDE_DYE);

        itemBuilder.setData("isHexColor", false);

        result.accept(itemBuilder);
    }

    private void setPlaceholders(InventoryContents contents) {
        contents.fillRow(0, ClickableItem.empty(ItemBuilder.placeHolder(Material.LIGHT_BLUE_STAINED_GLASS_PANE)));
        contents.fillRow(5, ClickableItem.empty(ItemBuilder.placeHolder(Material.LIGHT_BLUE_STAINED_GLASS_PANE)));
    }
}
