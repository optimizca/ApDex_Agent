package de.appdynamics.ace.apdex.util.api;

/**
 * Created by stefan.marx on 22.12.14.
 */
public class BandDO {
    private long _lower;
    private long _upper;
    private long _pageCount;

    public String getBandKey() {
        return _bandKey;
    }

    private final String _bandKey;

    public BandDO(String key) {
        _bandKey=key;
    }

    public void setLower(long lower) {
        _lower = lower;
    }

    public long getLower() {
        return _lower;
    }

    public void setUpper(long upper) {
        _upper = upper;
    }

    public long getUpper() {
        return _upper;
    }

    public void setPageCount(long pageCount) {
        _pageCount = pageCount;
    }

    public long getPageCount() {
        return _pageCount;
    }
}
