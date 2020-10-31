package me.stefan923.superdiscord.listeners.discord;

import me.stefan923.superdiscord.SuperDiscord;
import me.stefan923.superdiscord.commands.discord.CommandManager;
import me.stefan923.superdiscord.settings.Setting;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.Arrays;

public class MessageReceivedListener extends ListenerAdapter {

    private final CommandManager commandManager;

    public MessageReceivedListener(SuperDiscord plugin) {
        this.commandManager = plugin.getDiscordCommandManager();
    }

    public void onMessageReceived(MessageReceivedEvent event) {
        String message = event.getMessage().getContentRaw();

        if (message.startsWith(Setting.COMMAND_PREFIX)) {
            System.out.println(event.getAuthor().getIdLong() + " executed command: " + message);
            String[] split = message.replaceFirst(Setting.COMMAND_PREFIX, "").split(" ");

            commandManager.onCommand(event, split[0], Arrays.copyOfRange(split, 1, split.length));
        }
    }

}
