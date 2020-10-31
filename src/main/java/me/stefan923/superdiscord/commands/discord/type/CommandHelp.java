package me.stefan923.superdiscord.commands.discord.type;

import me.stefan923.superdiscord.SuperDiscord;
import me.stefan923.superdiscord.commands.discord.AbstractCommand;
import me.stefan923.superdiscord.settings.Setting;
import me.stefan923.superdiscord.utils.discord.ConversionUtils;
import me.stefan923.superdiscord.utils.discord.DiscordMessageUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.*;

public class CommandHelp extends AbstractCommand implements DiscordMessageUtils, ConversionUtils {
    public CommandHelp() {
        super(null, false, "help");
    }

    @Override
    protected ReturnType runCommand(SuperDiscord instance, MessageReceivedEvent event, User sender, String... args) {
        help(event, sender);
        return ReturnType.SUCCESS;
    }

    private void help(MessageReceivedEvent event, User user) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setAuthor("SuperDiscord help - commands", null, "https://i.imgur.com/RGxsbVa.png");
        embedBuilder.setTitle("List of SuperDiscord main commands:");
        embedBuilder.setDescription("**" + Setting.COMMAND_PREFIX + "server** - Shows statistics about the server.\n" +
                "**" + Setting.COMMAND_PREFIX + "config** - Configures the main software settings.\n" +
                "**" + Setting.COMMAND_PREFIX + "litabans** - Shows punishments details and more.\n" +
                "**" + Setting.COMMAND_PREFIX + "helpop** - Sends a response to all online players.\n");
        embedBuilder.setColor(Color.decode("0x0092e2"));
        embedBuilder.setFooter("Requested by " + user.getName() + " | Answered in " + (System.currentTimeMillis() - event.getMessage().getTimeCreated().toInstant().toEpochMilli()) + " ms", user.getAvatarUrl());
        sendEmbededMessage(event.getTextChannel(), embedBuilder);
    }

    @Override
    public String getPermissionNode() {
        return null;
    }

    @Override
    public String getSyntax() {
        return Setting.COMMAND_PREFIX + "server <stats>";
    }

    @Override
    public String getDescription() {
        return null;
    }
}
