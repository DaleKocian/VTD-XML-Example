package io.github.dkocian.vtd_xml_example;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ximpleware.AutoPilot;
import com.ximpleware.NavException;
import com.ximpleware.ParseException;
import com.ximpleware.PilotException;
import com.ximpleware.VTDGen;
import com.ximpleware.VTDNav;
import com.ximpleware.XPathEvalException;
import com.ximpleware.XPathParseException;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MainActivity extends ActionBarActivity {
    private static final String TAG = MainActivity.class.getName();
    @InjectView(R.id.tvContent)
    TextView tvContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://stackoverflow.com/feeds/tag?tagnames=android&sort=newest";
//        String url = "http://vtd-xml.sourceforge.net/codeSample/servers.xml";
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        tvContent.setText("Response is: " + response);
                        VTDGen vg = new VTDGen();
                        vg.setDoc(response.getBytes());
                        try {
                            vg.parse(true);
                            VTDNav vn = vg.getNav();
                            AutoPilot ap = new AutoPilot(vn);
                            ap.selectXPath("/feed/entry");
                            int count = 0;
                            while (ap.evalXPath() != -1) {
                                Log.v(TAG, "" + vn.getCurrentIndex() + " ");
                                Log.v(TAG, "Element name ==> " + vn.toString(vn.getCurrentIndex()));
                                int t = vn.getText(); // get the index of the text (char data or CDATA)
                                if (t != -1) {
                                    Log.v(TAG, " Text ==> " + vn.toNormalizedString(t));
                                    System.out.println();
                                }
                                Log.v(TAG, "\n ============================== ");
                                count++;
                                if (vn.toElement(VTDNav.FIRST_CHILD, "title")) {
                                    do {
                                        t = vn.getText();
                                        if (t != -1) {
                                            String id = vn.toNormalizedString(t);
                                            Log.v(TAG, "\ttitle:" + id);
                                        }
                                    } while (vn.toElement(VTDNav.NEXT_SIBLING, "title"));
                                }
                            }
                            Log.v(TAG, "Total # of element " + count);
                        } catch (ParseException e) {
                            Log.e(TAG, e.getMessage());
                        } catch (PilotException e) {
                            Log.e(TAG, e.getMessage());
                        } catch (NavException e) {
                            Log.e(TAG, e.getMessage());
                        } catch (XPathParseException e) {
                            Log.e(TAG, e.getMessage());
                        } catch (XPathEvalException e) {
                            Log.e(TAG, e.getMessage());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                tvContent.setText("That didn't work!");
            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
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
}
