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

package com.jomp16.pircbotx.plugin;

import lombok.Getter;
import org.pircbotx.hooks.ListenerAdapter;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Properties;

@SuppressWarnings({"ConstantConditions", "unchecked"})
public class PluginLoader {
    @Getter
    private ArrayList<ListenerAdapter> plugins = new ArrayList<>();
    @Getter
    private ArrayList<ConsolePlugin> commandPlugin = new ArrayList<>();

    public PluginLoader() throws Exception {
        File f = new File(System.getProperty("user.dir").replace("\\", "/") + "/plugins");
        for (File file : f.listFiles()) {
            if (file.getName().endsWith(".jar")) {
                URLClassLoader classLoader = new URLClassLoader(new URL[]{file.toURI().toURL()}, this.getClass().getClassLoader());
                URL url = new URL("jar:file:" + file.getAbsolutePath() + "!/plugin.properties");
                InputStream inputStream = url.openStream();

                Properties properties = new Properties();
                properties.load(inputStream);

                if (properties.getProperty("Type").equals("console")) {
                    commandPlugin.add((ConsolePlugin) Class.forName(properties.getProperty("MainClass"), true, classLoader).newInstance());
                } else if (properties.getProperty("Type").equals("plugin")) {
                    plugins.add((ListenerAdapter) Class.forName(properties.getProperty("MainClass"), true, classLoader).newInstance());
                } else if (properties.getProperty("Type").equals("console&plugin")) {
                    commandPlugin.add((ConsolePlugin) Class.forName(properties.getProperty("MainClass-Console"), true, classLoader).newInstance());
                    plugins.add((ListenerAdapter) Class.forName(properties.getProperty("MainClass-Plugin"), true, classLoader).newInstance());
                }
            }
        }
    }
}
