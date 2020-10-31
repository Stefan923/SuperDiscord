package me.stefan923.superdiscord;

import litebans.api.Database;
import me.stefan923.superdiscord.commands.discord.CommandManager;
import me.stefan923.superdiscord.listeners.bukkit.PlayerCommandPreprocessListener;
import me.stefan923.superdiscord.listeners.discord.MessageReceivedListener;
import me.stefan923.superdiscord.settings.Setting;
import me.stefan923.superdiscord.settings.SettingsManager;
import me.stefan923.ultimateafk.UltimateAfk;
import net.dv8tion.jda.api.AccountType;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class SuperDiscord extends JavaPlugin {

    private JDA jda;

    private SettingsManager settingsManager;

    private CommandManager discordCommandManager;
    private Database litebansDatabase;
    private me.stefan923.ultimateafk.storage.Database ultimateAfkDatabase;

    @Override
    public void onEnable() {
        settingsManager = SettingsManager.getInstance();
        settingsManager.setup(this);

        discordCommandManager = new CommandManager(this);
        litebansDatabase = Database.get();
        ultimateAfkDatabase = UltimateAfk.getInstance().getDatabase("ultimateafk_logs");

        loadDiscordBot();
        loadBukkitListeners();
    }

    private void loadDiscordBot() {
        JDABuilder builder = new JDABuilder(AccountType.BOT);

        builder.setToken(Setting.TOKEN_ID);
        builder.setAutoReconnect(true);
        builder.setStatus(OnlineStatus.DO_NOT_DISTURB);
        builder.setActivity(Activity.playing("with Stefan923"));
        builder.addEventListeners(new MessageReceivedListener(this));

        try {
            jda = builder.build();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadBukkitListeners() {
        int i = 1;
        PluginManager pluginManager = Bukkit.getServer().getPluginManager();
        pluginManager.registerEvents(new PlayerCommandPreprocessListener(jda), this);
    }

    public SettingsManager getSettingsManager() {
        return settingsManager;
    }

    public CommandManager getDiscordCommandManager() {
        return discordCommandManager;
    }

    public void reloadDiscordCommandManager() { discordCommandManager.loadCommands();
    }

    public Database getLitebansDatabase() {
        return litebansDatabase;
    }

    public me.stefan923.ultimateafk.storage.Database getUltimateAfkDatabase() {
        return ultimateAfkDatabase;
    }

    @Override
    public void onDisable() {

        super.onDisable();
    }

}
