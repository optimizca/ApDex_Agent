package de.appdynamics.ace.apdex.agent.api.test;

import de.appdynamics.ace.apdex.agent.api.API;
import de.appdynamics.ace.apdex.agent.api.dto.*;
import de.appdynamics.ace.apdex.util.api.ADEUMAnalyticsRequest;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;
import org.junit.Test;
import org.quartz.CronExpression;

import java.io.IOException;

/**
 * Created by stefan.marx on 16.03.15.
 */

public class TestJsonLoad {

    public Config createJsonConfig() throws IOException {
        Config c = new Config("lloyds","LLOYDSBANKINGGROUPPLC-a0Q8000000BWuotEAD","e25c8683-39ce-45ca-a566-b913747c48e1","AD-AAB-AAA-MXW");

        String [] apages = {"a/account_details",
                "a/account_overview_personal",
                "logon/login.jsp",
                "a/logon/entermemorableinformation.jsp",
                "a/mobile/account_details",
                "a/mobile/account_overview",
                "a/useradmin/mobile/logon/entermemorableinformation.jsp",
                "a/make_payment"};

            PageApdex ap = new PageApdex(apages, 3500, 6500);

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


        Config[] configs = new Config[]{c};

        return c;

    }

    @Test
    public void testAPI() throws IOException {
        Config c = createJsonConfig();
        API.runApdex(c,true);

        System.out.println("--");
        //API.runApdex(c, true);

        System.out.println(toJsonString(new Config[]{c}));


    }

    private String toJsonString(Config[] configs) throws IOException {

        ObjectMapper m = new ObjectMapper();
        ObjectWriter w = m.writerWithDefaultPrettyPrinter();

        return w.writeValueAsString(configs);

    }


    @Test
    public void testPages() throws IOException {
        Config c = createJsonConfigCombined();
        Results r = API.runApdex(c, true);

        System.out.println("--");
        //API.runApdex(c, true);

        for (ResultLine l : r.getLines()) {
            System.out.println(l.getPagename()+"  : "+l.getAppdexValue());
        }
        System.out.println(toJsonString(new Config[]{c}));

    }

    public Config createJsonConfigCombined() throws IOException {
        Config c = new Config("lloyds","LLOYDSBANKINGGROUPPLC-a0Q8000000BWuotEAD","e25c8683-39ce-45ca-a566-b913747c48e1","AD-AAB-AAA-MXW");

        String [] apages = {"a/account_details",
                "a/account_overview_personal",
                "logon/login.jsp",
                "a/logon/entermemorableinformation.jsp",
                "a/mobile/account_details",
                "a/mobile/account_overview",
                "a/useradmin/mobile/logon/entermemorableinformation.jsp",
                "a/make_payment"};

        PageApdex ap = new PageApdex(new String []{}, 3500, 6500);
        ap.setIncludeAll(true);

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


        Config[] configs = new Config[]{c};

        return c;

    }
}
