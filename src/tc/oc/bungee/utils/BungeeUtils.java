package tc.oc.bungee.utils;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;
import tc.oc.bungee.utils.commands.ServerCommands;

import com.sk89q.bungee.util.BungeeCommandsManager;
import com.sk89q.bungee.util.CommandExecutor;
import com.sk89q.bungee.util.CommandRegistration;
import com.sk89q.minecraft.util.commands.*;

public class BungeeUtils extends Plugin implements CommandExecutor<CommandSender> {
    private static BungeeUtils bungeeutils;
    private BungeeCommandsManager commands;

    public static BungeeUtils get() {
        return bungeeutils;
    }

    public BungeeUtils() {
        bungeeutils = this;
    }

    @Override
    public void onEnable() {
        super.onEnable();
        this.registerCommands();
    }

    private void registerCommands() {
        PluginManager pluginManager = this.getProxy().getPluginManager();

        this.commands = new BungeeCommandsManager();

        CommandRegistration registrar = new CommandRegistration(this, pluginManager, this.commands, this);
        registrar.register(ServerCommands.class);
    }

    @Override
    public void onCommand(CommandSender sender, String commandName, String[] args) {
        try {
            this.commands.execute(commandName, args, sender, sender);
        } catch (CommandPermissionsException e) {
            sender.sendMessage(ChatColor.RED + "You don't have permission.");
        } catch (MissingNestedCommandException e) {
            sender.sendMessage(ChatColor.RED + e.getUsage());
        } catch (CommandUsageException e) {
            sender.sendMessage(ChatColor.RED + e.getMessage());
            sender.sendMessage(ChatColor.RED + e.getUsage());
        } catch (WrappedCommandException e) {
            if (e.getCause() instanceof NumberFormatException) {
                sender.sendMessage(ChatColor.RED + "Number expected, string received instead.");
            } else {
                sender.sendMessage(ChatColor.RED + "An error has occurred. See console.");
                e.printStackTrace();
            }
        } catch (CommandException e) {
            sender.sendMessage(ChatColor.RED + e.getMessage());
        }
    }
}
