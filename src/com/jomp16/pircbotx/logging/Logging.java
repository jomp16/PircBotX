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

package com.jomp16.pircbotx.logging;

import com.jomp16.pircbotx.Main;

import java.util.Scanner;

public class Logging {
    private Scanner scanner = new Scanner(System.in);

    public void newLine() {
        System.out.println();
    }

    public void writeLine(String key) {
        System.out.println(Main.getLanguageManager().getString(key));
    }

    public void writeLine(String key, Object... params) {
        System.out.println(Main.getLanguageManager().getString(key, params));
    }

    public void write(Object object, boolean newLine) {
        if (newLine) {
            System.out.println(object);
        } else {
            System.out.print(object);
        }
    }

    private String humanReadableByteCount(long bytes, boolean si) {
        int unit = si ? 1000 : 1024;
        if (bytes < unit) {
            return bytes + " B";
        }
        int exp = (int) (Math.log(bytes) / Math.log(unit));
        String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp - 1) + (si ? "" : "i");

        return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
    }

    public void writeUsedRAM() {
        newLine();
    }

    public Object getInputString() {
        return scanner.nextLine();
    }
}
