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

import java.util.HashMap;

public class HelpConsole extends ConsolePlugin {
    private HashMap<String, String> help = new HashMap<>();
    private Vars vars;
    public HelpConsole() {
        vars = new Vars();
        help.putAll(vars.getHelpConsole());
    }

    @Override
    public void run() {
        if (args.get(0).toLowerCase().equals("help")) {
            if (args.size() >= 2) {
                if (args.get(1).toLowerCase().equals("all")) {
                    StringBuilder builder = new StringBuilder();
                    for (String key : help.keySet()) {
                        builder.append(key).append(", ");
                    }
                    builder.delete(builder.length() - 2, builder.length());

                    System.out.println(Main.getLanguageManager().getString("AvailableHelp", builder.toString()));
                } else {
                    if (help.containsKey(args.get(1).toLowerCase())) {
                        System.out.println(Main.getLanguageManager().getString("Help", args.get(1).toLowerCase(), help.get(args.get(1).toLowerCase())));
                    } else {
                        System.out.println(Main.getLanguageManager().getString("HelpNoResult"));
                    }
                }
            } else {
                System.out.println(Main.getLanguageManager().getString("HelpSyntax"));
            }
        }
    }

    @Override
    public HashMap<String, String> getHelp() {
        return null;
    }
}
