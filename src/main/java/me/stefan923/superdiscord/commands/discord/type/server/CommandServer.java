package me.stefan923.superdiscord.commands.discord.type.server;

import me.stefan923.superdiscord.SuperDiscord;
import me.stefan923.superdiscord.commands.discord.AbstractCommand;
import me.stefan923.superdiscord.settings.Setting;
import me.stefan923.superdiscord.utils.discord.ConversionUtils;
import me.stefan923.superdiscord.utils.discord.DiscordMessageUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.*;

public class CommandServer extends AbstractCommand implements DiscordMessageUtils, ConversionUtils {
    public CommandServer() {
        super(null, false, "server");
    }

    @Override
    protected ReturnType runCommand(SuperDiscord instance, MessageReceivedEvent event, User sender, String... args) {
        help(event, sender);
        return ReturnType.SUCCESS;
    }

    private void help(MessageReceivedEvent event, User user) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setAuthor("SuperDiscord help - commands", null, "https://i.imgur.com/RGxsbVa.png");
        embedBuilder.setTitle("List of SuperDiscord subcommands for `%server`:");
        embedBuilder.setDescription("**" + Setting.COMMAND_PREFIX + "server stats version [duration]** - Shows statistics about the game versions that players use.\n" +
                "**" + Setting.COMMAND_PREFIX + "server lag** - Shows server uptime, tps, free memory and more.\n" +
                "**" + Setting.COMMAND_PREFIX + "server glist** - Shows details about servers status and player count.\n");
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
        return Setting.COMMAND_PREFIX + "server";
    }

    @Override
    public String getDescription() {
        return null;
    }
}
