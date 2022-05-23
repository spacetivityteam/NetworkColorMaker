package net.spacetivity.colormaker.plugin.spigot.setup;

import net.md_5.bungee.api.ChatColor;
import net.spacetivity.colormaker.api.CacheAPI;
import net.spacetivity.colormaker.api.ColorAPI;
import net.spacetivity.colormaker.api.color.NetworkColor;
import net.spacetivity.colormaker.plugin.spigot.SpigotInitializer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class SetupListener implements Listener {

    private final SpigotInitializer plugin;

    public SetupListener(SpigotInitializer plugin) {
        this.plugin = plugin;
        this.plugin.getServer().getPluginManager().registerEvents(this, this.plugin);
    }

    @SuppressWarnings("deprecation")
    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        String[] message = event.getMessage().replace("%", "%%").split(" ");
        SetupManager setupManager = plugin.getSetupManager();

        if (!setupManager.isProcessingSetup(player.getUniqueId())) return;

        event.setCancelled(true);

        if (message[0].equalsIgnoreCase("cancel")) {
            setupManager.getSetupPlayers().remove(player.getUniqueId());
            setupManager.getColorSetup().remove(player.getUniqueId());
            return;
        }

        NetworkColor networkColor = setupManager.currentColor(player.getUniqueId());

        switch (setupManager.getSetupCount(player.getUniqueId())) {
            case 0 -> {

                if (CacheAPI.Colors.isExisting(message[0])) {
                    player.sendMessage("This color is already present. Choose a different name!");
                    return;
                }

                networkColor.setColorName(message[0]);
                setupManager.nextSetupStep(player.getUniqueId());
                player.sendMessage("Set color name to: " + message[0]);
                player.sendMessage("Should this color be a hexadecimal color code or not. ('true' for custom colors)");
            }
            case 1 -> {
                if (!message[0].equalsIgnoreCase("true") && !message[0].equalsIgnoreCase("false")) {
                    player.sendMessage("Please use a boolean. (true / false)");
                    return;
                }
                networkColor.setUseHexadecimal(Boolean.parseBoolean(message[0]));
                setupManager.nextSetupStep(player.getUniqueId());
                player.sendMessage("Use hexadecimal encoding was set to :" + message[0]);
                player.sendMessage("Now choose the actual color code. (Hexadecimal String / ChatColor)");
            }
            case 2 -> {
                if (networkColor.isUseHexadecimal()) {
                    networkColor.setColorCode(message[0]);
                } else {
                    if (ChatColor.of(message[0]) == null) {
                        player.sendMessage("This is not a valid ChatColor. (E.g. RED, BLUE, ...");
                        return;
                    }

                    networkColor.setColorCode(message[0]);
                }
                player.sendMessage("Color code was set to: " + message[0]);
                player.sendMessage("Now choose a permission a player needs to use this color.");
                setupManager.nextSetupStep(player.getUniqueId());
            }
            case 3 -> {
                networkColor.setPermission(message[0]);
                player.sendMessage("Permission for using was set to: " + message[0]);
                player.sendMessage("Now choose if this color is a primary color.");
                setupManager.nextSetupStep(player.getUniqueId());
            }
            case 4 -> {
                if (!message[0].equalsIgnoreCase("true") && !message[0].equalsIgnoreCase("false")) {
                    player.sendMessage("Please use a boolean. (true / false)");
                    return;
                }
                networkColor.setPrimaryColor(Boolean.parseBoolean(message[0]));
                player.sendMessage("Primary color status: " + message[0]);
                player.sendMessage("Now choose if this color is a secondary color.");
                setupManager.nextSetupStep(player.getUniqueId());
            }
            case 5 -> {
                if (!message[0].equalsIgnoreCase("true") && !message[0].equalsIgnoreCase("false")) {
                    player.sendMessage("Please use a boolean. (true / false)");
                    return;
                }
                networkColor.setSecondaryColor(Boolean.parseBoolean(message[0]));
                player.sendMessage("Secondary color status: " + message[0]);
                player.sendMessage("Color (" + networkColor.getColorName() + ") successfully created.");
                ColorAPI.saveColorToDatabase(networkColor);
                CacheAPI.Colors.updateCachedColorsAsync();
                setupManager.getSetupPlayers().remove(player.getUniqueId());
                setupManager.getColorSetup().remove(player.getUniqueId());
            }
        }
    }
}
