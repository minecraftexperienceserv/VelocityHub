package it.minecraftexperience.velohub;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import it.minecraftexperience.velohub.api.VeloHubAPI;
import it.minecraftexperience.velohub.command.LobbyCommand;
import it.minecraftexperience.velohub.command.ReloadCommand;
import it.minecraftexperience.velohub.listeners.PlayerListener;
import it.minecraftexperience.velohub.listeners.PluginMessageListener;
import it.minecraftexperience.velohub.managers.LobbyManager;
import it.minecraftexperience.velohub.utils.Config;
import lombok.Getter;

import javax.inject.Inject;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Plugin(id = "velohub", name = "VeloHub", version = "1.0", authors = {"xQuickGlare"})
public class VeloHubPlugin {

    @Getter private final Logger logger;
    @Getter private final ProxyServer server;
    @Getter private final Path folderPath;

    @Getter private Config config;
    
    @Getter private LobbyManager lobbyManager;
    
    @Inject
    public VeloHubPlugin(Logger logger, ProxyServer server, @DataDirectory Path folderPath) {
        this.logger = logger;
        this.server = server;
        this.folderPath = folderPath;
    }

    @Subscribe
    public void onProxyInitialize(ProxyInitializeEvent event) {
        if (!registerConfigs())
            return;
        
        registerInstances();
        registerListeners();
        registerCommands();

        VeloHubAPI.init(this);
    }

    /**
     * This method registers the Config files
     * @return True if loaded successfully
     */
    private boolean registerConfigs() {
        try {
            config = new Config(this, folderPath, "config");
            return true;
        } catch (IOException e) {
            logger.log(Level.SEVERE, "An error occurred while loading the config file");
            return false;
        }
    }

    /**
     * This method registers the instances
     */
    private void registerInstances() {
        lobbyManager = new LobbyManager(this);
        lobbyManager.loadLobbies();
    }

    /**
     * This method registers the listeners
     */
    private void registerListeners() {
        server.getEventManager().register(this, new PlayerListener(this));
        server.getEventManager().register(this, new PluginMessageListener(this));
    }

    /**
     * This method registers the commands
     */
    private void registerCommands() {
        server.getCommandManager().register("velohub-reload", new ReloadCommand(this));
        
        JsonObject commandInfo = config.getObject().getAsJsonObject("hub-command");
        if (!commandInfo.get("enabled").getAsBoolean())
            return;
        
        List<String> aliases = new ArrayList<>();
        for (JsonElement element : commandInfo.getAsJsonArray("aliases"))
            aliases.add(element.getAsString());

        server.getCommandManager().register(aliases.get(0), new LobbyCommand(this), Arrays.copyOfRange(aliases.toArray(new String[0]), 1, aliases.size()));
    }
    
}
