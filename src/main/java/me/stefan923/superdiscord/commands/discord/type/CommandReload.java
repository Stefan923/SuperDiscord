package me.stefan923.superdiscord.commands.discord.type;

import me.stefan923.superdiscord.SuperDiscord;
import me.stefan923.superdiscord.commands.discord.AbstractCommand;
import me.stefan923.superdiscord.language.LanguageManager;
import me.stefan923.superdiscord.settings.Setting;
import me.stefan923.superdiscord.settings.SettingsManager;
import me.stefan923.superdiscord.utils.discord.DiscordMessageUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.*;

public class CommandReload extends AbstractCommand implements DiscordMessageUtils {

    private final SettingsManager settingsManager;
    private final LanguageManager languageManager;

    public CommandReload() {
        super(null, true, "reload");
        settingsManager = SettingsManager.getInstance();
        languageManager = LanguageManager.getInstance();
    }

    @Override
    protected AbstractCommand.ReturnType runCommand(SuperDiscord instance, MessageReceivedEvent event, User sender, String... args) {
        if (args.length == 1) {
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setAuthor("SuperDiscord reload - " + args[0].toLowerCase(), null, "https://i.imgur.com/Hm6QvYG.png");

            if (args[0].equalsIgnoreCase("all")) {
                settingsManager.setup(instance);
                languageManager.setup(instance);
                embedBuilder.setDescription("You have successfully reloaded **all** plugin's modules!");
            } else if (args[0].equalsIgnoreCase("settings")) {
                settingsManager.setup(instance);
                embedBuilder.setDescription("You have successfully reloaded the **settings** module!");
            } else if (args[0].equalsIgnoreCase("language")) {
                languageManager.setup(instance);
                embedBuilder.setDescription("You have successfully reloaded the **language** module!");
            } else {
                return ReturnType.SYNTAX_ERROR;
            }

            embedBuilder.setColor(Color.decode("0x0092e2"));
            embedBuilder.setFooter("Requested by " + sender.getName() + " | Answered in " + (System.currentTimeMillis() - event.getMessage().getTimeCreated().toInstant().toEpochMilli()) + " ms", sender.getAvatarUrl());
            sendEmbededMessage(event.getTextChannel(), embedBuilder);

            return ReturnType.SUCCESS;
        }

        return ReturnType.SYNTAX_ERROR;
    }

    @Override
    public String getPermissionNode() {
        return null;
    }

    @Override
    public String getSyntax() {
        return "**" + Setting.COMMAND_PREFIX + "reload <all|settings|language>**";
    }

    @Override
    public String getDescription() {
        return null;
    }
}
