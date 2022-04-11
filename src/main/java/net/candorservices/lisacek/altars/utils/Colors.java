package net.candorservices.lisacek.altars.utils;

import net.md_5.bungee.api.ChatColor;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Colors {

    private Colors() {
    }

    // Patterms
    public static final Pattern HEX_PATTERN = Pattern.compile("&#" + "([A-Fa-f0-9]{6})" + "");
    public static final char COLOR_CHAR = ChatColor.COLOR_CHAR;

    // Translator
    private static String translateHexColorCodes(String message) {
        Matcher matcher = HEX_PATTERN.matcher(message);
        StringBuffer buffer = new StringBuffer(message.length() + 4 * 8);
        while (matcher.find()) {
            String group = matcher.group(1);

            matcher.appendReplacement(buffer, COLOR_CHAR + "x"
                    + COLOR_CHAR + group.charAt(0) + COLOR_CHAR + group.charAt(1)
                    + COLOR_CHAR + group.charAt(2) + COLOR_CHAR + group.charAt(3)
                    + COLOR_CHAR + group.charAt(4) + COLOR_CHAR + group.charAt(5)
            );

        }
        return matcher.appendTail(buffer).toString();
    }

    public static String translateColors(String message) {
        return ChatColor.translateAlternateColorCodes('&', translateHexColorCodes(message));
    }

    public static List<String> translateColors(List<String> messages) {
        List<String> translated = new ArrayList<>();
        for (String message : messages) {
            translated.add(translateColors(message));
        }
        return translated;
    }

}