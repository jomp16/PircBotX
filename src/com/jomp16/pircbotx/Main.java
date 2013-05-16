/*
 * Copyright (C) 2013 jomp16
 *
 * This file is part of PircBotX
 *
 * PircBotX is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * PircBotX is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with PircBotX. If not, see <http://www.gnu.org/licenses/>.
 */

package com.jomp16.pircbotx;

import com.jomp16.pircbotx.language.LanguageManager;
import com.jomp16.pircbotx.logging.Logging;
import com.jomp16.pircbotx.plugin.PluginLoader;
import com.jomp16.pircbotx.sqlite.SQLiteConfigurator;
import com.jomp16.pircbotx.sqlite.SQLiteManager;
import lombok.Getter;
import org.pircbotx.Configuration;
import org.pircbotx.MultiBotManager;
import org.pircbotx.PircBotX;
import org.pircbotx.hooks.Listener;
import org.pircbotx.hooks.managers.ListenerManager;
import org.pircbotx.hooks.managers.ThreadedListenerManager;

import java.net.URL;
import java.net.URLDecoder;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings("unchecked")
public class Main {
    @Getter
    private static LanguageManager languageManager;
    @Getter
    private static Logging logging;
    @Getter
    private static Connection connection;
    @Getter
    private static ArrayList<Class> plugins;
    @Getter
    private static MultiBotManager multiBotManager = new MultiBotManager();
    @Getter
    private static String prefix;
    @Getter
    private static SQLiteManager sqLiteManager;
    @Getter
    private static ArrayList<String> OPs = new ArrayList<>();
    private static ListenerManager<PircBotX> manager = new ThreadedListenerManager<>();

    public static void main(String[] args) throws Exception {
        /*plugins = new PluginLoader().getPlugins();
        for (Class plugin : plugins) {
            Method method = plugin.getDeclaredMethod("getHelp");
            Object object = method.invoke(plugin.newInstance());
            ArrayList<String> arrayList = (ArrayList<String>) object;
            for (String a : arrayList) {
                System.out.println(a);
            }
        }*/
        executeStart();
        while (true) {
            // TODO
            ArrayList<String> args1 = new ArrayList<>();
            logging.write("--> ", false);
            Matcher matcher = Pattern.compile("[^\\s\"']+|\"([^\"]*)\"|'([^']*)'").matcher((CharSequence) logging.getInputString());
            while (matcher.find()) {
                if (matcher.group(1) != null) {
                    // Add double-quoted string without the quotes
                    args1.add(matcher.group(1));
                } else if (matcher.group(2) != null) {
                    // Add single-quoted string without the quotes
                    args1.add(matcher.group(2));
                } else {
                    // Add unquoted word
                    args1.add(matcher.group());
                }
            }
            switch (args1.get(0).toLowerCase()) {
                case "say":
                    if (args1.size() >= 3) {
                        String channel = args1.get(1);
                        String message = args1.get(2);
                        multiBotManager.getBotById(0).sendIRC().message(multiBotManager.getBotById(0).getUserChannelDao().getChannel(channel).getName(), message);
                        System.out.println("Message sended!");
                    } else {
                        System.out.println("Syntax: say <#channel> \"message\"");
                    }
                    break;
                case "exit":
                    System.out.println("Shutting down");
                    connection.close();
                    multiBotManager.stopAndWait();
                    System.exit(0);
                    break;
            }
        }
    }

    private static void executeStart() throws Exception {
        String fileNameToFormat = "/lang/%s_%s.lang";
        String fileNameFormatted = String.format(fileNameToFormat, System.getProperty("user.language"), System.getProperty("user.country"));
        boolean jar = Main.class.getResource("Main.class").toString().startsWith("jar:");
        URL url;
        if (jar) {
            String jarPath = URLDecoder.decode(Main.class.getProtectionDomain().getCodeSource().getLocation().getPath(), "UTF-8");
            url = new URL("jar:file:" + jarPath + "!" + fileNameFormatted);
        } else {
            url = new URL("file:" + System.getProperty("user.dir").replace("\\", "/") + fileNameFormatted);
        }
        languageManager = new LanguageManager(url);
        logging = new Logging();

        logging.writeLine("Welcome");

        logging.writeLine("LoadingSQLiteDatabase");
        doSQLite();
        logging.writeLine("SQLiteLoaded");
        prefix = sqLiteManager.getData("SELECT * FROM bot_config", "Prefix");

        logging.writeLine("LoadingPlugins");
        doPlugins();
        logging.writeLine("LoadedPlugins", manager.getListeners().size());

        logging.writeLine("LoadingBot");
        doPircBotX();
        logging.writeLine("LoadedBot");
    }

    private static void doSQLite() throws SQLException, ClassNotFoundException {
        sqLiteManager = new SQLiteManager(System.getProperty("user.dir").replace("\\", "/") + "/database.db");
        connection = sqLiteManager.getConnection();

        // Test if database has something inside
        try {
            connection.prepareStatement("SELECT * FROM bot_config").close();
        } catch (SQLException e) {
            logging.writeLine("SQLiteNotFound");
            new SQLiteConfigurator();
        }

        // Add the OPs here
        ResultSet resultSet = connection.prepareStatement("SELECT * FROM OPs").executeQuery();
        while (resultSet.next()) {
            OPs.add(resultSet.getString("OPIndent"));
        }
        resultSet.close();
    }

    private static void doPlugins() throws Exception {
        plugins = new PluginLoader().getPlugins();
        for (Class plugin : plugins) {
            manager.addListener((Listener) plugin.newInstance());
        }
    }

    private static void doPircBotX() throws Exception {
        multiBotManager.addBot(new Configuration.Builder()
                .setName(sqLiteManager.getData("SELECT * FROM bot_config", "Name"))
                .setLogin(sqLiteManager.getData("SELECT * FROM bot_config", "Indent"))
                .setVersion(sqLiteManager.getData("SELECT * FROM bot_config", "RealName"))
                .setAutoNickChange(true)
                .setServerHostname(sqLiteManager.getData("SELECT * FROM bot_config", "IRCServer"))
                .setListenerManager(manager)
                .buildConfiguration());
        multiBotManager.start();

        Thread.sleep(5000);

        ResultSet resultSet = connection.prepareStatement("SELECT * FROM channels").executeQuery();
        while (resultSet.next()) {
            multiBotManager.getBotById(0).sendIRC().joinChannel(resultSet.getString("channel"));
        }
        resultSet.close();
    }
}
