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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Properties;

@SuppressWarnings("ConstantConditions")
public class PluginLoader {
    private ArrayList<Class> plugins = new ArrayList<>();

    public PluginLoader() throws IOException, ClassNotFoundException {
        File f = new File(System.getProperty("user.dir").replace("\\", "/") + "/plugins");
        for (File file : f.listFiles()) {
            if (file.getName().endsWith(".jar")) {
                URLClassLoader classLoader = new URLClassLoader(new URL[]{file.toURI().toURL()}, this.getClass().getClassLoader());
                URL url = new URL("jar:file:" + file.getAbsolutePath() + "!/plugin.properties");
                InputStream inputStream = url.openStream();

                Properties properties = new Properties();
                properties.load(inputStream);

                plugins.add(Class.forName(properties.getProperty("MainClass"), true, classLoader));
            }
        }
    }

    public ArrayList<Class> getPlugins() {
        return plugins;
    }
}
