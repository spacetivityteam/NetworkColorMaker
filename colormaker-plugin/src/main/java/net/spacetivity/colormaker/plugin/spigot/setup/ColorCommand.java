package net.spacetivity.colormaker.plugin.spigot.setup;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.*;
import net.spacetivity.colormaker.api.CacheAPI;
import net.spacetivity.colormaker.api.ColorAPI;
import net.spacetivity.colormaker.api.color.NetworkColor;
import net.spacetivity.colormaker.plugin.spigot.SpigotInitializer;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ColorCommand implements CommandExecutor, TabCompleter {

    private final SpigotInitializer plugin;

    public ColorCommand(SpigotInitializer plugin) {
        this.plugin = plugin;
        PluginCommand command = plugin.getCommand("color");
        if (command != null) {
            command.setExecutor(this);
            command.setTabCompleter(this);
        }
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) return true;

        String commandPermission = plugin.getConfigurationFile().getSetupCommandPermission();
        if (!player.hasPermission(commandPermission)) {
            player.sendMessage("§4You are lacking the permission §e" + commandPermission + " §4to execute that command!");
            return true;
        }

        if (args.length == 0) {
            player.sendMessage("§3Color §7| Color setup commands: ");
            player.sendMessage("§f> §7/color createChatColor");
            player.sendMessage("§f> §7/color createCustomColor <Name> <HexCode> [isPrimary]");

        } else if (args.length >= 3 && args[0].equalsIgnoreCase("createCustomColor")) {

            String name = args[1];

            if (CacheAPI.Color.isExisting(name)) {
                player.sendMessage("§cThis color is already stored in the database & cache!");
                return true;
            }

            if (!args[2].startsWith("#")) {
                player.sendMessage("§cThe color code has to start with a §e# §cchar!");
                return true;
            }

            String colorCode = args[2];

            NetworkColor newColor = NetworkColor.from( name,
                    colorCode,
                    true,
                    "color." + name);

            if (args.length > 3) {

                if (!args[3].equalsIgnoreCase("true") && !args[3].equalsIgnoreCase("false")) {
                    player.sendMessage("§cPlease enter a boolean! §e(true / false)");
                    return true;
                }

                boolean isPrimary = Boolean.parseBoolean(args[3]);

                boolean primaryField;
                boolean secondaryField;

                if (isPrimary && ColorAPI.getDefaultPrimaryColor().isPresent()) {
                    player.sendMessage("§cThere is already a default primary color.");
                    primaryField = false;
                } else {
                    primaryField = isPrimary;
                }

                if (!isPrimary && ColorAPI.getDefaultSecondaryColor().isPresent()) {
                    player.sendMessage("§cThere is already a default secondary color.");
                    secondaryField = false;
                } else {
                    secondaryField = !isPrimary;
                }

                newColor.setDefaultPrimaryColor(primaryField);
                newColor.setDefaultSecondaryColor(secondaryField);
            } else {
                newColor.setDefaultPrimaryColor(false);
                newColor.setDefaultSecondaryColor(false);
            }

            ColorAPI.saveColorToDatabase(newColor, true);
            player.sendMessage("§7Created color " + newColor.toSpigot() + newColor.getColorName() + " §7(Primary: §f" + newColor.isDefaultPrimaryColor() + "§7)");

        } else if (args.length == 1 && args[0].equalsIgnoreCase("createChatColor")) {

            player.sendMessage("§7Please select a ChatColor for this new color:");
            TextComponent component = new TextComponent();

            List<ChatColor> chatColors = new ArrayList<>(Arrays.stream(ChatColor.values()).toList());
            chatColors.remove(ChatColor.STRIKETHROUGH);
            chatColors.remove(ChatColor.UNDERLINE);
            chatColors.remove(ChatColor.RESET);
            chatColors.remove(ChatColor.BOLD);
            chatColors.remove(ChatColor.ITALIC);
            chatColors.remove(ChatColor.MAGIC);
            chatColors.remove(ChatColor.RED);

            for (ChatColor color : chatColors) {
                TextComponent colorComponent = new TextComponent();
                String displayName;
                if (color.getName().contains("_")) displayName = color.getName().split("_")[1];
                else displayName = color.getName();
                colorComponent.setText(color + "[" + displayName.toUpperCase() + "] §r");
                BaseComponent[] hoverText = new ComponentBuilder("Click to add " + color.getName() + " to the database.").create();
                colorComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, (hoverText)));
                colorComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/color finishChatColor " + color.getName() + " " + " color." + color.getName()));
                component.addExtra(colorComponent);
            }

            player.spigot().sendMessage(component);
        } else if (args.length == 3 && args[0].equalsIgnoreCase("finishChatColor")) {

            String name = args[1];

            if (CacheAPI.Color.isExisting(name)) {
                player.sendMessage("This color is already stored in the database & cache!");
                return true;
            }

            String permission = args[2];
            String colorCode = name.toUpperCase();

            NetworkColor color = NetworkColor.from(name, colorCode, false, permission);

            if (ColorAPI.getDefaultPrimaryColor().isPresent() && ColorAPI.getDefaultSecondaryColor().isPresent()) {
                color.setDefaultPrimaryColor(false);
                color.setDefaultSecondaryColor(false);
                ColorAPI.saveColorToDatabase(color, true);

                player.sendMessage("§7Created color " + color.toSpigot() + color.getColorName());
                return true;
            }

            player.sendMessage("§7Now choose if the color should be a primary- or secondary color? §f(true / false)");
            plugin.getSetupManager().addToSetup(player.getUniqueId(), color);
        }

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) return List.of("createChatColor", "createCustomColor");
        return null;
    }
}
