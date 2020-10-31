package me.stefan923.superdiscord.settings;

import me.stefan923.superdiscord.SuperDiscord;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class SettingsManager {

    private static final SettingsManager instance = new SettingsManager();
    private FileConfiguration config;
    private File cfile;

    public static SettingsManager getInstance() {
        return instance;
    }

    public void setup(SuperDiscord superDiscord) {
        cfile = new File(superDiscord.getDataFolder(), "settings.yml");
        config = YamlConfiguration.loadConfiguration(cfile);

        config.options().header("SuperDiscord by Stefan923\n");
        config.addDefault("Discord Bot.Token ID", Setting.TOKEN_ID);
        config.addDefault("Discord Bot.Command Prefix", Setting.COMMAND_PREFIX);
        config.addDefault("Discord Bot.Channels IDs.HelpOp", Setting.CHANNEL_IDS_HELPOP);
        config.addDefault("Discord Bot.Enabled Commands.FTop", Setting.ENABLED_COMMANDS_FTOP);
        config.addDefault("Discord Bot.Enabled Commands.HelpOp", Setting.ENABLED_COMMANDS_HELPOP);
        config.addDefault("Discord Bot.Enabled Commands.Litebans", Setting.ENABLED_COMMANDS_LITEBANS);
        config.addDefault("Discord Bot.Enabled Commands.Server", Setting.ENABLED_COMMANDS_SERVER);
        config.options().copyDefaults(true);

        saveDefault();
        load();
    }

    public FileConfiguration getConfig() {
        return config;
    }

    public void load() {
        Setting.TOKEN_ID = config.getString("Discord Bot.Token ID");
        Setting.COMMAND_PREFIX = config.getString("Discord Bot.Command Prefix");
        Setting.CHANNEL_IDS_HELPOP = config.getLong("Discord Bot.Channels IDs.HelpOp");
        Setting.ENABLED_COMMANDS_FTOP = config.getBoolean("Discord Bot.Enabled Commands.FTop");
        Setting.ENABLED_COMMANDS_HELPOP = config.getBoolean("Discord Bot.Enabled Commands.HelpOp");
        Setting.ENABLED_COMMANDS_LITEBANS = config.getBoolean("Discord Bot.Enabled Commands.Litebans");
        Setting.ENABLED_COMMANDS_SERVER = config.getBoolean("Discord Bot.Enabled Commands.Server");
    }

    public void save() {
        config.set("Discord Bot.Token ID", Setting.TOKEN_ID);
        config.set("Discord Bot.Command Prefix", Setting.COMMAND_PREFIX);
        config.set("Discord Bot.Channels IDs.HelpOp", Setting.CHANNEL_IDS_HELPOP);
        config.set("Discord Bot.Enabled Commands.FTop", Setting.ENABLED_COMMANDS_FTOP);
        config.set("Discord Bot.Enabled Commands.HelpOp", Setting.ENABLED_COMMANDS_HELPOP);
        config.set("Discord Bot.Enabled Commands.Litebans", Setting.ENABLED_COMMANDS_LITEBANS);
        config.set("Discord Bot.Enabled Commands.Server", Setting.ENABLED_COMMANDS_SERVER);
        try {
            config.save(cfile);
        } catch (IOException e) {
            Bukkit.getLogger().severe(ChatColor.RED + "File 'settings.yml' couldn't be saved!");
        }
    }

    public void saveDefault() {
        try {
            config.save(cfile);
        } catch (IOException e) {
            Bukkit.getLogger().severe(ChatColor.RED + "File 'settings.yml' couldn't be saved!");
        }
    }

    public void reload() {
        config = YamlConfiguration.loadConfiguration(cfile);
    }

}
