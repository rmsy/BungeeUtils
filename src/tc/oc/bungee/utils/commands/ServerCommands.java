package tc.oc.bungee.utils.commands;

import java.net.InetSocketAddress;
import java.util.Collection;

import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import com.sk89q.bungee.util.BungeeWrappedCommandSender;
import com.sk89q.minecraft.util.commands.*;
import com.sk89q.minecraft.util.pagination.SimplePaginatedResult;

public class ServerCommands {
    public ServerCommands() {
    }

    @Command(
        aliases = {"hub", "lobby", "main"},
        desc= "Teleport to the lobby",
        min = 0,
        max = 0
    )
    public static void hub(final CommandContext args, CommandSender sender) throws CommandException {
        if(sender instanceof ProxiedPlayer) {
            ((ProxiedPlayer) sender).connect(ProxyServer.getInstance().getServers().get("default"));
            sender.sendMessage(ChatColor.GREEN + "Teleporting you to the lobby...");
        } else {
            sender.sendMessage(ChatColor.RED + "Only players may use this command");
        }
    }

    @Command(
        aliases = {"serverlist"},
        desc = "List all BungeeCord servers",
        usage = "[page]",
        min = 0,
        max = 1
    )
    @CommandPermissions("bungeeutils.serverlist")
    public static void serverlist(final CommandContext args, CommandSender sender) throws CommandException {
        final Collection<ServerInfo> servers = BungeeCord.getInstance().getServers().values();

        new SimplePaginatedResult<ServerInfo>("BungeeCord Servers") {
            @Override public String format(ServerInfo server, int index) {
                return (index + 1) + ". " + ChatColor.RED + server.getName() + ChatColor.GREEN + server.getAddress().getHostString() + ":" + server.getAddress().getPort();
            }
        }.display(new BungeeWrappedCommandSender(sender), servers, args.getInteger(0, 1) /* page */);
    }

    @Command(
        aliases = {"addserver"},
        desc = "Add a BungeeCord server",
        usage = "<name> <address> [port]",
        flags = "r",
        min = 2,
        max = 4
    )
    @CommandPermissions("bungeeutils.addserver")
    public static void addserver(final CommandContext args, CommandSender sender) throws CommandException {
        String name = args.getString(0);
        String address = args.getString(1);
        int port = args.argsLength() > 2 ? args.getInteger(2) : 25565;
        boolean restricted = args.hasFlag('r');

        ServerInfo serverInfo = ProxyServer.getInstance().constructServerInfo(name, new InetSocketAddress(address, port), "", restricted);
        ProxyServer.getInstance().getServers().put(name, serverInfo);

        sender.sendMessage(ChatColor.GREEN + "Added server " + ChatColor.GOLD + name);
    }

    @Command(
        aliases = {"delserver"},
        desc = "Remove a BungeeCord server",
        usage = "<name>",
        min = 1,
        max = 1
    )
    @CommandPermissions("bungeeutils.delserver")
    public static void delserver(final CommandContext args, CommandSender sender) throws CommandException {
        String name = args.getString(0);

        if (ProxyServer.getInstance().getServers().remove(name) == null) {
            sender.sendMessage(ChatColor.RED + "Could not find server " + ChatColor.GOLD + name);
        } else {
            sender.sendMessage(ChatColor.GREEN + "Removed server " + ChatColor.GOLD + name);
        }
    }
}
