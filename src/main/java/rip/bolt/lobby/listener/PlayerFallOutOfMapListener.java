package rip.bolt.lobby.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import rip.bolt.lobby.LobbyPlugin;

public class PlayerFallOutOfMapListener implements Listener {

    private double minY = LobbyPlugin.getInstance().getConfig().getDouble("teleport-to-spawn-y");

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (event.getTo().getY() <= minY)
            event.setTo(event.getPlayer().getWorld().getSpawnLocation());
    }

}
