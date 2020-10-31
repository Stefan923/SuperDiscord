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

public class CommandConfigSetChannel extends AbstractCommand implements DiscordMessageUtils, ConversionUtils {
    public CommandConfigSetChannel(AbstractCommand abstractCommand) {
        super(abstractCommand, true, "setchannel");
    }

    @Override
    protected ReturnType runCommand(SuperDiscord instance, MessageReceivedEvent event, User sender, String... args) {
        if (args.length == 2 && args[1].equalsIgnoreCase("helpop")) {
            TextChannel textChannel = event.getTextChannel();
            Setting.CHANNEL_IDS_HELPOP = textChannel.getIdLong();
            instance.getSettingsManager().save();

            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setAuthor("SuperDiscord configuration - helpop channel", null, "https://i.imgur.com/Hm6QvYG.png");
            embedBuilder.setDescription("\nYou have successfully set the channel id to which helpop messages will be sent.\n");
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
        return "**" + Setting.COMMAND_PREFIX + "config setchannel <helpop>**\n\n`helpop` represents the text channel on which the messages sent through helpop will be received.\n";
    }

    @Override
    public String getDescription() {
        return null;
    }
}
