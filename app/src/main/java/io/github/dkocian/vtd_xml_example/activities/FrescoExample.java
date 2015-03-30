package io.github.dkocian.vtd_xml_example.activities;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import io.github.dkocian.vtd_xml_example.R;
import io.github.dkocian.vtd_xml_example.utils.Urls;

/**
 * Created by dkocian on 3/30/2015.
 */
public class FrescoExample extends ActionBarActivity {
    @InjectView(R.id.my_image_view)
    SimpleDraweeView draweeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.fresco);
        ButterKnife.inject(this);
        draweeView.setImageURI(Uri.parse(Urls.HTTP_FRESCOLIB_ORG_STATIC_FRESCO_LOGO_PNG));
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
