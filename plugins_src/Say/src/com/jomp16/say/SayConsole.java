/*
 * Copyright (C) 2013 jomp16
 *
 * This file is part of Say
 *
 * Say is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Say is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Say. If not, see <http://www.gnu.org/licenses/>.
 */

package com.jomp16.say;

import com.jomp16.pircbotx.Main;
import com.jomp16.pircbotx.plugin.ConsolePlugin;

import java.util.HashMap;

public class SayConsole extends ConsolePlugin {
    private Vars vars;
    private HashMap<String, String> help = new HashMap<>();

    public SayConsole() throws Exception {
        vars = new Vars();
        help.put("say", vars.getLanguageManager().getString("Help", ""));
    }

    @Override
    public void run() {
        if (args.get(0).toLowerCase().equals("say")) {
            if (args.size() >= 3) {
                String channel = args.get(1), message = args.get(2);
                Main.getMultiBotManager().getBotById(0).getUserChannelDao().getChannel(channel).send().message(message);
                System.out.println("Message sended!");
            } else {
                System.out.println("Usage: say #channel 'message to send'");
            }
        }
    }

    @Override
    public HashMap<String, String> getHelp() {
        return help;
    }
}
