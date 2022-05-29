package net.spacetivity.colormaker.plugin.spigot.inventory;

import net.spacetivity.colormaker.api.CacheAPI;
import net.spacetivity.colormaker.api.ColorAPI;
import net.spacetivity.colormaker.api.color.NetworkColor;
import net.spacetivity.colormaker.plugin.spigot.SpigotInitializer;
import net.spacetivity.colormaker.plugin.spigot.inventoryapi.ClickableItem;
import net.spacetivity.colormaker.plugin.spigot.inventoryapi.InventoryUtils;
import net.spacetivity.colormaker.plugin.spigot.inventoryapi.SmartInventory;
import net.spacetivity.colormaker.plugin.spigot.inventoryapi.content.*;
import net.spacetivity.colormaker.plugin.spigot.item.ItemBuilder;
import net.spacetivity.colormaker.plugin.spigot.item.SkullBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;

public class ColorInventory implements InventoryProvider {

    private Player player;
    private ColorType colorType;
    private SpigotInitializer plugin;

    public ColorInventory(Player player, ColorType colorType) {
        this.player = player;
        this.colorType = colorType;
        this.plugin = SpigotInitializer.getPlugin(SpigotInitializer.class);
    }

    public static SmartInventory getInventory(Player player, ColorType colorType) {
        return SmartInventory.builder()
                .provider(new ColorInventory(player, colorType))
                .size(6, 9)
                .title(InventoryUtils.subTitle("Choose coloring", colorType.getTitle()))
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
                        .setDisplayName("§cThere are no colors in the database & cache! :(")
                        .build()));
            } else {
                pagination.setItems(clickableItems.toArray(new ClickableItem[0]));
                pagination.setItemsPerPage(36);
                pagination.addToIterator(contents.newIterator(SlotIterator.Type.HORIZONTAL, 1, 0));
            }

            InventoryUtils.loadNavigators(SlotPos.of(0, 1), SlotPos.of(0, 7), player, getInventory(player, colorType), pagination);
        });
    }

    private void loadAllColors(Player player, Consumer<List<ClickableItem>> result) {
        List<NetworkColor> colors = CacheAPI.Color.getCachedColors();
        List<ItemBuilder> itemBuilders = new ArrayList<>();
        List<ClickableItem> items = new ArrayList<>();

        for (NetworkColor color : colors) getDisplayItem(color, itemBuilders::add);

        itemBuilders.sort(Comparator.comparingInt(value -> value.getData("isHexColor", boolean.class) ? 1 : 0));
        itemBuilders.forEach(itemBuilder -> items.add(ClickableItem.of(itemBuilder.build(), event -> {
            String colorName = itemBuilder.getData("colorName", String.class);
            ColorAPI.getPlayerAsync(player.getUniqueId(), colorPlayer -> {
                if (colorType.equals(ColorType.PRIMARY)) ColorAPI.updatePrimaryColor(colorPlayer, colorName, true);
                else ColorAPI.updateSecondaryColor(colorPlayer, colorName, true);
                Bukkit.getScheduler().runTask(plugin, () -> ColorSelectionInventory.getInventory(player).open(player));
                player.sendMessage("§7Color §f" + colorName + " §7is now your " + (colorType.equals(ColorType.PRIMARY) ? "primary" : "secondary") + " color.");
            });
        })));

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
            itemBuilder.setData("colorName", color.getColorName());

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
        itemBuilder.setData("colorName", color.getColorName());

        result.accept(itemBuilder);
    }

    private void setPlaceholders(InventoryContents contents) {
        contents.fillRow(0, ClickableItem.empty(ItemBuilder.placeHolder(Material.CYAN_STAINED_GLASS_PANE)));
        contents.fillRow(5, ClickableItem.empty(ItemBuilder.placeHolder(Material.CYAN_STAINED_GLASS_PANE)));
    }
}
