package tc.oc.bungee.utils.commands;

import java.net.InetSocketAddress;
import java.util.Collection;

import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;

import com.sk89q.bungee.util.BungeeWrappedCommandSender;
import com.sk89q.minecraft.util.commands.ChatColor;
import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandException;
import com.sk89q.minecraft.util.pagination.SimplePaginatedResult;

public class ServerCommands {
    public ServerCommands() {
    }

    @Command(
        aliases = {"serverlist"},
        desc = "List all BungeeCord servers",
        usage = "[page]",
        min = 0,
        max = 1
    )
    public static void serverlist(final CommandContext args, CommandSender sender) throws CommandException {
        final Collection<ServerInfo> servers = BungeeCord.getInstance().getServers().values();

        new SimplePaginatedResult<ServerInfo>("BungeeCord Servers") {
            @Override public String format(ServerInfo server, int index) {
                return (index + 1) + ". " + server.getName();
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
    public static void addserver(final CommandContext args, CommandSender sender) throws CommandException {
        String name = args.getString(0);
        String address = args.getString(1);
        int port = args.argsLength() > 2 ? args.getInteger(2) : 25565;
        boolean restricted = args.hasFlag('r');

        ServerInfo serverInfo = ProxyServer.getInstance().constructServerInfo(name, new InetSocketAddress(address, port), restricted);
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
    public static void delserver(final CommandContext args, CommandSender sender) throws CommandException {
        String name = args.getString(0);

        ProxyServer.getInstance().getServers().remove(name);

        sender.sendMessage(ChatColor.GREEN + "Removed server " + ChatColor.GOLD + name);
    }
}
