package me.stefan923.superdiscord.language;

import me.stefan923.superdiscord.SuperDiscord;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class LanguageManager {

    private static final LanguageManager instance = new LanguageManager();
    private FileConfiguration config;
    private File cfile;

    public static LanguageManager getInstance() {
        return instance;
    }

    public void setup(SuperDiscord superDiscord) {
        cfile = new File(superDiscord.getDataFolder(), "language.yml");
        config = YamlConfiguration.loadConfiguration(cfile);

        config.options().header("SuperDiscord by Stefan923\n");
        config.addDefault("General.Invalid Command Syntax", Language.INVALID_COMMAND_SYNTAX);
        config.addDefault("General.No Permission", Language.NO_PERMISSION);
        config.addDefault("General.No Such Command", Language.NO_SUCH_COMMAND);
        config.addDefault("Generic.Year", Language.GENERIC_YEAR);
        config.addDefault("Generic.Year", Language.GENERIC_YEARS);
        config.addDefault("Generic.Month", Language.GENERIC_MONTH);
        config.addDefault("Generic.Months", Language.GENERIC_MONTHS);
        config.addDefault("Generic.Day", Language.GENERIC_DAY);
        config.addDefault("Generic.Days", Language.GENERIC_DAYS);
        config.addDefault("Generic.Hour", Language.GENERIC_HOUR);
        config.addDefault("Generic.Hours", Language.GENERIC_HOURS);
        config.addDefault("Generic.Minute", Language.GENERIC_MINUTE);
        config.addDefault("Generic.Minutes", Language.GENERIC_MINUTES);
        config.addDefault("Generic.Second", Language.GENERIC_SECOND);
        config.addDefault("Generic.Seconds", Language.GENERIC_SECONDS);
        config.addDefault("Hooks.Factions.Top", Language.FACTIONS_TOP);
        config.addDefault("Hooks.Factions.Info", Language.FACTIONS_TOP_INFO);
        config.options().copyDefaults(true);

        saveDefault();
        load();
    }

    public FileConfiguration getConfig() {
        return config;
    }

    public void load() {
        Language.INVALID_COMMAND_SYNTAX = config.getString("General.Invalid Command Syntax");
        Language.NO_PERMISSION = config.getString("General.No Permission");
        Language.NO_SUCH_COMMAND = config.getString("General.No Such Command");
        Language.GENERIC_YEAR = config.getString("Generic.Year");
        Language.GENERIC_YEARS = config.getString("Generic.Year");
        Language.GENERIC_MONTH = config.getString("Generic.Month");
        Language.GENERIC_MONTHS = config.getString("Generic.Months");
        Language.GENERIC_DAY = config.getString("Generic.Day");
        Language.GENERIC_DAYS = config.getString("Generic.Days");
        Language.GENERIC_HOUR = config.getString("Generic.Hour");
        Language.GENERIC_HOURS = config.getString("Generic.Hours");
        Language.GENERIC_MINUTE = config.getString("Generic.Minute");
        Language.GENERIC_MINUTES = config.getString("Generic.Minutes");
        Language.GENERIC_SECOND = config.getString("Generic.Second");
        Language.GENERIC_SECONDS = config.getString("Generic.Seconds");
        Language.FACTIONS_TOP = config.getString("Hooks.Factions.Top Item");
        Language.FACTIONS_TOP_INFO = config.getString("Hooks.Factions.Info");
    }

    public void save() {
        config.set("General.Invalid Command Syntax", Language.INVALID_COMMAND_SYNTAX);
        config.set("General.No Permission", Language.NO_PERMISSION);
        config.set("General.No Such Command", Language.NO_SUCH_COMMAND);
        config.set("Generic.Year", Language.GENERIC_YEAR);
        config.set("Generic.Year", Language.GENERIC_YEARS);
        config.set("Generic.Month", Language.GENERIC_MONTH);
        config.set("Generic.Months", Language.GENERIC_MONTHS);
        config.set("Generic.Day", Language.GENERIC_DAY);
        config.set("Generic.Days", Language.GENERIC_DAYS);
        config.set("Generic.Hour", Language.GENERIC_HOUR);
        config.set("Generic.Hours", Language.GENERIC_HOURS);
        config.set("Generic.Minute", Language.GENERIC_MINUTE);
        config.set("Generic.Minutes", Language.GENERIC_MINUTES);
        config.set("Generic.Second", Language.GENERIC_SECOND);
        config.set("Generic.Seconds", Language.GENERIC_SECONDS);
        config.set("Hooks.Factions.Top", Language.FACTIONS_TOP);
        config.set("Hooks.Factions.Info", Language.FACTIONS_TOP_INFO);
        try {
            config.save(cfile);
        } catch (IOException e) {
            Bukkit.getLogger().severe(ChatColor.RED + "File 'language.yml' couldn't be saved!");
        }
    }

    public void save(String content) throws InvalidConfigurationException {
        try {
            config.loadFromString(content);
            config.save(cfile);
        } catch (IOException e) {
            Bukkit.getLogger().severe(ChatColor.RED + "File 'language.yml' couldn't be saved!");
        }
    }

    public void saveDefault() {
        try {
            config.save(cfile);
        } catch (IOException e) {
            Bukkit.getLogger().severe(ChatColor.RED + "File 'language.yml' couldn't be saved!");
        }
    }

    public void reload() {
        config = YamlConfiguration.loadConfiguration(cfile);
    }

}
