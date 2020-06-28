package rip.bolt.lobby.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.sk89q.minecraft.util.commands.ChatColor;
import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandException;
import com.sk89q.minecraft.util.commands.CommandPermissions;

import rip.bolt.lobby.LobbyPlugin;

public class LobbyCommands {

    @Command(aliases = { "modify" }, desc = "Toggles your ability to modify the lobby. Defaults to off upon logging in.", max = 0)
    @CommandPermissions("lobby.modify")
    public static void modify(final CommandContext cmd, CommandSender sender) throws CommandException {
        Player player = Commands.checkIfSenderIsPlayer(sender);

        boolean newVal = LobbyPlugin.getInstance().getModifyLobbyManager().setModifyLobby(player);
        player.sendMessage(ChatColor.YELLOW + "Editing the lobby is now " + (newVal ? ChatColor.GREEN + "on" : ChatColor.RED + "off"));
    }

}
