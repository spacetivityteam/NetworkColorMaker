package net.spacetivity.colormaker.api;

import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.md_5.bungee.api.ChatColor;
import net.spacetivity.colormaker.api.color.NetworkColor;

public class ColorTranslator {

    private static String translateChatColor(String text) {
        String[] texts = text.split(String.format("((?<=%1$s)|(?=%1$s))", "&"));
        StringBuilder finalText = new StringBuilder();

        for (int i = 0; i < texts.length; i++) {
            if (texts[i].equalsIgnoreCase("&")) {
                i++;
                if (texts[i].charAt(0) == '#') {
                    finalText.append(ChatColor.of(texts[i].substring(0, 7))).append(texts[i].substring(7));
                } else {
                    finalText.append(ChatColor.translateAlternateColorCodes('&', "&" + texts[i]));
                }
            } else {
                finalText.append(texts[i]);
            }
        }

        return finalText.toString();
    }

    public static class Spigot {

        public static String toChatColor(NetworkColor color) {
            if (color.isUseHexadecimal())
                return translateChatColor("&" + color.getColorCode());

            return ChatColor.of(color.getColorCode()).toString();
        }

        public static TextColor toTextColor(NetworkColor color) {
            if (color.isUseHexadecimal())
                return TextColor.fromHexString(color.getColorCode());

            return NamedTextColor.NAMES.value(color.getColorCode().toLowerCase());
        }
    }

    public static class Velocity {

        public static TextColor toTextColor(NetworkColor color) {
            if (color.isUseHexadecimal())
                return TextColor.fromHexString(color.getColorCode());

            return NamedTextColor.NAMES.value(color.getColorCode().toLowerCase());
        }

    }

    public static class Bungee {
        public static String toChatColor(NetworkColor color) {
            return translateChatColor("&" + color.getColorCode());
        }
    }
}
