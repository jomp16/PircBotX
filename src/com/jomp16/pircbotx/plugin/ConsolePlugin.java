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

import com.jomp16.pircbotx.language.LanguageManager;

import java.util.ArrayList;

public abstract class ConsolePlugin implements Runnable, Help {
    public ArrayList<String> args = new ArrayList<>();
    public LanguageManager languageManager;

    public void setArgs(ArrayList<String> args) {
        this.args = args;
    }
}
