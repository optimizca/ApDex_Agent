package de.appdynamics.ace.apdex.util.api;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by stefan.marx on 22.12.14.
 */
public class BucketBandsResult extends BucketResult {
    private ArrayList<PageDataDO> _pages = new ArrayList<PageDataDO>();

    public BucketBandsResult(JSONObject json) {
        super(json);

        // Check Structure

        fillData(json);

    }

    public BucketBandsResult() {
        super();


    }

    private void fillData(JSONObject json) {
        try {
            if (json.has("aggregations") &&
                    json.getJSONObject("aggregations").has("page")) {
                JSONArray pagesData = json.getJSONObject("aggregations")
                        .getJSONObject("page")
                        .getJSONArray("buckets");

                for (int i = 0; i < pagesData.length(); i++) {
                    JSONObject pd = pagesData.getJSONObject(i);

                    PageDataDO pdo = new PageDataDO(pd.getString("key"));
                    pdo.setprocessedPages(pd.getInt("doc_count"));

                    fillBandsData(pdo, pd.getJSONObject("bands"));

                    addPage(pdo);
                }


            }
            if (json.has("aggregations") &&
                    json.getJSONObject("aggregations").has("bands")) {

                JSONObject bandsData = json.getJSONObject("aggregations")
                        .getJSONObject("bands");

                PageDataDO pdo = new PageDataDO("APPLICATION");
                fillBandsData(pdo, bandsData);
                int pages = 0;
                for (BandDO b : pdo.getBands()) {

                    pages += b.getPageCount();
                }
                pdo.setProcessedPages(pages);
                addPage(pdo);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void addPage(PageDataDO pdo) {
        _pages.add(pdo);

    }

    private void fillBandsData(PageDataDO pdo, JSONObject band) {
        try {
            if (band.has("buckets")) {
                JSONArray buckets = null;

                buckets = band.getJSONArray("buckets");

                for (int j = 0; j < buckets.length(); j++) {
                    JSONObject bucket = buckets.getJSONObject(j);

                    BandDO dataBand = new BandDO(bucket.getString("key"));

                    if (bucket.has("from")) dataBand.setLower(bucket.getLong("from"));
                    if (bucket.has("to")) dataBand.setUpper(bucket.getLong("to"));
                    dataBand.setPageCount(bucket.getLong("doc_count"));

                    pdo.addBandData(dataBand);


                }


            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public java.util.List<PageDataDO> getPages() {
        return Collections.unmodifiableList(_pages);


    }


}
