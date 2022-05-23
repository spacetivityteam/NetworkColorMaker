package net.spacetivity.colormaker.plugin.spigot.setup;

import net.spacetivity.colormaker.plugin.spigot.SpigotInitializer;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
        if (!(sender instanceof Player player))
            return true;

        if (!player.hasPermission(plugin.getConfigurationFile().getCommandPermission())) {
            player.sendMessage("No permissions!");
            return true;
        }

        if (args.length == 0) {
            player.sendMessage("Use: /setup start");
        } else if (args.length == 1 && args[0].equalsIgnoreCase("start")) {
            plugin.getSetupManager().addToSetup(player.getUniqueId());
            player.sendMessage("Please choose a name for the new color.");
        }

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return null;
    }
}
