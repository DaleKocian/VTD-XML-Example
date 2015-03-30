package io.github.dkocian.vtd_xml_example.model;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by dkocian on 3/29/2015.
 */
public class Entry extends XmlModel {
    private static final String TAG = Entry.class.getName();
    public static final String TITLE = "title";
    public static final String LINK = "link";
    public static final String HREF = "href";
    public static final String SUMMARY = "summary";
    public String title;
    public String link;
    public String summary;

    public Entry() {
    }

    public Entry(String title, String link, String summary) {
        this.title = title;
        this.link = link;
        this.summary = summary;
    }

    public Entry getInstance(JSONObject jsonObject) {
        String summary;
        String link = null;
        String title = null;
        try {
            title = jsonObject.getString(TITLE);
            link = jsonObject.getString(HREF);
            summary = jsonObject.getString(SUMMARY);
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
            summary = "";
            if (link == null) {
                link = "";
            }
            if (title == null) {
                title = "";
            }
        }
        return new Entry(title, link, summary);
    }
}
