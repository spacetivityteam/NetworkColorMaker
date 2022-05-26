package net.spacetivity.colormaker.database.velocity;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import lombok.Getter;
import net.spacetivity.colormaker.database.DatabaseRepository;

import java.sql.SQLException;
import java.util.logging.Logger;

@Plugin(id = "networkcolormakerdatabase", name = "NetworkColorMaker", version = "1.0", authors = {"TGamings"})
@Getter
public class VelocityInitializer {

    private final ProxyServer server;
    private final Logger logger;
    private DatabaseRepository repository;

    @Inject
    public VelocityInitializer(ProxyServer server, Logger logger) {
        this.server = server;
        this.logger = logger;
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        this.repository = new DatabaseRepository(" ");
    }

    @Subscribe
    public void onProxyInitialization(ProxyShutdownEvent event) {
        try {
            this.repository.getDatabaseManager().getConnection().close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
