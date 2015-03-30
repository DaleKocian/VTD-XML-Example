package io.github.dkocian.vtd_xml_example;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by dkocian on 3/30/2015.
 */
public class MainActivity extends ActionBarActivity {
    @InjectView(R.id.btnLaunchVtd)
    Button btnLaunchVtd;
    @InjectView(R.id.btnLaunchPullParser)
    Button btnLaunchPullParser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_ui);
        ButterKnife.inject(this);
    }

    @OnClick(R.id.btnLaunchVtd)
    public void onBtnLaunchVtd(View view) {
        Intent vtdXmlActivity = new Intent(this, VtdXmlActivity.class);
        startActivity(vtdXmlActivity);
    }

    @OnClick(R.id.btnLaunchPullParser)
    public void onBtnLaunchPullParser(View view) {
        Intent networkActivity = new Intent(this, NetworkActivity.class);
        startActivity(networkActivity);
    }
}
