package de.appdynamics.ace.apdex.agent.api.dto;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by stefan.marx on 16.03.15.
 */

@JsonIgnoreProperties(ignoreUnknown=true)
public class Config {
    public Config() {
    }

    public void setAppkey(String appkey) {
        this.appkey = appkey;
    }

    public String getAppkey() {

        return appkey;
    }

    public Config(String accountName, String accountId, String accessToken, String appKey) {
        this.accountName = accountName;
        this.accountId = accountId;
        this.accessToken = accessToken;
        this.appkey = appKey;
    }

    @JsonProperty
    String accountName,appkey;

    @JsonProperty
    String analyticsUrl = "https://analytics.api.appdynamics.com/v1/events/browserrecord/search", accountId, accessToken;

    @JsonProperty
    List<PageApdex> pages = new ArrayList<PageApdex>();

    public String getAccountName() {
        return accountName;
    }

    public String getAnalyticsUrl() {
        return analyticsUrl;
    }

    public String getAccountId() {
        return accountId;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public List<PageApdex> getPages() {
        return pages;
    }

    public Config addPage(PageApdex page) {
        pages.add(page);
        return this;
    }

    public String toJsonString() throws IOException {
        ObjectMapper m = new ObjectMapper();
        ObjectWriter w = m.writerWithDefaultPrettyPrinter();

        return w.writeValueAsString(this);

    }
}
