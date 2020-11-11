package me.stefan923.superdiscord.commands.discord.type.config;

import me.stefan923.superdiscord.SuperDiscord;
import me.stefan923.superdiscord.commands.discord.AbstractCommand;
import me.stefan923.superdiscord.language.Language;
import me.stefan923.superdiscord.settings.Setting;
import me.stefan923.superdiscord.utils.discord.ConversionUtils;
import me.stefan923.superdiscord.utils.discord.DiscordMessageUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.*;

public class CommandConfigSetCommand extends AbstractCommand implements DiscordMessageUtils, ConversionUtils {
    public CommandConfigSetCommand(AbstractCommand abstractCommand) {
        super(abstractCommand, true, "setcommand");
    }

    @Override
    protected ReturnType runCommand(SuperDiscord instance, MessageReceivedEvent event, User sender, String... args) {
        if (args.length == 3) {
            String command = args[1].toLowerCase();
            boolean enable = false;

            if (args[2].equalsIgnoreCase("true")) {
                enable = true;
            } else if (!args[2].equalsIgnoreCase("false")) {
                return ReturnType.SYNTAX_ERROR;
            }

            switch (command) {
                case "ftop":
                    Setting.ENABLED_COMMANDS_FTOP = enable;
                    break;
                case "helpop":
                    Setting.ENABLED_COMMANDS_HELPOP = enable;
                    break;
                case "litebans":
                    Setting.ENABLED_COMMANDS_LITEBANS = enable;
                    break;
                case "server":
                    Setting.ENABLED_COMMANDS_SERVER = enable;
                    break;
                default:
                    return ReturnType.SYNTAX_ERROR;
            }

            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setAuthor("SuperDiscord configuration - toggle command", null, "https://i.imgur.com/Hm6QvYG.png");
            embedBuilder.setDescription(Language.TOGGLE_COMMAND
                    .replace("%action%", enable ? "enabled" : "disabled")
                    .replace("%command%", command));
            embedBuilder.setColor(Color.decode("0x0092e2"));
            embedBuilder.setFooter("Requested by " + sender.getName() + " | Answered in " + (System.currentTimeMillis() - event.getMessage().getTimeCreated().toInstant().toEpochMilli()) + " ms", sender.getAvatarUrl());
            sendEmbededMessage(event.getTextChannel(), embedBuilder);

            instance.getSettingsManager().save();
            instance.reloadDiscordCommandManager();
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
        return "**" + Setting.COMMAND_PREFIX + "config setcommand <command> <true|false>**\n\n`commands` represents the command you want to enable/disable.\nOptions: `ftop`, `helpop`, `litebans`, `server`\n";
    }

    @Override
    public String getDescription() {
        return null;
    }
}
