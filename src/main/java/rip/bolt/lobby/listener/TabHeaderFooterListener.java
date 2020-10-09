package rip.bolt.lobby.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import rip.bolt.lobby.LobbyPlugin;

public class TabHeaderFooterListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        String header = ChatColor.translateAlternateColorCodes('^', LobbyPlugin.getInstance().getConfig().getString("tab.header"));
        String footer = ChatColor.translateAlternateColorCodes('^', LobbyPlugin.getInstance().getConfig().getString("tab.footer"));

        event.getPlayer().setPlayerListHeaderFooter(new TextComponent(header), new TextComponent(footer));
    }

}
