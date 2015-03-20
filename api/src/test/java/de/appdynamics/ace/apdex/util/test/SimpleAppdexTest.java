package de.appdynamics.ace.apdex.util.test;

import de.appdynamics.ace.apdex.util.api.*;
import junit.framework.TestCase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by stefan.marx on 18.12.14.
 */
public class SimpleAppdexTest extends TestCase {

    public void testAppdexRequest() {
        ADEUMAnalyticsRequest adRequest = new ADEUMAnalyticsRequest("LLOYDSBANKINGGROUPPLC-a0Q8000000BWuotEAD",
                "e25c8683-39ce-45ca-a566-b913747c48e1",
                "https://analytics.api.appdynamics.com/v1/events/browserrecord/search");

        long span = (1000*60)*60*24*7*52;
        BucketBandsResult result = adRequest.queryOnePagesWithinApp("a/account_details","AD-AAB-AAA-MXW","now/d", "now-1w",2500,6000);


        dumpResults(result);


    }


    public void testGenericRequest() {
        ADEUMAnalyticsRequest adRequest = new ADEUMAnalyticsRequest("LLOYDSBANKINGGROUPPLC-a0Q8000000BWuotEAD",
                "e25c8683-39ce-45ca-a566-b913747c48e1",
                "https://analytics.api.appdynamics.com/v1/events/browserrecord/search");

        long span = (1000*60)*60*24*7*52;


        Map<String,String> param = new HashMap<String, String>();


        param.put("FROM","\"now-1w\"");
        param.put("TO","\"now\"");
        param.put("B1","2000");
        param.put("B2","8000");

        BucketBandsResult result = adRequest.executeRequest(QueryConstants.ALL_PAGE_BANDS,param);


        dumpResults(result);


    }

    private void dumpResults(BucketBandsResult result) {
        int c = result.getCost();
        int l = result.getProccessed();

        System.out.println("P: "+c+"  C:"+l);


        for (PageDataDO p : result.getPages()) {
            System.out.println("Appdex for "+p.getPagename()+" based on "+p.getProcessedPages()+" page hits. (processed in "+result.getCost()+" ms)");

            List<BandDO> bands = p.getBands();
            if (bands.size()!=3) {
                System.out.println("Wrong Bands Size ::"+bands.size());
            }   else  {
                System.out.println(" Appdex: "+(bands.get(0).getPageCount()+(0.5*bands.get(1).getPageCount()))/p.getProcessedPages()   );
            }
        }
    }

    public void testAllAppdexRequest() {
        ADEUMAnalyticsRequest adRequest = new ADEUMAnalyticsRequest("LLOYDSBANKINGGROUPPLC-a0Q8000000BWuotEAD",
                "e25c8683-39ce-45ca-a566-b913747c48e1",
                "https://analytics.api.appdynamics.com/v1/events/browserrecord/search");

        long span = (1000*60)*60*24*7*52;
        BucketBandsResult result = adRequest.queryAllPages("now/d", "now-1w",2500,6000);


        dumpResults(result);


    }
}
