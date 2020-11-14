package me.stefan923.superdiscord.commands.discord.type.config;

import me.stefan923.superdiscord.SuperDiscord;
import me.stefan923.superdiscord.commands.discord.AbstractCommand;
import me.stefan923.superdiscord.language.LanguageManager;
import me.stefan923.superdiscord.settings.Setting;
import me.stefan923.superdiscord.utils.discord.DiscordMessageUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import javax.net.ssl.HttpsURLConnection;
import java.awt.*;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class CommandConfigGetLang extends AbstractCommand implements DiscordMessageUtils {
    private final LanguageManager languageManager;

    public CommandConfigGetLang(AbstractCommand abstractCommand) {
        super(abstractCommand, false, "getlang");
        languageManager = LanguageManager.getInstance();
    }

    @Override
    protected ReturnType runCommand(SuperDiscord instance, MessageReceivedEvent event, User sender, String... args) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setAuthor("SuperDiscord configuration - language", null, "https://i.imgur.com/Hm6QvYG.png");
        try {
            embedBuilder.setDescription("\nYour `language.yml` file content has been uploaded.\n\u2022 Link: " + pasteToHastebin(languageManager.getConfig().saveToString(), false));
        } catch (IOException e) {
            return ReturnType.FAILURE;
        }
        embedBuilder.setColor(Color.decode("0x0092e2"));
        embedBuilder.setFooter("Requested by " + sender.getName() + " | Answered in " + (System.currentTimeMillis() - event.getMessage().getTimeCreated().toInstant().toEpochMilli()) + " ms", sender.getAvatarUrl());
        sendEmbededMessage(event.getTextChannel(), embedBuilder);

        return ReturnType.SUCCESS;
    }

    public String pasteToHastebin(String text, boolean raw) throws IOException {
        byte[] postData = text.getBytes(StandardCharsets.UTF_8);
        int postDataLength = postData.length;

        String requestURL = "https://hastebin.com/documents";
        URL url = new URL(requestURL);
        HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
        conn.setDoOutput(true);
        conn.setInstanceFollowRedirects(false);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("User-Agent", "Hastebin Java Api");
        conn.setRequestProperty("Content-Length", Integer.toString(postDataLength));
        conn.setUseCaches(false);

        String response = null;
        DataOutputStream wr;
        try {
            wr = new DataOutputStream(conn.getOutputStream());
            wr.write(postData);
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            response = reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (response.contains("\"key\"")) {
            response = response.substring(response.indexOf(":") + 2, response.length() - 2);

            String postURL = raw ? "https://hastebin.com/raw/" : "https://hastebin.com/";
            response = postURL + response;
        }

        return response;
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
