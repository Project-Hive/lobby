package rip.bolt.lobby.manager;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Player;

public class ModifyLobbyManager {

    private Map<Player, Boolean> canModifySpawn = new HashMap<Player, Boolean>();

    public boolean setModifyLobby(Player player, boolean newVal) {
        canModifySpawn.put(player, newVal);

        return newVal;
    }

    public boolean setModifyLobby(Player player) {
        return setModifyLobby(player, !canModifyLobby(player));
    }

    public boolean canModifyLobby(Player player) {
        Boolean enabled = canModifySpawn.get(player);
        if (enabled == null)
            return false;

        return enabled;
    }

    public void removeModifyLobby(Player player) {
        canModifySpawn.remove(player);
    }

}
