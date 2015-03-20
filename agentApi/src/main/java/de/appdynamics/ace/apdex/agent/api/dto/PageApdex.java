package de.appdynamics.ace.apdex.agent.api.dto;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by stefan.marx on 16.03.15.
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class PageApdex {


    public PageApdex() {
    }

    public PageApdex(String[] pages, long thresholdSlow, long thresholdVerySlow) {
        this.pagenames = pages;
        this.thresholdSlow = thresholdSlow;
        this.thresholdVerySlow = thresholdVerySlow;
    }

    public PageApdex(String page, long thresholdSlow, long thresholdVerySlow) {
        this.pagenames = new String []{page};
        this.thresholdSlow = thresholdSlow;
        this.thresholdVerySlow = thresholdVerySlow;
    }

    @JsonProperty
    String[] pagenames;

    @JsonProperty
    long thresholdSlow, thresholdVerySlow;

    @JsonProperty
    List<AppdexExecution> executions = new ArrayList<AppdexExecution>();

    @JsonProperty
    long cutFastPages,cutSlowPages;

    @JsonProperty
    private boolean includeAll;

    public long getCutFastPages() {
        return cutFastPages;
    }

    public void setCutFastPages(long cutFastPages) {
        this.cutFastPages = cutFastPages;
    }

    public long getCutSlowPages() {
        return cutSlowPages;
    }

    public void setCutSlowPages(long cutSlowPages) {
        this.cutSlowPages = cutSlowPages;
    }

    public String[] getPagenames() {
        return pagenames;
    }

    public long getThresholdSlow() {
        return thresholdSlow;
    }

    public long getThresholdVerySlow() {
        return thresholdVerySlow;
    }

    public List<AppdexExecution> getExecutions() {
        return executions;
    }

    public PageApdex addExecution(AppdexExecution execution) {
        executions.add(execution);

        return this;
    }

    public boolean isIncludeAll() {
        return includeAll;
    }

    public void setIncludeAll(boolean includeAll) {
        this.includeAll = includeAll;
    }
}
