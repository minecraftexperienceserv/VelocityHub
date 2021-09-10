package it.minecraftexperience.velohub.objects.balancers;

import it.minecraftexperience.velohub.objects.Balancer;
import it.minecraftexperience.velohub.objects.Lobby;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class LowestBalancer implements Balancer {
    
    @Override
    public Lobby getLobby(List<Lobby> lobbies) {
        Lobby lowestLobby = null;
        
        for (Lobby lobby : lobbies) {
            if (lowestLobby == null || lowestLobby.getServer().getPlayersConnected().size() > lobby.getServer().getPlayersConnected().size())
                lowestLobby = lobby;
        }
        
        return lowestLobby;
    }
    
}
