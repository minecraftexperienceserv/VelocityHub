package it.minecraftexperience.velohub.command;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import it.minecraftexperience.velohub.VeloHubPlugin;
import it.minecraftexperience.velohub.objects.Lobby;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class LobbyCommand implements SimpleCommand {
    
    private final VeloHubPlugin plugin;
    
    @Override
    public void execute(Invocation invocation) {
        CommandSource source = invocation.source();
        if (!(source instanceof Player))
            return;
        
        Player player = (Player) source;
        Lobby lobby = plugin.getLobbyManager().getLobby();
        if (lobby == null) {
            player.sendMessage(plugin.getConfig().getMessage("noLobbyAvailable"));
            return;
        }
        
        player.createConnectionRequest(lobby.getServer()).fireAndForget();
    }
}
