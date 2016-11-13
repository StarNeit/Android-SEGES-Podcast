package com.seges.newpodcast.model;

import android.os.AsyncTask;
import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by alexeyreznik on 02/03/16.
 */
public class RSSLoader {
    private static final String TAG = RSSLoader.class.getSimpleName();
    //private static final String FEED_URL = "https://segespodcast.wordpress.com/feed/";
    private static final String FEED_URL = "http://podcast.seges.dk/feed/podcast/";


    private static final int TAG_TITLE = 1;
    private static final int TAG_LINK = 2;
    private static final int TAG_DATE = 3;
    private static final int TAG_DESCRIPTION = 4;
    private static final int TAG_THUMBNAIL = 5;
    private static final int TAG_AUDIO = 6;
    private static final int TAG_CATEGORIES = 7;

    private static Integer idCount = 0;

    // We don't use XML namespaces
    private static final String ns = null;

    private static RSSLoader instance;

    public static RSSLoader getInstance() {
        if (instance == null) {
            instance = new RSSLoader();
        }
        return instance;
    }

    private RSSLoader() {

    }

    public void loadConferences(OnConferencesLoadedListener listener) {
        new ParseFeedAsyncTask(listener).execute();
    }

    private class ParseFeedAsyncTask extends AsyncTask<Void, Void, List<Conference>> {
        private OnConferencesLoadedListener listener;

        public ParseFeedAsyncTask(OnConferencesLoadedListener listener) {
            this.listener = listener;
        }

        @Override
        protected void onPostExecute(List<Conference> conferences) {
            super.onPostExecute(conferences);
            listener.onConferencesLoaded(conferences);
        }

        @Override
        protected List<Conference> doInBackground(Void... params) {
            List<Conference> conferences = new ArrayList<Conference>();

            try {
                URL url = new URL(FEED_URL);
                URLConnection urlConnection = url.openConnection();
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());

                XmlPullParser parser = Xml.newPullParser();
                parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                parser.setInput(in, null);
                parser.nextTag();

                Log.d(TAG, "Parsing the feed");

                conferences = readFeed(parser);
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            Log.d(TAG, "Loaded " + conferences.size() + " Conference items");
            return conferences;
        }

        private List<Conference> readFeed(XmlPullParser parser) throws IOException, XmlPullParserException {
            List<Conference> items = new ArrayList<Conference>();

            parser.require(XmlPullParser.START_TAG, ns, "rss");

            while (parser.next() != XmlPullParser.END_TAG) {
                if (parser.getEventType() != XmlPullParser.START_TAG) {
                    continue;
                }

                String name = parser.getName();
                if (name.equals("channel")) {
                    items = readChannel(parser);
                } else {
                    skip(parser);
                }
            }

            return items;
        }

        private List<Conference> readChannel(XmlPullParser parser) throws IOException, XmlPullParserException {
            List<Conference> items = new ArrayList<Conference>();

            parser.require(XmlPullParser.START_TAG, ns, "channel");

            while (parser.next() != XmlPullParser.END_TAG) {
                if (parser.getEventType() != XmlPullParser.START_TAG) {
                    continue;
                }

                String name = parser.getName();
                if (name.equals("item")) {
                    items.add(readItem(parser));
                } else {
                    skip(parser);
                }
            }

            return items;
        }

        private Conference readItem(XmlPullParser parser) throws IOException, XmlPullParserException {
            parser.require(XmlPullParser.START_TAG, ns, "item");

            String title = null;
            String link = null;
            Date date = null;
            String description = null;
            String thumbnailUrl = null;
            String audioUrl = null;
            ArrayList<String> categories = new ArrayList<String>();

            while (parser.next() != XmlPullParser.END_TAG) {
                if (parser.getEventType() != XmlPullParser.START_TAG) {
                    continue;
                }
                String name = parser.getName();
                if (name.equals("title")) {
                    title = readTag(parser, TAG_TITLE);
                } else if (name.equals("link")) {
                    link = readTag(parser, TAG_LINK);
                } else if (name.equals("pubDate")) {
                    String dateStr = readTag(parser, TAG_DATE);
                    SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z");
                    try {
                        date = format.parse(dateStr);
                        System.out.println(date);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                } else if (name.equals("description")) {
                    description = readTag(parser, TAG_DESCRIPTION);
                } /*else if (name.equals("media:thumbnail")) {
                    thumbnailUrl = readTag(parser, TAG_THUMBNAIL);
                } */else if (name.equals("enclosure")) {
                    audioUrl = readTag(parser, TAG_AUDIO);
                } else if (name.equals("category")) {
                    categories.add(readTag(parser, TAG_CATEGORIES));
                } else if (name.equals("content:encoded")){

                    String result = readText(parser), firstStr = "<img src=\"";
                    int start = result.indexOf(firstStr);
                    int end = result.substring(start + firstStr.length()).indexOf("\"");
                    thumbnailUrl = result.substring(start + firstStr.length()).substring(0, end);

                } else{
                    skip(parser);
                }
            }

            Conference conference = new Conference(idCount.toString());
            idCount++;
            if (title != null) {
                conference.setTitle(title);
            }

            if (link != null) {
                conference.setLink(link);
            }

            if (description != null) {
                conference.setDescription(description);
            }

            if (date != null) {
                conference.setDate(date);
            }

            if (thumbnailUrl != null) {
                conference.setThumbnailUrl(thumbnailUrl);
            }

            if (audioUrl != null) {
                conference.setAudioUrl(audioUrl);
            }

            if (categories != null && !categories.isEmpty()) {
                conference.setCategories(categories);
            }

            return conference;
        }

        private String readTag(XmlPullParser parser, int tagType)
                throws IOException, XmlPullParserException {
            String tag = null;
            String endTag = null;

            switch (tagType) {
                case TAG_TITLE:
                    return readBasicTag(parser, "title");
                case TAG_LINK:
                    return readBasicTag(parser, "link");
                case TAG_DATE:
                    return readBasicTag(parser, "pubDate");
                case TAG_DESCRIPTION:
                    return readDescriptionTag(parser, "description");
                /*case TAG_THUMBNAIL:
                    return readUrlTag(parser, "media:thumbnail");*/
                case TAG_AUDIO:
                    return readAudioUrl(parser, "enclosure");
                case TAG_CATEGORIES:
                    return readBasicTag(parser, "category");
                default:
                    throw new IllegalArgumentException("Unknown tag type: " + tagType);
            }
        }

        private String readDescriptionTag(XmlPullParser parser, String tag) throws IOException, XmlPullParserException {
            parser.require(XmlPullParser.START_TAG, ns, tag);
            String result = readText(parser);
            parser.require(XmlPullParser.END_TAG, ns, tag);
            result = result.split("<")[0];
            return result;
        }

        private String readAudioUrl(XmlPullParser parser, String tag) throws IOException, XmlPullParserException {
            String link = null;
            parser.require(XmlPullParser.START_TAG, ns, tag);
            if (parser.getAttributeValue(null, "type").equals("audio/mpeg"))
            {
                link = parser.getAttributeValue(null, "url");
                parser.nextTag();
            } else {
                skip(parser);
            }
            return link;
        }
/*
        private String readUrlTag(XmlPullParser parser, String tag) throws IOException, XmlPullParserException {
            String link = null;
            parser.require(XmlPullParser.START_TAG, ns, tag);
            link = parser.getAttributeValue(null, "url");
            parser.nextTag();
            parser.require(XmlPullParser.END_TAG, ns, tag);
            return link;
        }*/

        private String readBasicTag(XmlPullParser parser, String tag)
                throws IOException, XmlPullParserException {
            parser.require(XmlPullParser.START_TAG, ns, tag);
            String result = readText(parser);
            parser.require(XmlPullParser.END_TAG, ns, tag);
            return result;
        }

        private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
            String result = null;
            if (parser.next() == XmlPullParser.TEXT) {
                result = parser.getText();
                parser.nextTag();
            }
            return result;
        }

        private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                throw new IllegalStateException();
            }
            int depth = 1;
            while (depth != 0) {
                switch (parser.next()) {
                    case XmlPullParser.END_TAG:
                        depth--;
                        break;
                    case XmlPullParser.START_TAG:
                        depth++;
                        break;
                }
            }
        }
    }
}
