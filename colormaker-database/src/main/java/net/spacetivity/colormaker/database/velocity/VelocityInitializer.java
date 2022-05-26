package net.spacetivity.colormaker.database.velocity;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import lombok.Getter;
import net.spacetivity.colormaker.database.DatabaseRepository;

import java.nio.file.Path;
import java.sql.SQLException;
import java.util.logging.Logger;

@Plugin(id = "networkcolormakerdatabase", name = "NetworkColorMaker", version = "1.0", authors = {"TGamings"})
@Getter
public class VelocityInitializer {

    private final ProxyServer server;
    private final Logger logger;
    private DatabaseRepository repository;

    private final Path dataDirectory;

    @Inject
    public VelocityInitializer(ProxyServer server, Logger logger, @DataDirectory Path dataDirectory) {
        this.server = server;
        this.logger = logger;
        this.dataDirectory = dataDirectory;
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        this.repository = new DatabaseRepository(dataDirectory.toString());
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
