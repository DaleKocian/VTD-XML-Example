package io.github.dkocian.vtd_xml_example;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import io.github.dkocian.vtd_xml_example.model.Entry;
import io.github.dkocian.vtd_xml_example.network.XmlRequest;
import io.github.dkocian.vtd_xml_example.utils.Constants;
import io.github.dkocian.vtd_xml_example.utils.JsonKeys;
import io.github.dkocian.vtd_xml_example.utils.Urls;

public class VtdXmlActivity extends ActionBarActivity {
    public static final String PLEASE_WAIT = "Please wait";
    public static final String DOWNLOAD_IN_PROGRESS = "Download in progress..";
    private static final String TAG = VtdXmlActivity.class.getName();
    @InjectView(R.id.wvXml)
    WebView wvXml;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vtd_xml_ui);
        ButterKnife.inject(this);
        RequestQueue queue = Volley.newRequestQueue(this);
        XmlRequest<Entry> xmlRequest = new XmlRequest<>(Request.Method.GET, Urls.STACK_OVERFLOW_URL, new Response.Listener<ArrayList<Entry>>() {
            @Override
            public void onResponse(ArrayList<Entry> response) {
                StringBuilder out = new StringBuilder();
                for (Entry entry : response) {
                    out.append("<html><body>")
                            .append("<b>Title: </b>")
                            .append(entry.getTitle())
                            .append("<br/><b>Link: </b><a href=\"")
                            .append(entry.getLink())
                            .append("\">")
                            .append(entry.getLink())
                            .append("</a>")
                            .append("<br/><b>Summary: </b>")
                            .append(entry.getSummary())
                            .append("<br/><br/>")
                            .append("</body></html>");
                }
                wvXml.loadData(out.toString(), Constants.TEXT_HTML, null);
                mProgressDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }, getParsingStructure(), Entry.class);
        // Add the request to the RequestQueue.
        mProgressDialog = ProgressDialog.show(this, PLEASE_WAIT, DOWNLOAD_IN_PROGRESS);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCancelable(true);
        queue.add(xmlRequest);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private JSONObject getParsingStructure() {
        JSONObject parsingStructure = new JSONObject();
        try {
            parsingStructure.put(JsonKeys.ROOT, JsonKeys.ENTRY);
            JSONArray jsonArray = new JSONArray();
            jsonArray.put(new JSONObject().put(JsonKeys.ELEMENT, Entry.TITLE));
            jsonArray.put(new JSONObject().put(JsonKeys.ELEMENT, Entry.LINK).put(JsonKeys.ATTRS, new JSONArray().put(new JSONObject().put
                    (JsonKeys.ATTR + 1, Entry.HREF))));
            jsonArray.put(new JSONObject().put(JsonKeys.ELEMENT, Entry.SUMMARY));
            parsingStructure.put(JsonKeys.ELEMENTS, jsonArray);
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }
        return parsingStructure;
    }

    @Override
    public void onBackPressed() {
        if (wvXml != null && wvXml.canGoBack()) {
            wvXml.goBack();
        } else {
            super.onBackPressed();
        }
    }
}
