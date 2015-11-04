package de.appdynamics.ace.apdex.agent.api;

import de.appdynamics.ace.apdex.agent.api.dto.*;
import de.appdynamics.ace.apdex.util.api.*;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.Date;


/**
 * Created by stefan.marx on 16.03.15.
 */
public class API {

    private static Logger logger = Logger.getLogger("de.appdynamics.ace.apdex.agent.api");

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


                        boolean failed = executeAppdexRequestForPagenames(config, adRequest, results, pageConfig, executionConfig).isError();

                            if (pageConfig.isIncludeAll()) {
                                failed = failed || executeAppdexAllPagesRequest(config, adRequest, results, pageConfig, executionConfig).isError();

                            }



                        // if executed once before make sure to calculate possible Delays
                        if (d != null) {
                            long proposedtTime = executionConfig.getCronExpression().getNextValidTimeAfter(d).getTime();
                            long delay = now.getTime()-proposedtTime;
                            if (delay/(1000*60) > 2) {
                                logger.warn("Execution was, delayed for " + (delay/(1000*60)) +" minutes");
                            }
                        }



                           // only reset timer if non of the request have failed
                           if (!failed) executionConfig.setLastExecution(now);
                           else {
                               logger.error("Error retriving Values, retry next execution!y");
                           }



                    }
                } catch (Throwable t) {
                    logger.error("Error while run Apdex Query ",t);
                }

            }
        }
        results.setProcessingTime(System.currentTimeMillis() - start);

        return results;
    }

    private static BucketBandsResult executeAppdexAllPagesRequest(Config config, ADEUMAnalyticsRequest adRequest, Results results, PageApdex pageConfig, AppdexExecution executionConfig) throws AppdexException {


        BucketBandsResult result = adRequest.queryAllPagesCombined(config.getAppkey(), executionConfig.getEndTime(), executionConfig.getStartTime(),
                pageConfig.getThresholdSlow(), pageConfig.getThresholdVerySlow(),executionConfig.getMetricField());

        for (PageDataDO p : result.getPages()) {

            if (p.getProcessedPages() > executionConfig.getMinHits()) {
                double appdexValue = PageDataDO.calcAppdex(p);
                ResultLine line = new ResultLine(config.getAccountName(),p.getPagename(),p.getProcessedPages(),executionConfig.getName(),result.getCost(),appdexValue);
                results.addLine(line);
            }

        }
        logger.debug("--------------------------\n"
                +"AllPages Collection :"+config.getAccountName()+"\n"
                +"Execution :"+executionConfig.getName()+"\n"
                +"Processed PageCount :"+result.getProccessed() +"\n"
                +"Shards OK :"+result.getShardsOK()+"\n"
                +"Shards Error :"+result.getShardsError()+"\n"
                +"Cost :"+result.getCost());


        return result;
    }

    private static BucketBandsResult executeAppdexRequestForPagenames(Config c, ADEUMAnalyticsRequest adRequest, Results r, PageApdex pageConfig, AppdexExecution execution) throws AppdexException {
        logger.debug("Run Pages Query : "+dumpPages(pageConfig.getPagenames()));

        BucketBandsResult result = adRequest.queryPagesWithinApp(pageConfig.getPagenames(),c.getAppkey(), execution.getEndTime(), execution.getStartTime(),
                pageConfig.getThresholdSlow(), pageConfig.getThresholdVerySlow(),execution.getMetricField());

        for (PageDataDO p : result.getPages()) {

            if (p.getProcessedPages() > execution.getMinHits()) {
                double appdexValue = PageDataDO.calcAppdex(p);
                ResultLine line = new ResultLine(c.getAccountName(),p.getPagename(),p.getProcessedPages(),execution.getName(),result.getCost(),appdexValue);
                r.addLine(line);
            }

        }

        logger.debug("--------------------------\n"
                +"Collection :"+c.getAccountName()+"\n"
                +"Execution :"+execution.getName()+"\n"
                +"Processed PageCount :"+result.getProccessed() +"\n"
                +"Shards OK :"+result.getShardsOK()+"\n"
                +"Shards Error :"+result.getShardsError()+"\n"
                +"Cost :"+result.getCost());


        return result;

    }

    private static String dumpPages(String[] pagenames) {
        StringBuffer b = new StringBuffer("");
        for (String p : pagenames) b.append(p).append("\n");
        return b.toString();
    }


    public static Config[] loadConfig(File config) throws IOException {

        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(config,new Config[]{}.getClass());



    }
}
