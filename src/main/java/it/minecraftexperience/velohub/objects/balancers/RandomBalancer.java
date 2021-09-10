package it.minecraftexperience.velohub.objects.balancers;

import it.minecraftexperience.velohub.objects.Balancer;
import it.minecraftexperience.velohub.objects.Lobby;

import java.util.List;
import java.util.Random;

public class RandomBalancer implements Balancer {
    
    private final Random random = new Random();
    
    @Override
    public Lobby getLobby(List<Lobby> lobbies) {
        return lobbies.get(random.nextInt(lobbies.size()));
    }
    
}
