package io.github.dkocian.vtd_xml_example.network;

import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.JsonSyntaxException;
import com.ximpleware.AutoPilot;
import com.ximpleware.NavException;
import com.ximpleware.ParseException;
import com.ximpleware.PilotException;
import com.ximpleware.VTDGen;
import com.ximpleware.VTDNav;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import io.github.dkocian.vtd_xml_example.model.XmlModel;
import io.github.dkocian.vtd_xml_example.utils.JsonKeys;

/**
 * Created by dkocian on 3/30/2015.
 */
public class XmlRequest<T extends XmlModel> extends Request<ArrayList<T>> {
    private static final String TAG = XmlRequest.class.getName();
    private final Response.Listener<ArrayList<T>> listener;
    private final JSONObject parsingStructure;
    final Class<T> typeParameterClass;

    public XmlRequest(int method, String url, Response.Listener<ArrayList<T>> listener, Response.ErrorListener errorListener,
                      JSONObject parsingStructure, Class<T> typeParameterClass) {
        super(method, url, errorListener);
        this.listener = listener;
        this.parsingStructure = parsingStructure;
        this.typeParameterClass = typeParameterClass;
    }

    @Override
    protected Response<ArrayList<T>> parseNetworkResponse(NetworkResponse response) {
        try {
            String xml = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            ArrayList<T> entries = parse(xml);
            return Response.success(entries, HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JsonSyntaxException e) {
            return Response.error(new ParseError(e));
        }
    }

    @Override
    protected void deliverResponse(ArrayList<T> response) {
        listener.onResponse(response);
    }

    private ArrayList<T> parse(String result) {
        ArrayList<T> entryList = new ArrayList<>();
        VTDGen vg = new VTDGen();
        vg.setDoc(result.getBytes());
        try {
            vg.parse(true);
            VTDNav vn = vg.getNav();
            vn.toElement(VTDNav.ROOT);
            AutoPilot ap = new AutoPilot(vn);
            ap.selectElement(parsingStructure.getString(JsonKeys.ROOT));
            JSONArray elements = parsingStructure.getJSONArray(JsonKeys.ELEMENTS);
            int count = 0;
            while (ap.iterate()) {
                JSONObject payload = new JSONObject();
                for (int i = 0; i <= parsingStructure.length(); ++i) {
                    JSONObject nextJsonObject = elements.getJSONObject(i);
                    String text;
                    int val;
                    if (i == 0) {
                        if (vn.toElement(VTDNav.FC, nextJsonObject.getString(JsonKeys.ELEMENT))) {
                            val = vn.getText();
                            if (val != -1) {
                                text = vn.toNormalizedString(val);
                                payload.put(nextJsonObject.getString(JsonKeys.ELEMENT), text);
                            }
                        }
                    } else if (vn.toElement(VTDNav.NEXT_SIBLING, nextJsonObject.getString(JsonKeys.ELEMENT))) {
                        try {
                            JSONArray attrs = nextJsonObject.getJSONArray(JsonKeys.ATTRS);
                            JSONObject attr = attrs.getJSONObject(0);
                            val = vn.getAttrVal(attr.getString(JsonKeys.ATTR + 1));
                            if (val != -1) {
                                text = vn.toNormalizedString(val);
                                payload.put(attr.getString(JsonKeys.ATTR + 1), text);
                            }
                        } catch (JSONException e) {
                            val = vn.getText();
                            if (val != -1) {
                                text = vn.toNormalizedString(val);
                                payload.put(nextJsonObject.getString(JsonKeys.ELEMENT), text);
                            }
                        }
                    }
                }
                T instance = (T) typeParameterClass.newInstance().getInstance(payload);
                entryList.add(instance);
                ++count;
            }
            Log.v(TAG, "Total # of element " + count);
        } catch (ParseException e) {
            Log.e(TAG, e.getMessage());
        } catch (PilotException e) {
            Log.e(TAG, e.getMessage());
        } catch (NavException e) {
            Log.e(TAG, e.getMessage());
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        } catch (InstantiationException e) {
            Log.e(TAG, e.getMessage());
        } catch (IllegalAccessException e) {
            Log.e(TAG, e.getMessage());
        }
        return entryList;
    }
}
