package de.appdynamics.ace.apdex.agent.api;

import de.appdynamics.ace.apdex.agent.api.dto.*;
import de.appdynamics.ace.apdex.util.api.*;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.Date;

/**
 * Created by stefan.marx on 16.03.15.
 */
public class API {

    public static Results runApdex(Config c, boolean runInitialy) {
        return runApdex(c,runInitialy,null);
    }
    public static Results runApdex(Config config, boolean runInitialy, APIProxy proxy) {
        ADEUMAnalyticsRequest adRequest = new ADEUMAnalyticsRequest(config.getAccountId(),
                config.getAccessToken(),config.getAnalyticsUrl());
        adRequest.setProxy(proxy);

        Results results = new Results();

        long start = System.currentTimeMillis();


        for (PageApdex pageConfig : config.getPages()) {
            for (AppdexExecution executionConfig : pageConfig.getExecutions()) {

                try {
                    Date d = executionConfig.getLastExecution();

                    Date now = new Date();
                    if ((d == null && runInitialy) ||
                         d != null && now.getTime() > executionConfig.getCronExpression().getNextValidTimeAfter(d).getTime()) {


                            executeAppdexRequestForPagenames(config, adRequest, results, pageConfig, executionConfig);

                            if (pageConfig.isIncludeAll()) {
                                executeAppdexAllPagesRequest(config, adRequest, results, pageConfig, executionConfig);

                            }


                            executionConfig.setLastExecution(now);
                    }
                } catch (Throwable t) {
                    t.printStackTrace();
                }

            }
        }
        results.setProcessingTime(System.currentTimeMillis() - start);

        return results;
    }

    private static void executeAppdexAllPagesRequest(Config config, ADEUMAnalyticsRequest adRequest, Results results, PageApdex pageConfig, AppdexExecution executionConfig) throws AppdexException {
        BucketBandsResult result = adRequest.queryAllPagesCombined(config.getAppkey(), executionConfig.getEndTime(), executionConfig.getStartTime(),
                pageConfig.getThresholdSlow(), pageConfig.getThresholdVerySlow());

        for (PageDataDO p : result.getPages()) {

            if (p.getProcessedPages() > executionConfig.getMinHits()) {
                double appdexValue = PageDataDO.calcAppdex(p);
                ResultLine line = new ResultLine(config.getAccountName(),p.getPagename(),p.getProcessedPages(),executionConfig.getName(),result.getCost(),appdexValue);
                results.addLine(line);
            }

        }

    }

    private static void executeAppdexRequestForPagenames(Config c, ADEUMAnalyticsRequest adRequest, Results r, PageApdex pageConfig, AppdexExecution execution) throws AppdexException {
        BucketBandsResult result = adRequest.queryPagesWithinApp(pageConfig.getPagenames(),c.getAppkey(), execution.getEndTime(), execution.getStartTime(),
                pageConfig.getThresholdSlow(), pageConfig.getThresholdVerySlow());

        for (PageDataDO p : result.getPages()) {

            if (p.getProcessedPages() > execution.getMinHits()) {
                double appdexValue = PageDataDO.calcAppdex(p);
                ResultLine line = new ResultLine(c.getAccountName(),p.getPagename(),p.getProcessedPages(),execution.getName(),result.getCost(),appdexValue);
                r.addLine(line);
            }

        }
    }


    public static Config[] loadConfig(File config) throws IOException {

        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(config,new Config[]{}.getClass());



    }
}
