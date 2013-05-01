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

import java.util.List;

public class GoogleSearch {
    public String responseStatus;
    public ResponseData responseData;

    public class ResponseData {
        public List<Result> results;

        public class Result {
            public String unescapedUrl;
            public String url;
            public String visibleUrl;
            public String title;
            public String titleNoFormatting;
            public String content;
        }
    }
}
