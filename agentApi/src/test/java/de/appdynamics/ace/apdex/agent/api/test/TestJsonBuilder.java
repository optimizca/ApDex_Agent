package de.appdynamics.ace.apdex.agent.api.test;

import de.appdynamics.ace.apdex.agent.api.dto.AppdexExecution;
import de.appdynamics.ace.apdex.agent.api.dto.Config;
import de.appdynamics.ace.apdex.agent.api.dto.PageApdex;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by stefan.marx on 17.03.15.
 */
public class TestJsonBuilder {

    @Test
    public void testBuildConfig() {
        String[] pages;
        pages = new String[]{"logon.login.jsp",
                "a/logon/entermemorableinformation.jsp",
                "a/account_overview_personal",
                "a/account details",
                "a/make_payment",
                "a/make_transfer" };

        ArrayList<Config> cfgs = new ArrayList<Config>();

        //Lloyds Bank Retail, Halifax Retail and Band Of Scotland Retail applications: -
        cfgs.add(constructConfig(pages,"lloyds retail","AD-AAB-AAA-MXY"));
        cfgs.add(constructConfig(pages,"Halifax Retail","AD-AAB-AAA-MYH"));
        cfgs.add(constructConfig(pages,"Scotland Retail","AD-AAB-AAA-MXX"));

        ObjectMapper m = new ObjectMapper();
        ObjectWriter w = m.writerWithDefaultPrettyPrinter();

        try {
            System.out.println(w.writeValueAsString(cfgs));
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private Config constructConfig(String[] pages, String name,String appKey) {
        Config c = new Config(name,"LLOYDSBANKINGGROUPPLC-a0Q8000000BWuotEAD",
                "e25c8683-39ce-45ca-a566-b913747c48e1",appKey);

        PageApdex ap = new PageApdex(pages, 2000, 8000);

        AppdexExecution ex = null;

        ex = new AppdexExecution("5min",AppdexExecution.CRON_EVERY5Min,"now-5m","now");
        ex.setMinHits(100);
        ap.addExecution(ex);


        ap.addExecution(ex = new AppdexExecution("hourly",AppdexExecution.CRON_HOURLY,"now-1h","now"));
        ex.setMinHits(100);
        ap.addExecution(ex = new AppdexExecution("daily", AppdexExecution.CRON_HOURLY, "now-1d", "now"));
        ex.setMinHits(100);
        ap.addExecution(ex = new AppdexExecution("weekly", AppdexExecution.CRON_HOURLY, "now-1w", "now"));
        ex.setMinHits(100);
        c.addPage(ap);
        return c;

    }

}
