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
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.http.client.fluent.Request;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;
import org.pircbotx.hooks.events.PrivateMessageEvent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;

public class Google extends ListenerAdapter {
    private String prefix = Main.getPrefix();
    private String GOOGLE = "http://ajax.googleapis.com/ajax/services/search/web?v=1.0&q=%s";
    private LanguageManager languageManager;

    public Google() throws IOException {
        String fileNameToFormat = "/languages/%s_%s.lang";
        String fileNameFormatted = String.format(fileNameToFormat, System.getProperty("user.language"), System.getProperty("user.country"));
        languageManager = new LanguageManager(getClass().getResourceAsStream(fileNameFormatted), true);
    }

    @Override
    public void onMessage(MessageEvent event) throws IOException {
        if (event.getMessage().startsWith(prefix + "google")) {
            if (event.getMessage().length() >= 11) {
                String raw1 = event.getMessage().substring(10);
                if (raw1.length() >= 1) {
                    int index1 = raw1.indexOf("\"");
                    String term = raw1.substring(0, index1);
                    int num = (raw1.substring(index1 + 1).length() >= 2) ? Integer.parseInt(raw1.substring(index1 + 2)) : 1;

                    String url = String.format(GOOGLE, URLEncoder.encode(term, "UTF-8"));
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

                    for (int i = 0; i < num; i++) {
                        String title = StringEscapeUtils.unescapeHtml4(search.responseData.results.get(i).titleNoFormatting);
                        String url2 = URLDecoder.decode(search.responseData.results.get(i).unescapedUrl, "UTF-8");
                        event.respond(languageManager.getString("Result", (i + 1), title, url2));
                    }
                }
            } else {
                event.respond(languageManager.getString("CommandSyntax", prefix));
            }
        }
    }

    @Override
    public void onPrivateMessage(PrivateMessageEvent event) throws IOException {
        if (event.getMessage().startsWith(prefix + "google")) {
            if (event.getMessage().length() >= 11) {
                String raw1 = event.getMessage().substring(10);
                if (raw1.length() >= 1) {
                    int index1 = raw1.indexOf("\"");
                    String term = raw1.substring(0, index1);
                    int num = (raw1.substring(index1 + 1).length() >= 2) ? Integer.parseInt(raw1.substring(index1 + 2)) : 1;

                    String url = String.format(GOOGLE, URLEncoder.encode(term, "UTF-8"));
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

                    for (int i = 0; i < num; i++) {
                        String title = StringEscapeUtils.unescapeHtml4(search.responseData.results.get(i).titleNoFormatting);
                        String url2 = URLDecoder.decode(search.responseData.results.get(i).unescapedUrl, "UTF-8");
                        event.respond(languageManager.getString("Result", (i + 1), title, url2));
                    }
                }
            } else {
                event.respond(languageManager.getString("CommandSyntax", prefix));
            }
        }
    }

    public HashMap<String, String> getCommands() {
        HashMap<String, String> commands = new HashMap<>();
        commands.put("google", languageManager.getString("Help1", prefix));
        return commands;
    }
}
