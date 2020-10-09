package rip.bolt.lobby.listener;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.hanging.HangingPlaceEvent;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.scoreboard.Team;

import com.sk89q.minecraft.util.commands.ChatColor;

import rip.bolt.lobby.LobbyPlugin;
import rip.bolt.lobby.manager.ModifyLobbyManager;

public class ModifyLobbyListener implements Listener {

    private Team team;
    private ModifyLobbyManager modifyLobbyManager = LobbyPlugin.getInstance().getModifyLobbyManager();

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (!modifyLobbyManager.canModifyLobby(event.getPlayer()))
            event.setCancelled(true);
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (!modifyLobbyManager.canModifyLobby(event.getPlayer()))
            event.setCancelled(true);
    }

    @EventHandler
    public void onArmourStandEdit(PlayerArmorStandManipulateEvent event) {
        if (!modifyLobbyManager.canModifyLobby(event.getPlayer()))
            event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (!modifyLobbyManager.canModifyLobby(event.getPlayer()))
            event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerInteract(PlayerInteractEntityEvent event) {
        if (event.getRightClicked().getType() == EntityType.ITEM_FRAME && !modifyLobbyManager.canModifyLobby(event.getPlayer()))
            event.setCancelled(true);
    }

    @EventHandler
    public void onHangingPlace(HangingPlaceEvent event) {
        if (!modifyLobbyManager.canModifyLobby(event.getPlayer()))
            event.setCancelled(true);
    }

    @EventHandler
    public void onHangingPlace(HangingBreakByEntityEvent event) {
        if (event.getEntityType() == EntityType.ITEM_FRAME) {
            if (!(event.getRemover() instanceof Player))
                return;

            Player player = (Player) event.getRemover();
            if (!modifyLobbyManager.canModifyLobby(player))
                event.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player))
            return;

        Player player = (Player) event.getDamager();
        if (!modifyLobbyManager.canModifyLobby(player))
            event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerFall(EntityDamageEvent event) {
        if (event.getCause() == DamageCause.FALL && event.getEntity() instanceof Player)
            event.setCancelled(true);
    }

    @EventHandler
    public void onRaining(WeatherChangeEvent event) {
        if (event.toWeatherState())
            event.setCancelled(true);
    }

    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onDropItem(PlayerDropItemEvent event) {
        if (!modifyLobbyManager.canModifyLobby(event.getPlayer()))
            event.setCancelled(true);
    }

    @EventHandler
    public void onPickupItem(PlayerPickupItemEvent event) {
        if (!modifyLobbyManager.canModifyLobby(event.getPlayer()))
            event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (team == null) { // scoreboard manager loads after plugins
            team = Bukkit.getServer().getScoreboardManager().getMainScoreboard().getTeam("team");
            if (team == null)
                team = Bukkit.getServer().getScoreboardManager().getMainScoreboard().registerNewTeam("team");
            team.setPrefix(ChatColor.AQUA + "");
        }

        Bukkit.getScheduler().runTaskLater(LobbyPlugin.getInstance(), new Runnable() {

            @Override
            public void run() {
                // run a tick later due to some weird bug
                // reset colour after so chat message isn't coloured
                event.getPlayer().setPlayerListName(ChatColor.AQUA + event.getPlayer().getName() + ChatColor.RESET);
                event.getPlayer().setDisplayName(ChatColor.AQUA + event.getPlayer().getName() + ChatColor.RESET);
            }

        }, 1);

        team.addEntry(event.getPlayer().getName());
        event.getPlayer().teleport(new Location(Bukkit.getWorld("world"), -256.5,4,93.5,-34,-5));
        modifyLobbyManager.setModifyLobby(event.getPlayer(), false);
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        modifyLobbyManager.removeModifyLobby(event.getPlayer());
    }

}
