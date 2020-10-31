package me.stefan923.superdiscord.commands.discord;

import me.stefan923.superdiscord.SuperDiscord;
import me.stefan923.superdiscord.exceptions.MissingPermissionException;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class AbstractCommand {

    private AbstractCommand parent;
    private boolean hasArgs;
    private String command;
    private List<String> subCommand = new ArrayList<>();

    protected AbstractCommand(AbstractCommand parent, boolean hasArgs, String... command) {
        if (parent != null) {
            this.subCommand = Arrays.asList(command);
        } else {
            this.command = Arrays.asList(command).get(0);
        }
        this.parent = parent;
        this.hasArgs = hasArgs;
    }

    public AbstractCommand getParent() {
        return parent;
    }

    public String getCommand() {
        return command;
    }

    public List<String> getSubCommand() {
        return subCommand;
    }

    public void addSubCommand(String command) {
        subCommand.add(command);
    }

    protected abstract ReturnType runCommand(SuperDiscord instance, MessageReceivedEvent event, User sender, String... args) throws MissingPermissionException, SQLException;

    public abstract String getPermissionNode();

    public abstract String getSyntax();

    public abstract String getDescription();

    public boolean hasArgs() {
        return hasArgs;
    }

    public enum ReturnType {SUCCESS, FAILURE, SYNTAX_ERROR}

}
