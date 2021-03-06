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

package com.jomp16.pircbotx.language;

import java.io.IOException;
import java.net.URL;
import java.util.Properties;

public class LanguageManager {
    private Properties properties = new Properties();

    public LanguageManager(URL url) throws IOException {
        try {
            properties.load(url.openStream());
        } catch (IOException e) {
            // If file not found, load the default language
            String raw = url.getFile().substring(0, url.getFile().length() - 10) + "en_US.lang";
            properties.load(new URL("jar:" + raw).openStream());
        }
    }

    public String getString(String key) {
        try {
            return new String(properties.getProperty(key).getBytes("ISO-8859-1"), "UTF-8");
        } catch (Exception e) {
            // Ignore
        }
        return null;
    }

    public String getString(String key, Object... param) {
        try {
            return String.format(new String(properties.getProperty(key).getBytes("ISO-8859-1"), "UTF-8"), param);
        } catch (Exception e) {
            // Ignore
        }
        return null;
    }
}
