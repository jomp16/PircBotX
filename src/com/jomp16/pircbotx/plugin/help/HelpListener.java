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
import com.jomp16.pircbotx.plugin.Help;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.types.GenericMessageEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HelpListener extends ListenerAdapter implements Help {
    private String prefix = Main.getPrefix();
    private HashMap<String, String> help = new HashMap<>();
    private Vars vars;
    public HelpListener() throws Exception {
        vars = new Vars();
        help.putAll(vars.getHelpPlugin());
    }

    @Override
    public void onGenericMessage(GenericMessageEvent event) throws Exception {
        ArrayList<String> args = new ArrayList<>();
        Matcher matcher = Pattern.compile("[^\\s\"']+|\"([^\"]*)\"|'([^']*)'").matcher(event.getMessage());
        while (matcher.find()) {
            if (matcher.group(1) != null) {
                args.add(matcher.group(1));
            } else if (matcher.group(2) != null) {
                args.add(matcher.group(2));
            } else {
                args.add(matcher.group());
            }
        }
        if (args.get(0).toLowerCase().equals(prefix + "help")) {
            if (args.size() >= 2) {
                if (args.get(1).toLowerCase().equals("all")) {
                    StringBuilder builder = new StringBuilder();
                    for (String key : help.keySet()) {
                        builder.append(key).append(", ");
                    }
                    builder.delete(builder.length() - 2, builder.length());

                    event.respond(Main.getLanguageManager().getString("AvailableHelp", builder.toString()));
                } else {
                    if (help.containsKey(args.get(1))) {
                        event.respond(Main.getLanguageManager().getString("Help", args.get(1).toLowerCase(), help.get(args.get(1).toLowerCase())));
                    } else {
                        event.respond(Main.getLanguageManager().getString("HelpNoResult"));
                    }
                }
            } else {
                event.respond(Main.getLanguageManager().getString("HelpSyntax", prefix));
            }
        }
    }

    @Override
    public HashMap<String, String> getHelp() {
        return null;
    }
}
