package de.appdynamics.ace.apdex.agent.api.dto;

/**
 * Created by stefan.marx on 16.03.15.
 */
public class ResultLine {

    private final String _pagename;
    private final int _processedPages;
    private final String _name;
    private final int _cost;
    private final double _appdexValue;
    private final String _accountName;

    public static final String [] HEADER_FIELDS = {
           "accountName", "pagename" ,"processedPages",
            "name","cost","appdexValue" };

    public ResultLine(String accountName, String pagename, int processedPages, String name, int cost, double appdexValue) {
        _accountName = accountName;
        _pagename = pagename;
        _processedPages = processedPages;
        _name = name;
        _cost = cost;
        _appdexValue = appdexValue;
    }

    public int getProcessedPages() {
        return _processedPages;
    }

    public String getName() {
        return _name;
    }

    public int getCost() {
        return _cost;
    }

    public double getAppdexValue() {
        return _appdexValue;
    }

    public String getAccountName() {
        return _accountName;
    }

    public String getPagename() {

        return _pagename;
    }

    public String[] toCSVFields() {
        String[] result = new String[]{
                "\""+_accountName +"\"",
            "\""+_pagename+"\"" ,
            ""+_processedPages,
                "\""+_name+"\"" ,
            ""+_cost ,
            ""+_appdexValue

        };
        return result;

    }
}
