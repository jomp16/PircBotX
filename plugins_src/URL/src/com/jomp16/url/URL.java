/*
 * Copyright (C) 2013 jomp16
 *
 * This file is part of URL
 *
 * URL is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * URL is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with URL. If not, see <http://www.gnu.org/licenses/>.
 */

package com.jomp16.url;

import com.jomp16.pircbotx.language.LanguageManager;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;
import org.pircbotx.hooks.events.PrivateMessageEvent;

import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.net.URLDecoder;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

public class URL extends ListenerAdapter {
    private LanguageManager languageManager;

    public URL() throws IOException, NoSuchAlgorithmException, KeyManagementException {
        String fileNameToFormat = "/lang/%s_%s.lang",
                fileNameFormatted = String.format(fileNameToFormat, System.getProperty("user.language"), System.getProperty("user.country")),
                jarPath = URLDecoder.decode(this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath(), "UTF-8");

        java.net.URL url = new java.net.URL("jar:file:" + jarPath + "!" + fileNameFormatted);
        languageManager = new LanguageManager(url);

        SSLContext context = SSLContext.getInstance("TLS");
        context.init(new KeyManager[0], new TrustManager[]{new DefaultTrustManager()}, new SecureRandom());
        SSLContext.setDefault(context);
    }

    @Override
    public void onMessage(MessageEvent event) throws IOException {
        if (event.getMessage().contains("http://") || event.getMessage().contains("https://") || event.getMessage().startsWith("www.")) {
            String raw = event.getMessage().startsWith("www.") ? new StringBuilder(event.getMessage()).insert(0, "http://").toString() : event.getMessage();
            Document document = Jsoup.connect(raw).followRedirects(true).get().normalise();
            StringBuilder builder = new StringBuilder();
            builder.append(languageManager.getString("Text", document.title(), raw));
            event.getBot().sendIRC().message(event.getChannel().getName(), builder.toString());
        }
    }

    @Override
    public void onPrivateMessage(PrivateMessageEvent event) throws IOException {
        if (event.getMessage().contains("http://") || event.getMessage().contains("https://") || event.getMessage().startsWith("www.")) {
            String raw = event.getMessage().startsWith("www.") ? new StringBuilder(event.getMessage()).insert(0, "http://").toString() : event.getMessage();
            Document document = Jsoup.connect(raw).followRedirects(true).get().normalise();
            StringBuilder builder = new StringBuilder();
            builder.append(languageManager.getString("Text", document.title(), raw));
            event.getBot().sendIRC().message(event.getUser().getNick(), builder.toString());
        }
    }

    private static class DefaultTrustManager implements X509TrustManager {
        @Override
        public void checkClientTrusted(X509Certificate[] xcs, String string) throws CertificateException {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] xcs, String string) throws CertificateException {
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }
    }
}
