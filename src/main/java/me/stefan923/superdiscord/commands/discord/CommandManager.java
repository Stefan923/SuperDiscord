package me.stefan923.superdiscord.commands.discord;

import me.stefan923.superdiscord.SuperDiscord;
import me.stefan923.superdiscord.commands.discord.type.*;
import me.stefan923.superdiscord.commands.discord.type.config.*;
import me.stefan923.superdiscord.commands.discord.type.factions.*;
import me.stefan923.superdiscord.commands.discord.type.litebans.*;
import me.stefan923.superdiscord.commands.discord.type.server.*;
import me.stefan923.superdiscord.exceptions.MissingPermissionException;
import me.stefan923.superdiscord.language.Language;
import me.stefan923.superdiscord.settings.Setting;
import me.stefan923.superdiscord.utils.discord.DiscordMessageUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommandManager implements DiscordMessageUtils {

    private static final List<AbstractCommand> commands = new ArrayList<>();
    private final SuperDiscord plugin;

    public CommandManager(SuperDiscord plugin) {
        this.plugin = plugin;

        loadCommands();
    }

    public void loadCommands() {
        commands.clear();

        if (Setting.ENABLED_COMMANDS_LITEBANS) {
            AbstractCommand commandLitebans = addCommand(new CommandLitebans());
            addCommand(new CommandLitebansCount(commandLitebans));
            addCommand(new CommandLitebansStaffinfo(commandLitebans));
        }

        if (Setting.ENABLED_COMMANDS_SERVER) {
            AbstractCommand commandServer = addCommand(new CommandServer());
            addCommand(new CommandServerStats(commandServer));
            addCommand(new CommandServerLag(commandServer));
            addCommand(new CommandServerGList(commandServer));
        }

        AbstractCommand commandConfig = addCommand(new CommandConfig());
        addCommand(new CommandConfigGetLang(commandConfig));
        addCommand(new CommandConfigSetChannel(commandConfig));
        addCommand(new CommandConfigSetCommand(commandConfig));
        addCommand(new CommandConfigSetLang(commandConfig));
        addCommand(new CommandConfigSetPrefix(commandConfig));

        addCommand(new CommandHelp());
        addCommand(new CommandReload());

        if (Setting.ENABLED_COMMANDS_FTOP) {
            addCommand(new CommandFactionsTop());
            addCommand(new CommandFactionsTopInfo());
        }

        if (Setting.ENABLED_COMMANDS_HELPOP) {
            addCommand(new CommandHelpOp());
        }
    }

    private AbstractCommand addCommand(AbstractCommand abstractCommand) {
        commands.add(abstractCommand);
        return abstractCommand;
    }

    public void onCommand(MessageReceivedEvent event, String command, String... args) {
        User commandSender = event.getAuthor();

        for (AbstractCommand abstractCommand : commands) {
            if (abstractCommand.getCommand() != null && abstractCommand.getCommand().equalsIgnoreCase(command)) {
                if (args.length == 0 || abstractCommand.hasArgs()) {
                    processRequirements(event, abstractCommand, commandSender, args);
                    return;
                }
            } else if (args.length != 0 && abstractCommand.getParent() != null && abstractCommand.getParent().getCommand().equalsIgnoreCase(command)) {
                String cmd = args[0];
                String cmd2 = args.length >= 2 ? String.join(" ", args[0], args[1]) : null;
                for (String cmds : abstractCommand.getSubCommand()) {
                    if (cmd.equalsIgnoreCase(cmds) || (cmd2 != null && cmd2.equalsIgnoreCase(cmds))) {
                        processRequirements(event, abstractCommand, commandSender, args);
                        return;
                    }
                }
            }
        }

        noSuchCommandError(event, commandSender);
    }

    private void processRequirements(MessageReceivedEvent event, AbstractCommand command, User sender, String... args) {
        String permissionNode = command.getPermissionNode();
        if (permissionNode == null || true) {
            AbstractCommand.ReturnType returnType = null;
            try {
                returnType = command.runCommand(plugin, event, sender, args);
            } catch (MissingPermissionException exception) {
                sendMessage(event.getChannel().getIdLong(), replacePlaceholders(Language.NO_PERMISSION.replace("%permission%", exception.getMessage()), event));
            } catch (SQLException exception) {
                exception.printStackTrace();
                sendMessage(event.getChannel().getIdLong(), replacePlaceholders(Language.NO_PERMISSION.replace("%permission%", exception.getMessage()), event));
            }
            if (returnType == AbstractCommand.ReturnType.SYNTAX_ERROR) {
                syntaxError(event, sender, command.getSyntax());
            }
            return;
        }
        sendMessage(event.getChannel().getIdLong(), replacePlaceholders(Language.NO_PERMISSION.replace("%permission%", permissionNode), event));
    }

    private void syntaxError(MessageReceivedEvent event, User user, String syntax) {
        EmbedBuilder embedBuilder = new EmbedBuilder();

        embedBuilder.setAuthor("Syntax - Error", null, "https://i.imgur.com/EmNLVFm.png");
        embedBuilder.setDescription(Language.INVALID_COMMAND_SYNTAX.replace("%syntax%", syntax));
        embedBuilder.setColor(Color.decode("0x0092e2"));
        embedBuilder.setFooter("Requested by " + user.getName() + " | Answered in " + (System.currentTimeMillis() - event.getMessage().getTimeCreated().toInstant().toEpochMilli()) + " ms", user.getAvatarUrl());

        sendEmbededMessage(event.getTextChannel(), embedBuilder);
    }

    private void noSuchCommandError(MessageReceivedEvent event, User user) {
        EmbedBuilder embedBuilder = new EmbedBuilder();

        embedBuilder.setAuthor("No Such Command - Error", null, "https://i.imgur.com/EmNLVFm.png");
        embedBuilder.setDescription(Language.NO_SUCH_COMMAND);
        embedBuilder.setColor(Color.decode("0x0092e2"));
        embedBuilder.setFooter("Requested by " + user.getName() + " | Answered in " + (System.currentTimeMillis() - event.getMessage().getTimeCreated().toInstant().toEpochMilli()) + " ms", user.getAvatarUrl());

        sendEmbededMessage(event.getTextChannel(), embedBuilder);
    }

    public List<AbstractCommand> getCommands() {
        return Collections.unmodifiableList(commands);
    }

}
