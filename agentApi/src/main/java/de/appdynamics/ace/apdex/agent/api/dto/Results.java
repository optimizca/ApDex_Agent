package de.appdynamics.ace.apdex.agent.api.dto;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by stefan.marx on 16.03.15.
 */
public class Results {

    List<ResultLine> lines = new ArrayList<ResultLine>();
    private long _processingTime;

    public long getTotalPages() {
        return _totalPages;
    }

    private long _totalPages = 0;


    public List<ResultLine> getLines() {
        return lines;
    }

    public Results addLine(ResultLine line) {
        lines.add(line);
        _totalPages+=line.getProcessedPages();
        return this;
    }

    public void setProcessingTime(long processingTime) {
        _processingTime = processingTime;
    }

    public long getProcessingTime() {
        return _processingTime;
    }
}
