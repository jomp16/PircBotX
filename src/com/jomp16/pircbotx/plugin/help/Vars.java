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

package com.jomp16.pircbotx.plugin.help;

import com.jomp16.pircbotx.Main;
import com.jomp16.pircbotx.plugin.ConsolePlugin;
import lombok.Getter;
import org.pircbotx.hooks.ListenerAdapter;

import java.util.HashMap;

public class Vars {
    @Getter
    private HashMap<String, String> helpPlugin = new HashMap<>();
    @Getter
    private HashMap<String, String> helpConsole = new HashMap<>();

    public Vars() {
        try {
            addHelpConsole();
            addHelpPlugin();
        } catch (Exception e) {
            // Ignore it
        }
    }

    private void addHelpConsole() throws Exception {
        for (ConsolePlugin consolePlugin : Main.getConsolePlugins()) {
            helpConsole.putAll(consolePlugin.getHelp());
        }
    }

    private void addHelpPlugin() {
        for (ListenerAdapter plugin : Main.getPlugins()) {
            try {
                com.jomp16.pircbotx.plugin.Help help = (com.jomp16.pircbotx.plugin.Help) plugin;
                helpPlugin.putAll(help.getHelp());
            } catch (Exception e) {
                // Ignore it
            }
        }
    }
}
