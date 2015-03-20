package de.appdynamics.ace.apdex.util.api;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by stefan.marx on 22.12.14.
 */
public class PageDataDO {
    private String _pagename;
    private int _processedPages;
    private List<BandDO> _bands = new ArrayList<BandDO>();

    public PageDataDO(String key) {
        setPagename(key);
    }

    public static double calcAppdex(PageDataDO p) throws AppdexException {
        List<BandDO> bands = p.getBands();
        if (bands.size()!=3) {
            throw new AppdexException("Wrong Bands Size ::"+bands.size());
        }   else  {
            return (bands.get(0).getPageCount()+(0.5*bands.get(1).getPageCount()))/p.getProcessedPages();
        }
    }

    public void setPagename(String pagename) {
        _pagename = pagename;
    }

    public String getPagename() {
        return _pagename;
    }

    public void setprocessedPages(int processedPages) {
        _processedPages = processedPages;
    }

    public int getProcessedPages() {
        return _processedPages;
    }

    public void setProcessedPages(int processedPages) {
        _processedPages = processedPages;
    }

    public void addBandData(BandDO dataBand) {
        _bands.add(dataBand);

    }

    public List<BandDO> getBands() {
        return Collections.unmodifiableList(_bands);

    }
}
