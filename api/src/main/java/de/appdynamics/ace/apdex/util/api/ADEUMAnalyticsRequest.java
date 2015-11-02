package de.appdynamics.ace.apdex.util.api;

import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.*;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import sun.misc.BASE64Encoder;

import java.io.*;
import java.net.URI;

import java.util.Map;

/**
 * Created by stefan.marx on 18.12.14.
 */
public class ADEUMAnalyticsRequest {
    private APIProxy _proxy;

    private static Logger logger = Logger.getLogger(ADEUMAnalyticsRequest.class.getName());


    public String getUrl() {
        return _url;
    }

    private final String _url;

    public String getAccount() {
        return _account;
    }

    public String getAccess() {
        return _access;
    }

    private final String _account;
    private final String _access;

    public ADEUMAnalyticsRequest(String account, String access, String url) {
        _account = account;
        _access = access;
        _url = url;
    }

    public BucketBandsResult queryAllPages(String end, String start,long lowerBand, long upperBand) {

        HttpPost post = new HttpPost(getUrl());

        BucketBandsResult br = null;
        String query = QueryConstants.ALL_PAGE_BANDS;

        query = applyValue(query,"FROM","\""+start+"\"");
        query = applyValue(query,"TO","\""+end+"\"");
        query = applyValue(query,"B1",""+lowerBand);
        query = applyValue(query,"B2",""+upperBand);

        System.out.println(query);


        try {
            JSONObject json = executePostQuery(post, query);
            br = new BucketBandsResult(json);


        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return br;
    }

    public BucketBandsResult queryOnePages(String page, String end, String start,long lowerBand, long upperBand) {

        HttpPost post = new HttpPost(getUrl());

        BucketBandsResult br = null;
        String query = QueryConstants.SINGLE_PAGE_BANDS;

        query = applyValue(query,"FROM","\""+start+"\"");
        query = applyValue(query,"TO","\""+end+"\"");
        query = applyValue(query,"B1",""+lowerBand);
        query = applyValue(query,"B2",""+upperBand);
        query = applyValue(query,"PAGE",page);




        try {
            JSONObject json = executePostQuery(post, query);
            br = new BucketBandsResult(json);


        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            logger.error("Error Reading response :",e);
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("Error Reading response :",e);

        } catch (JSONException e) {
            e.printStackTrace();
            logger.error("Error Unmarshaling response :",e);
        }


        return br;
    }

    private JSONObject executePostQuery(HttpPost post, String query) throws IOException, JSONException {
        post.setHeader("Authorization", getAuthToken());
        post.setHeader("Content-Type", "application/json");
        post.setHeader("Accept","application/json");

        post.setEntity(new StringEntity(query));

        InputStream is = executeHttpCall(post);

        // Debug Query
        logger.debug("Query :\n"+post.toString()+"\n"+"---"+"\n"+query+"\n---");


        BufferedReader streamReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));

        StringBuilder responseStrBuilder = new StringBuilder();

        String inputStr;
        while ((inputStr = streamReader.readLine()) != null)
            responseStrBuilder.append(inputStr);

        String payload = responseStrBuilder.toString();

        if (logger.isTraceEnabled()) {
            String s;
            if (payload.length() > 1000) {
                s = payload.substring(0,1000);
            } else {
                s = payload;
            }
            logger.trace("Result :\n"+s+"\n-------");
        }

        return new JSONObject(payload);
    }

    private String getAuthToken() {
        String x =  getAccount()+":"+getAccess();
        String token = new BASE64Encoder().encode(x.getBytes());
        token = "Basic "+token;
        return token.replaceAll("\n","");
    }

    private String applyValue(String query, String key, String value) {
        //TODO :
        return query.replaceAll("::"+key+"::",value);
    }

    private java.io.InputStream executeHttpCall( HttpRequestBase method) throws IOException {

        if (logger.isTraceEnabled()) {
            logger.trace("Execute CALL :"+method.toString());
        }
        CloseableHttpClient httpclient = HttpClientBuilder.create().build();

        if (getProxy() != null) {
            String scheme = method.getURI().getScheme();

            HttpHost proxy = new HttpHost(getProxy().getHost(), getProxy().getPort(), "http");

            RequestConfig config = RequestConfig.custom()
                    .setProxy(proxy)
                    .build();

            method.setConfig(config);
        }



        CloseableHttpResponse result = httpclient.execute(method);

        if (logger.isTraceEnabled()) {
            logger.trace("CALL Result :"+result.getStatusLine());
            StringBuffer b = new StringBuffer();
            for (Header s :result.getAllHeaders()) {
                 b.append(s.getName()+":"+s.getValue()).append("\n");
            }
            logger.trace("Headers :\n"+b.toString());

        }
        if (result.getStatusLine().getStatusCode()>400) {
            logger.error("HTTP Error : "+result.getStatusLine());
            InputStream is = result.getEntity().getContent();
            BufferedReader streamReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));

            StringBuilder responseStrBuilder = new StringBuilder();

            String inputStr;
            while ((inputStr = streamReader.readLine()) != null)
                responseStrBuilder.append(inputStr);

            String payload = responseStrBuilder.toString();
            logger.error("Payload :"+payload);

            throw new IOException("Http Error :"+result.getStatusLine());

        }
        return result.getEntity().getContent();
    }


    public BucketBandsResult queryOnePagesWithinApp(String page,String appKey, String end, String start,long lowerBand, long upperBand) {
        HttpPost post = new HttpPost(getUrl());

        BucketBandsResult br = null;
        String query = QueryConstants.SINGLE_PAGE_BANDS_WITH_APP;

        query = applyValue(query,"FROM","\""+start+"\"");
        query = applyValue(query,"TO","\""+end+"\"");
        query = applyValue(query,"B1",""+lowerBand);
        query = applyValue(query,"B2",""+upperBand);
        query = applyValue(query,"PAGE",page);
        query = applyValue(query,"APP",appKey);




        try {
            JSONObject json = executePostQuery(post, query);
            br = new BucketBandsResult(json);


        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return br;
    }


    public BucketBandsResult queryPagesWithinApp(String[] pagenames, String appKey, String end, String start, long lowerBand, long upperBand, String metricField) {
        if (pagenames == null || pagenames.length == 0) return new BucketBandsResult();

        HttpPost post = new HttpPost(getUrl());

        BucketBandsResult br = new BucketBandsResult();
        String query = QueryConstants.MULTIPLE_PAGE_BANDS_WITH_APP;

        query = applyValue(query,"FROM","\""+start+"\"");
        query = applyValue(query,"TO","\""+end+"\"");
        query = applyValue(query,"B1",""+lowerBand);
        query = applyValue(query,"B2",""+upperBand);
        query = applyValue(query,"PAGE",renderArray(pagenames));
        query = applyValue(query,"APP",appKey);
        query = applyValue(query,"METRICFIELD",metricField);




        try {
            JSONObject json = executePostQuery(post, query);
            br = new BucketBandsResult(json);


        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            br.setError(true);
            br.setErrorMessage(e.toString());

        } catch (IOException e) {
            e.printStackTrace();
            br.setError(true);
            br.setErrorMessage(e.toString());
        } catch (JSONException e) {
            e.printStackTrace();
            br.setError(true);
            br.setErrorMessage(e.toString());
        }


        return br;
    }

    private String renderArray(String[] pagenames) {
        String page = "[";
        int i = 0;
        for (String p:pagenames) {
            if (i++ > 0) page += ",";
            page+= '"'+p+'"';


        }
        page +="]";
        return page;
    }

    public BucketBandsResult executeRequest(String query, Map<String, String> param) {
        HttpPost post = new HttpPost(getUrl());

        BucketBandsResult br = null;
        for(Map.Entry<String,String> e : param.entrySet()) {
             query = applyValue(query,e.getKey(),e.getValue()) ;
        }
        try {
            JSONObject json = executePostQuery(post, query);
            br = new BucketBandsResult(json);


        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return br;
    }

    public void setProxy(APIProxy proxy) {
        _proxy = proxy;
    }

    public APIProxy getProxy() {
        return _proxy;
    }

    public BucketBandsResult queryAllPagesCombined(String appkey, String endTime, String startTime, long thresholdSlow, long thresholdVerySlow, String metricField) {
        HttpPost post = new HttpPost(getUrl());

        BucketBandsResult br = new BucketBandsResult();
        String query = QueryConstants.ALL_PAGE_BANDS_COMBINED;

        query = applyValue(query,"FROM","\""+startTime+"\"");
        query = applyValue(query,"TO","\""+endTime+"\"");
        query = applyValue(query,"B1",""+thresholdSlow);
        query = applyValue(query,"B2",""+thresholdVerySlow);
        query = applyValue(query,"METRICFIELD",metricField);




        try {
            JSONObject json = executePostQuery(post, query);
            br = new BucketBandsResult(json);


        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            br.setError(true);
            br.setErrorMessage(e.toString());

        } catch (IOException e) {
            e.printStackTrace();
            br.setError(true);
            br.setErrorMessage(e.toString());
        } catch (JSONException e) {
            e.printStackTrace();
            br.setError(true);
            br.setErrorMessage(e.toString());
        }

        return br;
    }
}
