package io.github.dkocian.vtd_xml_example.model;

import org.json.JSONObject;

/**
 * Created by dkocian on 3/30/2015.
 */
public abstract class XmlModel<T> {
    public abstract T getInstance(JSONObject jsonObject);
}
