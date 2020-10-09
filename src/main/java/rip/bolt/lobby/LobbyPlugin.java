package rip.bolt.lobby;

import org.bukkit.Bukkit;
import org.bukkit.WorldCreator;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.java.JavaPlugin;

import com.sk89q.bukkit.util.CommandsManagerRegistration;
import com.sk89q.minecraft.util.commands.CommandException;
import com.sk89q.minecraft.util.commands.CommandPermissionsException;
import com.sk89q.minecraft.util.commands.CommandUsageException;
import com.sk89q.minecraft.util.commands.CommandsManager;
import com.sk89q.minecraft.util.commands.MissingNestedCommandException;
import com.sk89q.minecraft.util.commands.WrappedCommandException;

import net.md_5.bungee.api.ChatColor;
import rip.bolt.lobby.chunk.NullChunkGenerator;
import rip.bolt.lobby.commands.LobbyCommands;
import rip.bolt.lobby.exception.PlayerNotFoundException;
import rip.bolt.lobby.listener.ModifyLobbyListener;
import rip.bolt.lobby.listener.PlayerFallOutOfMapListener;
import rip.bolt.lobby.listener.TabHeaderFooterListener;
import rip.bolt.lobby.manager.ModifyLobbyManager;
import rip.bolt.lobby.manager.ServerBrowserManager;

public class LobbyPlugin extends JavaPlugin {

    protected ModifyLobbyManager modifyLobbyManager;
    protected ServerBrowserManager serverBrowserManager;

    protected CommandsManager<CommandSender> commands;
    protected CommandsManagerRegistration cmdRegister;

    private static LobbyPlugin instance;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();

        modifyLobbyManager = new ModifyLobbyManager();
        serverBrowserManager = new ServerBrowserManager();

        Bukkit.getPluginManager().registerEvents(new ModifyLobbyListener(), this);
        Bukkit.getPluginManager().registerEvents(new TabHeaderFooterListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerFallOutOfMapListener(), this);

        setupCommands();
        System.out.println("[Lobby] Lobby is now enabled!");
    }

    @Override
    public void onDisable() {
        System.out.println("[Lobby] Lobby is now disabled!");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        try {
            this.commands.execute(cmd.getName(), args, sender, sender);
        } catch (CommandPermissionsException e) {
            sender.sendMessage(ChatColor.RED + "You do not have permission to run this command!");
        } catch (MissingNestedCommandException e) {
            sender.sendMessage(ChatColor.RED + e.getUsage().replace("{cmd}", cmd.getName()));
        } catch (PlayerNotFoundException e) {
            sender.sendMessage(ChatColor.RED + "No players matched query.");
        } catch (CommandUsageException e) {
            sender.sendMessage(ChatColor.RED + e.getMessage());
            sender.sendMessage(ChatColor.RED + e.getUsage());
        } catch (WrappedCommandException e) {
            if (e.getCause() instanceof NumberFormatException) {
                sender.sendMessage(ChatColor.RED + "Number expected. String received instead");
            } else {
                sender.sendMessage(ChatColor.RED + "An unknown error has occured. Please check the console.");
                e.printStackTrace();
            }
        } catch (CommandException e) {
            sender.sendMessage(ChatColor.RED + e.getMessage());
        }
        return true;
    }

    @Override
    public ChunkGenerator getDefaultWorldGenerator(String worldName, String id) {
        return new NullChunkGenerator();
    }

    private void setupCommands() {
        this.commands = new CommandsManager<CommandSender>() {

            @Override
            public boolean hasPermission(CommandSender sender, String perm) {
                return sender instanceof ConsoleCommandSender || sender.hasPermission(perm);
            }

        };
        cmdRegister = new CommandsManagerRegistration(this, this.commands);
        cmdRegister.register(LobbyCommands.class);
    }

    public ServerBrowserManager getServerBrowserManager() {
        return serverBrowserManager;
    }

    public ModifyLobbyManager getModifyLobbyManager() {
        return modifyLobbyManager;
    }

    public static LobbyPlugin getInstance() {
        return instance;
    }

}
