package rip.bolt.lobby.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.sk89q.minecraft.util.commands.CommandException;

public class Commands {

    /**
     * Throws a CommandException if console tries to run the command.
     * 
     * @param sender the sender who ran the command
     * @return a Player object if they're a player
     * @throws CommandException if console tries to run the command
     */
    public static Player checkIfSenderIsPlayer(CommandSender sender) throws CommandException {
        if (!(sender instanceof Player))
            throw new CommandException("Only players may use this command!");

        return (Player) sender;
    }

}
