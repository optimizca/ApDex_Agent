package de.appdynamics.ace.apdex.agent.api.dto;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.quartz.CronExpression;

import java.text.ParseException;
import java.util.Date;

/**
 * Created by stefan.marx on 16.03.15.
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class AppdexExecution {

    public final static String CRON_DAILY = "0 0 0 ? * *";
    public final static String CRON_WEEKLY = "0 0 0 ? * SUN";
    public final static String CRON_MONTHLY = "0 0 0 L * ?";
    public final static String CRON_HOURLY = "0 0 * ? * *";
    public final static String CRON_EVERY5Min = "0 */5 * ? * *";
    public final static String CRON_EVERY15Min = "0 */15 * ? * *";
    private Date _lastExecution;

    public void setMinHits(long minHits) {
        this.minHits = minHits;
    }

    public AppdexExecution(String name, String cron, String startTime, String endTime) {
        this.name = name;
        this.cron = cron;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getName() {

        return name;
    }

    @JsonProperty
    String name,cron;

    @JsonProperty
    String startTime="now-5m",endTime="now";

    @JsonProperty
    long minHits = 0;

    public AppdexExecution() {
    }

    public String getCron() {

        return cron;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public long getMinHits() {
        return minHits;
    }

    @JsonIgnore
    public Date getLastExecution() {
        return _lastExecution;
    }

    @JsonIgnore
    public CronExpression getCronExpression() throws ParseException {
        return new CronExpression(cron);
    }

    public void setLastExecution(Date lastExecution) {
        _lastExecution = lastExecution;
    }
}
