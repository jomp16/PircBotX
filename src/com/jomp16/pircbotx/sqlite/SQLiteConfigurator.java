/*
 * Copyright (C) 2013 jomp16
 *
 * This file is part of PircBotX.
 *
 * PircBotX is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * PircBotX is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with PircBotX.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.jomp16.pircbotx.sqlite;

import com.jomp16.pircbotx.Main;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

public class SQLiteConfigurator {
    private String botName, realName, indent, prefix, ircHost;
    private ArrayList<String> OPs = new ArrayList<>();
    private ArrayList<String> channels = new ArrayList<>();

    public SQLiteConfigurator() throws SQLException {
        Welcome();
        Step1();
        Step2();
        Step3();
        Step4();
        Step5();
        Step6();
        Step7();
        createDatabase();
        Finished();
    }

    private void Welcome() {
        Main.getLogging().writeLine("SQLiteWelcome");
    }

    private void Step1() {
        Main.getLogging().writeLine("SQLiteStep1");
        Main.getLogging().write("--> ", false);
        botName = (String) Main.getLogging().getInputString();
    }

    private void Step2() {
        Main.getLogging().writeLine("SQLiteStep2");
        Main.getLogging().write("--> ", false);
        realName = (String) Main.getLogging().getInputString();
    }

    private void Step3() {
        Main.getLogging().writeLine("SQLiteStep3");
        Main.getLogging().write("--> ", false);
        indent = (String) Main.getLogging().getInputString();
    }

    private void Step4() {
        while (true) {
            Main.getLogging().writeLine("SQLiteStep4-1");
            Main.getLogging().write("--> ", false);
            OPs.add((String) Main.getLogging().getInputString());
            Main.getLogging().writeLine("SQLiteStep4-2");
            Main.getLogging().write("--> ", false);
            String raw = ((String) Main.getLogging().getInputString()).toLowerCase();
            if (raw.equals(Main.getLanguageManager().getString("CancelChar")) || raw.length() == 0) {
                break;
            }
        }
    }

    private void Step5() {
        Main.getLogging().writeLine("SQLiteStep5");
        Main.getLogging().write("--> ", false);
        prefix = (String) Main.getLogging().getInputString();
    }

    private void Step6() {
        Main.getLogging().writeLine("SQLiteStep6");
        Main.getLogging().write("--> ", false);
        ircHost = (String) Main.getLogging().getInputString();
    }

    private void Step7() {
        while (true) {
            Main.getLogging().writeLine("SQLiteStep7-1");
            Main.getLogging().write("--> ", false);
            channels.add((String) Main.getLogging().getInputString());
            Main.getLogging().writeLine("SQLiteStep7-2");
            Main.getLogging().write("--> ", false);
            String raw = ((String) Main.getLogging().getInputString()).toLowerCase();
            if (raw.equals(Main.getLanguageManager().getString("CancelChar")) || raw.length() == 0) {
                break;
            }
        }
    }

    private void Finished() {
        Main.getLogging().writeLine("SQLiteFinished");
    }

    private void createDatabase() throws SQLException {
        Main.getConnection().prepareStatement("CREATE TABLE bot_config (Name string NOT NULL, RealName string NOT NULL, Indent string NOT NULL, Prefix string NOT NULL, IRCServer string NOT NULL)").executeUpdate();
        Main.getConnection().prepareStatement("CREATE TABLE OPs (OPIndent string NOT NULL)").executeUpdate();
        Main.getConnection().prepareStatement("CREATE TABLE channels (channel string NOT NULL)").executeUpdate();

        // Insert the data to table bot_config
        PreparedStatement preparedStatement = Main.getConnection().prepareStatement("INSERT INTO bot_config VALUES (?, ?, ?, ?, ?)");
        preparedStatement.setString(1, botName);
        preparedStatement.setString(2, realName);
        preparedStatement.setString(3, indent);
        preparedStatement.setString(4, prefix);
        preparedStatement.setString(5, ircHost);
        preparedStatement.executeUpdate();
        preparedStatement.close();

        // Insert the OPs to table OPs
        preparedStatement = Main.getConnection().prepareStatement("INSERT INTO OPs VALUES (?)");
        for (String OP : OPs) {
            preparedStatement.setString(1, OP);
            preparedStatement.executeUpdate();
            preparedStatement.clearParameters();
        }
        preparedStatement.close();

        // Insert the channels to join
        preparedStatement = Main.getConnection().prepareStatement("INSERT INTO channels VALUES (?)");
        for (String channel : channels) {
            preparedStatement.setString(1, channel);
            preparedStatement.executeUpdate();
            preparedStatement.clearParameters();
        }
        preparedStatement.close();
    }
}
