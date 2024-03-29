package com.shirwa.simplistic_rss;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import android.util.Log;

import java.util.List;

/*
 * Copyright (C) 2014 Shirwa Mohamed <shirwa99@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

public class RssReader {
    private String rssUrl;

    public RssReader(String url) {
        rssUrl = url;
    }

    public List<RssItem> getItems() throws Exception {
    	Log.v("getItems", "1");
        SAXParserFactory factory = SAXParserFactory.newInstance();
        Log.v("getItems", "2");
        SAXParser saxParser = factory.newSAXParser();
        Log.v("getItems", "3");
        //Creates a new RssHandler which will do all the parsing.
        RssHandler handler = new RssHandler();
        Log.v("getItems", "4");
        //Pass SaxParser the RssHandler that was created.
        saxParser.parse(rssUrl, handler);
        Log.v("getItems", "5");
        return handler.getRssItemList();
    }
}
