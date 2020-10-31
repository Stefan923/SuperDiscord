package me.stefan923.superdiscord.commands.discord.type;

import me.stefan923.superdiscord.SuperDiscord;
import me.stefan923.superdiscord.commands.discord.AbstractCommand;
import me.stefan923.superdiscord.settings.Setting;
import me.stefan923.superdiscord.settings.SettingsManager;
import me.stefan923.superdiscord.utils.discord.ConversionUtils;
import me.stefan923.superdiscord.utils.discord.DiscordMessageUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.*;

public class CommandConfigSetPrefix extends AbstractCommand implements DiscordMessageUtils, ConversionUtils {
    public CommandConfigSetPrefix(AbstractCommand abstractCommand) {
        super(abstractCommand, true, "setprefix");
    }

    @Override
    protected ReturnType runCommand(SuperDiscord instance, MessageReceivedEvent event, User sender, String... args) {
        if (args.length > 1) {
            TextChannel textChannel = event.getTextChannel();
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 1; i < args.length; i++) {
                stringBuilder.append(args[i]).append((i < args.length - 1 ? " " : ""));
            }

            Setting.COMMAND_PREFIX = stringBuilder.toString();
            instance.getSettingsManager().save();

            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setAuthor("SuperDiscord configuration - command prefix", null, "https://i.imgur.com/Hm6QvYG.png");
            embedBuilder.setDescription("\nYou have successfully set the new command prefix for SuperDiscord to `" + Setting.COMMAND_PREFIX + "`.\n");
            embedBuilder.setColor(Color.decode("0x0092e2"));
            embedBuilder.setFooter("Requested by " + sender.getName() + " | Answered in " + (System.currentTimeMillis() - event.getMessage().getTimeCreated().toInstant().toEpochMilli()) + " ms", sender.getAvatarUrl());
            sendEmbededMessage(textChannel, embedBuilder);

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
        return "**" + Setting.COMMAND_PREFIX + "config setprefix <prefix>** - Configures the command prefix for SuperDiscord.\n";
    }

    @Override
    public String getDescription() {
        return null;
    }
}
