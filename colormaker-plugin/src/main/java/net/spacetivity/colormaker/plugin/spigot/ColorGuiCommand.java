package net.spacetivity.colormaker.plugin.spigot;

import net.spacetivity.colormaker.plugin.spigot.inventory.ColorSelectionInventory;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ColorGuiCommand implements CommandExecutor {

    private final SpigotInitializer plugin;

    public ColorGuiCommand(SpigotInitializer plugin) {
        this.plugin = plugin;
        PluginCommand command = plugin.getCommand("colorgui");
        if (command != null) {
            command.setExecutor(this);
        }
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) return true;

        String commandPermission = plugin.getConfigurationFile().getColorGuiCommandPermission();

        if (!player.hasPermission(commandPermission)) {
            player.sendMessage("§4You are lacking the permission §e" + commandPermission + " §4to execute that command!");
            return true;
        }

        ColorSelectionInventory.getInventory(player).open(player);

        return true;
    }
}
