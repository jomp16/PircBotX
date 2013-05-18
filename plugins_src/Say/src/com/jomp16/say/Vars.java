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
import com.jomp16.pircbotx.language.LanguageManager;
import lombok.Getter;

import java.net.URL;
import java.net.URLDecoder;

public class Vars {
    @Getter
    private LanguageManager languageManager;
    @Getter
    private String prefix = Main.getPrefix();

    public Vars() throws Exception {
        String fileNameToFormat = "/lang/%s_%s.lang",
                fileNameFormatted = String.format(fileNameToFormat, System.getProperty("user.language"), System.getProperty("user.country")),
                jarPath = URLDecoder.decode(this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath(), "UTF-8");

        URL url = new URL("jar:file:" + jarPath + "!" + fileNameFormatted);
        languageManager = new LanguageManager(url);
    }
}
