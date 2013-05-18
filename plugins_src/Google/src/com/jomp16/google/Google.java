/*
 * Copyright (C) 2013 jomp16
 *
 * This file is part of Google
 *
 * Google is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Google is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Google. If not, see <http://www.gnu.org/licenses/>.
 */

package com.jomp16.google;

import com.google.gson.Gson;
import com.jomp16.pircbotx.Main;
import com.jomp16.pircbotx.language.LanguageManager;
import com.jomp16.pircbotx.plugin.Help;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.http.client.fluent.Request;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.types.GenericMessageEvent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Google extends ListenerAdapter implements Help {
    private String prefix = Main.getPrefix(), GOOGLE = "http://ajax.googleapis.com/ajax/services/search/web?v=1.0&q=%s";
    private LanguageManager languageManager;
    private HashMap<String, String> help = new HashMap<>();

    public Google() throws IOException {
        String fileNameToFormat = "/lang/%s_%s.lang",
                fileNameFormatted = String.format(fileNameToFormat, System.getProperty("user.language"), System.getProperty("user.country")),
                jarPath = URLDecoder.decode(this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath(), "UTF-8");

        URL url = new URL("jar:file:" + jarPath + "!" + fileNameFormatted);
        languageManager = new LanguageManager(url);
    }

    @Override
    public void onGenericMessage(GenericMessageEvent event) throws Exception {
        ArrayList<String> args = new ArrayList<>();
        Matcher matcher = Pattern.compile("[^\\s\"']+|\"([^\"]*)\"|'([^']*)'").matcher(event.getMessage());
        while (matcher.find()) {
            if (matcher.group(1) != null) {
                // Add double-quoted string without the quotes
                args.add(matcher.group(1));
            } else if (matcher.group(2) != null) {
                // Add single-quoted string without the quotes
                args.add(matcher.group(2));
            } else {
                // Add unquoted word
                args.add(matcher.group());
            }
        }
        if (args.get(0).toLowerCase().equals(prefix + "google")) {
            if (args.size() >= 2) {
                String url = String.format(GOOGLE, URLEncoder.encode(args.get(1), "UTF-8"));
                BufferedReader reader = new BufferedReader(new InputStreamReader(Request.Get(url).execute().returnContent().asStream()));
                GoogleSearch search = new Gson().fromJson(reader, GoogleSearch.class);

                reader.close();
                if (!search.responseStatus.equals("200")) {
                    event.respond(languageManager.getString("Error"));
                    return;
                }
                if (search.responseData.results.size() <= 0) {
                    event.respond(languageManager.getString("NoResultsFound"));
                    return;
                }

                if (args.size() >= 3) {
                    for (int i = 0; i < Integer.parseInt(args.get(2)); i++) {
                        String title = StringEscapeUtils.unescapeHtml4(search.responseData.results.get(i).titleNoFormatting);
                        String url2 = URLDecoder.decode(search.responseData.results.get(i).unescapedUrl, "UTF-8");
                        event.respond(languageManager.getString("Result", (i + 1), title, url2));
                    }
                } else {
                    String title = StringEscapeUtils.unescapeHtml4(search.responseData.results.get(0).titleNoFormatting);
                    String url2 = URLDecoder.decode(search.responseData.results.get(0).unescapedUrl, "UTF-8");
                    event.respond(languageManager.getString("Result", 1, title, url2));
                }
            } else {
                event.respond(languageManager.getString("CommandSyntax", prefix));
            }
            args.clear();
        }
    }

    @Override
    public HashMap<String, String> getHelp() {
        help.put("google", languageManager.getString("Help1", prefix));
        return help;
    }
}
