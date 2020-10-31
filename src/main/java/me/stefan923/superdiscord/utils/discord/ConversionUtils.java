package me.stefan923.superdiscord.utils.discord;

import me.stefan923.superdiscord.language.Language;
import org.bukkit.configuration.file.FileConfiguration;

import java.text.DecimalFormat;

public interface ConversionUtils {

    default long convertStringToLong(String string) {
        try {
            if (string.endsWith("y")) {
                return Integer.parseInt(string.replace("y", "")) * 31104000000L;
            }
            if (string.endsWith("mo")) {
                return Integer.parseInt(string.replace("mo", "")) * 2592000000L;
            }
            if (string.endsWith("w")) {
                return Integer.parseInt(string.replace("w", "")) * 604800000L;
            }
            if (string.endsWith("d")) {
                return Integer.parseInt(string.replace("d", "")) * 86400000L;
            }
            if (string.endsWith("h")) {
                return Integer.parseInt(string.replace("h", "")) * 3600000L;
            }
            if (string.endsWith("m")) {
                return Integer.parseInt(string.replace("m", "")) * 60000L;
            }
            if (string.endsWith("s")) {
                return Integer.parseInt(string.replace("s", "")) * 1000L;
            }
        } catch (NumberFormatException exception) {
            return -1;
        }

        return -1;
    }

    default String convertLongToString(final long time) {
        StringBuilder stringBuilder = new StringBuilder();

        int years = (int) (time / 1000 / 60 / 60 / 24 / 30 / 12);
        if (years != 0)
            stringBuilder.append(years).append(" ").append(years == 1 ? Language.GENERIC_YEAR : Language.GENERIC_YEARS);
        int months = (int) ((time / 1000 / 60 / 60 / 24 / 30) % 12);
        if (months != 0)
            stringBuilder.append(stringBuilder.length() != 0 ? " " : "").append(months).append(" ").append(months == 1 ? Language.GENERIC_MONTH : Language.GENERIC_MONTHS);
        int days = (int) ((time / 1000 / 60 / 60 / 24) % 30);
        if (days != 0)
            stringBuilder.append(stringBuilder.length() != 0 ? " " : "").append(days).append(" ").append(days == 1 ? Language.GENERIC_DAY : Language.GENERIC_DAYS);
        int hours = (int) ((time / 1000 / 60 / 60) % 24);
        if (hours != 0)
            stringBuilder.append(stringBuilder.length() != 0 ? " " : "").append(hours).append(" ").append(hours == 1 ? Language.GENERIC_HOUR : Language.GENERIC_HOURS);
        int minutes = (int) ((time / 1000 / 60) % 60);
        if (minutes != 0)
            stringBuilder.append(stringBuilder.length() != 0 ? " " : "").append(minutes).append(" ").append(minutes == 1 ? Language.GENERIC_MINUTE : Language.GENERIC_MINUTES);
        int seconds = (int) ((time / 1000) % 60);
        if (seconds != 0)
            stringBuilder.append(stringBuilder.length() != 0 ? " " : "").append(seconds).append(" ").append(seconds == 1 ? Language.GENERIC_SECOND : Language.GENERIC_SECONDS);

        return stringBuilder.toString();
    }

    default String formatDouble(double number) {
        DecimalFormat twoDPlaces = new DecimalFormat("#,###.##");

        return twoDPlaces.format(number);
    }

}
