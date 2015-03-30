package io.github.dkocian.vtd_xml_example.utils;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import io.github.dkocian.vtd_xml_example.model.Entry;

/**
 * Created by dkocian on 3/30/2015.
 */
public class StackOverflowXmlParser {
    // We don't use namespaces
    private static final String ns = null;
    private static final String ENTRY = "entry";
    private static final String FEED = "feed";
    private static final String REL = "rel";
    private static final String ALTERNATE = "alternate";

    public List<Entry> parse(InputStream in) throws XmlPullParserException, IOException {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            return readFeed(parser);
        } finally {
            in.close();
        }
    }

    private List<Entry> readFeed(XmlPullParser parser) throws XmlPullParserException, IOException {
        List<Entry> entries = new ArrayList<>();
        parser.require(XmlPullParser.START_TAG, ns, FEED);
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            // Starts by looking for the entry tag
            if (name.equals(ENTRY)) {
                entries.add(readEntry(parser));
            } else {
                skip(parser);
            }
        }
        return entries;
    }

    // Parses the contents of an entry. If it encounters a title, summary, or link tag, hands them off
    // to their respective "read" methods for processing. Otherwise, skips the tag.
    private Entry readEntry(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, ENTRY);
        String title = null;
        String summary = null;
        String link = null;
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            switch (name) {
                case Entry.TITLE:
                    title = readTitle(parser);
                    break;
                case Entry.SUMMARY:
                    summary = readSummary(parser);
                    break;
                case Entry.LINK:
                    link = readLink(parser);
                    break;
                default:
                    skip(parser);
                    break;
            }
        }
        return new Entry(title, summary, link);
    }

    // Processes title tags in the feed.
    private String readTitle(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, Entry.TITLE);
        String title = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, Entry.TITLE);
        return title;
    }

    // Processes link tags in the feed.
    private String readLink(XmlPullParser parser) throws IOException, XmlPullParserException {
        String link = "";
        parser.require(XmlPullParser.START_TAG, ns, Entry.LINK);
        String tag = parser.getName();
        String relType = parser.getAttributeValue(null, REL);
        if (tag.equals(Entry.LINK)) {
            if (relType.equals(ALTERNATE)) {
                link = parser.getAttributeValue(null, Entry.HREF);
                parser.nextTag();
            }
        }
        parser.require(XmlPullParser.END_TAG, ns, Entry.LINK);
        return link;
    }

    // Processes summary tags in the feed.
    private String readSummary(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, Entry.SUMMARY);
        String summary = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, Entry.SUMMARY);
        return summary;
    }

    // For the tags title and summary, extracts their text values.
    private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = "";
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
