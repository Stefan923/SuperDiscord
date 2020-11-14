package me.stefan923.superdiscord.commands.discord.type.config;

import me.stefan923.superdiscord.SuperDiscord;
import me.stefan923.superdiscord.commands.discord.AbstractCommand;
import me.stefan923.superdiscord.language.LanguageManager;
import me.stefan923.superdiscord.settings.Setting;
import me.stefan923.superdiscord.utils.discord.DiscordMessageUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.bukkit.configuration.InvalidConfigurationException;

import javax.net.ssl.HttpsURLConnection;
import java.awt.*;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class CommandConfigSetLang extends AbstractCommand implements DiscordMessageUtils {
    private final LanguageManager languageManager;

    public CommandConfigSetLang(AbstractCommand abstractCommand) {
        super(abstractCommand, true, "setlang");
        languageManager = LanguageManager.getInstance();
    }

    @Override
    protected ReturnType runCommand(SuperDiscord instance, MessageReceivedEvent event, User sender, String... args) {
        if (args.length == 2) {
            String url = "https://hastebin.com/raw/" + args[1];
            try {
                languageManager.save(getFromHastebin(url));
            } catch (InvalidConfigurationException e) {
                return ReturnType.SYNTAX_ERROR;
            }
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setAuthor("SuperDiscord configuration - language", null, "https://i.imgur.com/Hm6QvYG.png");
            embedBuilder.setDescription("\nYour `language.yml` file content has been loaded from **Hastebin**.\n\u2022 Link: " + url);

            embedBuilder.setColor(Color.decode("0x0092e2"));
            embedBuilder.setFooter("Requested by " + sender.getName() + " | Answered in " + (System.currentTimeMillis() - event.getMessage().getTimeCreated().toInstant().toEpochMilli()) + " ms", sender.getAvatarUrl());
            sendEmbededMessage(event.getTextChannel(), embedBuilder);

            return ReturnType.SUCCESS;
        }
        return ReturnType.SYNTAX_ERROR;
    }

    public String getFromHastebin(String url) {
        try (Scanner scanner = new Scanner(new URL(url).openStream(),
                StandardCharsets.UTF_8.toString())) {
            scanner.useDelimiter("\\A");
            return scanner.hasNext() ? scanner.next() : "";
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    public String getPermissionNode() {
        return null;
    }

    @Override
    public String getSyntax() {
        return "**" + Setting.COMMAND_PREFIX + "config setLang <linkID>**\n\n`linkID` represents the id of the content you uploaded on Hastebin.\n";
    }

    @Override
    public String getDescription() {
        return null;
    }
}
