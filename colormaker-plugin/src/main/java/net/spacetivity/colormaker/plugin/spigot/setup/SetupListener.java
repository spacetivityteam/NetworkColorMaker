package net.spacetivity.colormaker.plugin.spigot.setup;

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

        if (!setupManager.getColorSetup().containsKey(player.getUniqueId())) return;

        event.setCancelled(true);

        if (message[0].equalsIgnoreCase("cancel")) {
            setupManager.getColorSetup().remove(player.getUniqueId());
            player.sendMessage("§cRejected current color.");
            return;
        }

        if (!message[0].equalsIgnoreCase("true") && !message[0].equalsIgnoreCase("false")) {
            player.sendMessage("§cPlease enter a boolean! §e(true / false)");
            return;
        }

        if (CacheAPI.Color.isExisting(message[0])) {
            player.sendMessage("§cThis color is already stored in the database & cache!");
            return;
        }

        boolean primary = Boolean.parseBoolean(message[0]);
        NetworkColor color = setupManager.currentColor(player.getUniqueId());
        color.setPrimaryColor(primary);
        color.setSecondaryColor(!primary);
        ColorAPI.saveColorToDatabase(color, true);

        player.sendMessage("§7Created color " + color.toSpigot() + color.getColorName() + " §7(Primary: §f" + primary + "§7)");
        setupManager.getColorSetup().remove(player.getUniqueId());
    }
}
