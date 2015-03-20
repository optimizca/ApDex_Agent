package de.appdynamics.ace.apdex.util.api;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by stefan.marx on 22.12.14.
 */
public class AnalyticsResult {
    private boolean _error = false;
    private String _message;
    private int _cost;
    private int _proccessed;
    private int _shardsOK;
    private int _shardsError;

    public AnalyticsResult(JSONObject json) {
        try {

            if (json.has("statusCode") && json.getInt("statusCode") > 400) {
                setError(true);
                setMessage(json.getString("message"));
            } else {
                setCost(json.getInt("took"));
                setProccessed(json.getJSONObject("hits").getInt("total"));
                if (json.has("_shards")) {
                    setShardsOK(json.getJSONObject("_shards").getInt("successful"));
                    setShardsError(json.getJSONObject("_shards").getInt("failed"));
                }
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public AnalyticsResult() {


    }

    public void setError(boolean error) {
        _error = error;
    }

    public boolean isError() {
        return _error;
    }

    public void setMessage(String message) {
        _message = message;
    }

    public String getMessage() {
        return _message;
    }

    public void setCost(int cost) {
        _cost = cost;
    }

    public int getCost() {
        return _cost;
    }

    public void setProccessed(int proccessed) {
        _proccessed = proccessed;
    }

    public int getProccessed() {
        return _proccessed;
    }

    public void setShardsOK(int shardsOK) {
        _shardsOK = shardsOK;
    }

    public int getShardsOK() {
        return _shardsOK;
    }

    public void setShardsError(int shardsError) {
        _shardsError = shardsError;
    }

    public int getShardsError() {
        return _shardsError;
    }
}
