package de.appdynamics.ace.apdex.cli;

/**
 * Created by stefan.marx on 11.03.15.
 */
public class CSVUtil {
    static String compileCSVLine(String[] fields, int i, String prefix) {

        String line = (prefix == null) ? "" : prefix;
        for (; i < fields.length; i++) {
            if (line.length() > 0) line = line + ",";
            String f = fields[i];

            if (f.startsWith("\"") && f.endsWith("\"")) {
                f =  f.substring(1, f.lastIndexOf('"'));
                f = f.replaceAll("\"","'");
                f='"'+f+'"';
            } else {
                f = f.replaceAll("\"","'");
            }

            f = f.replaceAll(","," ");


            if (f.contains(",") && !f.startsWith("\"") && !f.endsWith("\"") ) {
                f='"'+f+'"';
            }

            line = line + f;
        }

        return line;
    }
}
