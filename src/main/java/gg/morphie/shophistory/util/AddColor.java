package gg.morphie.shophistory.util;

import net.md_5.bungee.api.ChatColor;

public class AddColor {

    public static String addColor(String message) {
        if (message == null) {
            return null;
        }
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public String fixCase(String string) {

        StringBuilder builder = new StringBuilder();
        for(String string2 : string.split("_"))
            builder.append(string2.substring(0,1).toUpperCase() + string2.substring(1).toLowerCase() + " ");
        String name = builder.toString();
        return removeLastChar(name);
    }

    public String removeLastChar(String s) {
        if (s == null || s.length() == 0) {
            return s;
        }
        return s.substring(0, s.length()-1);
    }
}
