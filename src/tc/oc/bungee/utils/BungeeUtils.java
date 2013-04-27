package tc.oc.bungee.utils;

import net.md_5.bungee.api.plugin.Plugin;

public class BungeeUtils extends Plugin {
    private static BungeeUtils bungeeutils;

    public static BungeeUtils get() {
        return bungeeutils;
    }

    public BungeeUtils() {
        bungeeutils = this;
    }

    @Override
    public void onEnable() {
    }
}
