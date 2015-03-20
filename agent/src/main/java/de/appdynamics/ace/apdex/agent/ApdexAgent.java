package de.appdynamics.ace.apdex.agent;


import com.singularity.ee.agent.commonservices.metricgeneration.metrics.MetricAggregatorType;
import com.singularity.ee.agent.resolver.AgentAccountInfo;
import com.singularity.ee.agent.systemagent.Agent;
import com.singularity.ee.agent.systemagent.SystemAgentLogAppender;
import com.singularity.ee.agent.systemagent.api.AManagedMonitor;
import com.singularity.ee.agent.systemagent.api.MetricWriter;
import com.singularity.ee.agent.systemagent.api.TaskExecutionContext;
import com.singularity.ee.agent.systemagent.api.TaskOutput;
import com.singularity.ee.agent.systemagent.api.exception.TaskExecutionException;
import com.singularity.ee.controller.api.constants.MetricClusterRollupType;
import com.singularity.ee.controller.api.constants.MetricTimeRollupType;
import com.singularity.ee.controller.api.dto.MachineAgent;
import de.appdynamics.ace.apdex.agent.api.API;
import de.appdynamics.ace.apdex.agent.api.dto.Config;
import de.appdynamics.ace.apdex.agent.api.dto.ResultLine;
import de.appdynamics.ace.apdex.agent.api.dto.Results;
import org.apache.log4j.*;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Created by stefan.marx on 22.12.14.
 */
public class ApdexAgent extends AManagedMonitor {

    private static final String METRIC_SEP = "|";
    private static Logger logger = Logger.getLogger("com.appdynamics.custom.agent.appdex");

    //Application Infrastructure Performance|APPDEX|Custom Metrics|MySimpleAgent
    private final static String metricPrefix = "Custom Metrics|Apdex";
    private final Date _date;

    private HashMap<String,Long> _values = new HashMap<String, Long>();

    private Config[] _cfg = null;

    public ApdexAgent() {



        logger.log(Level.INFO," LOADED !!!!");

        _date = new Date();

    }



    public TaskOutput execute(Map<String, String> stringStringMap, TaskExecutionContext taskExecutionContext) throws TaskExecutionException {
        if (_cfg == null ) {
            try {
                _cfg = loadConfig(new File(taskExecutionContext.getTaskDir()));
            } catch (IOException e) {
                logger.error("Error while loading Config File !!!",e);
                _cfg = null;
            }
        }

        for (Config c : _cfg) {
            reportApdex(c);
        }

        reportMetrics();


//        getMetricWriter(metricPrefix+"TESTER").printMetric("897");
//        logger.log(Level.INFO,"Submitted");
//        System.out.println("DONE!!!"+_date);
        return new TaskOutput("Metric Upload Complete ! ");

    }

    private void reportMetrics() {
        for (String mp : _values.keySet()) {
            MetricWriter mw = getApdexMetricWriter(mp);
            mw.printMetric(""+_values.get(mp));
            if (logger.isDebugEnabled())
                logger.debug("Metric Reported "+mp+" --> "+_values.get(mp));
        }

    }

    private void reportApdex(Config c) {
        logger.debug ("starting Apdex Request "+c.getAccountId());
        Results result = API.runApdex(c, true);
        for (ResultLine line : result.getLines()) {

// Work arround for bypassing Observation Aggregator Bug

//            MetricWriter mw = getApdexMetricWriter(line.getAccountName(), line.getPagename(), line.getName());
//            mw.printMetric(""+(int)(line.getAppdexValue()*100));

            storeMetricValue (line.getAccountName(), line.getPagename(), line.getName(),
                    (long) (line.getAppdexValue()*100));



        }
        logger.debug ("ApdexRequest done \n " +
                        "total Pages :"+result.getTotalPages()+"\n"+
                        "Cost : "+result.getProcessingTime()/1000   );

    }

    private void storeMetricValue(String accountName, String pagename, String name, long v) {
        String mp = getMetricPath(accountName,pagename,name);
        _values.put(mp,v);

    }

    private String getMetricPath(String accountName, String pagename, String name) {
        return metricPrefix+METRIC_SEP+accountName+METRIC_SEP+pagename+METRIC_SEP+name;
    }

    private MetricWriter getApdexMetricWriter(String path) {
        return getMetricWriter(path,
                MetricAggregatorType.OBSERVATION.toString(),
                MetricTimeRollupType.AVERAGE.toString(),
                MetricClusterRollupType.COLLECTIVE.toString());



    }

    private Config[] loadConfig(File taskDir) throws IOException {

        return API.loadConfig(new File(taskDir,"config.json"));
    }


}