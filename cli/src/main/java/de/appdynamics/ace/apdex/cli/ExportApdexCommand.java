package de.appdynamics.ace.apdex.cli;

import com.appdynamics.ace.util.cli.api.api.AbstractCommand;
import com.appdynamics.ace.util.cli.api.api.CommandException;
import com.appdynamics.ace.util.cli.api.api.OptionWrapper;
import de.appdynamics.ace.apdex.agent.api.API;
import de.appdynamics.ace.apdex.util.api.APIProxy;
import de.appdynamics.ace.apdex.agent.api.dto.Config;
import de.appdynamics.ace.apdex.agent.api.dto.ResultLine;
import de.appdynamics.ace.apdex.agent.api.dto.Results;
import org.apache.commons.cli.Option;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by stefan.marx on 16.03.15.
 */
public class ExportApdexCommand extends AbstractCommand {
    private static final String ARG_OUTFILE = "out";
    private static final String ARG_CONFIG = "config";
    private static final String ARG_PROXY = "proxy";
    private static final String ARG_PROXYUSER = "proxypwd";
    private static final String ARG_PROXYPASS = "proxyuser";

    @Override
    protected List<Option> getCLIOptionsImpl() {
        ArrayList<Option> opts = new ArrayList<Option>();

        Option o;

        opts.add(o = constructCommand(ARG_CONFIG, true, "Report Config File. (JSON)", true));
        opts.add(o = constructCommand(ARG_OUTFILE, true, "CSV Output File.",false));
        opts.add(o = constructCommand(ARG_PROXY, true, "HTTP Proxy",false));
        opts.add(o = constructCommand(ARG_PROXYUSER, true, "HTTP Proxy Authentication (Username)",false));
        opts.add(o = constructCommand(ARG_PROXYPASS, true, "HTTP Proxy Authentication (Password)",false));


        return opts;
    }

    private Option constructCommand(String optionName, boolean hasArg, String description, boolean required) {
        Option o = new Option(optionName, hasArg, description);

        o.setRequired(required);
        return o;
    }

    @Override
    protected int executeImpl(OptionWrapper optionWrapper) throws CommandException {

        try {
            Config[] c = API.loadConfig(new File(optionWrapper.getOptionValue(ARG_CONFIG)));

            PrintStream writer = System.out;
            if (optionWrapper.hasOption(ARG_OUTFILE)) {
                writer = new PrintStream(new FileOutputStream(optionWrapper.getOptionValue(ARG_OUTFILE)));
            }


            writer.append(CSVUtil.compileCSVLine(ResultLine.HEADER_FIELDS,0,""))
                    .append("\n");

            for (Config cfg :c) {
                APIProxy proxy = null;
                if (optionWrapper.hasOption(ARG_PROXY)) {
                    proxy = new APIProxy(optionWrapper.getOptionValue(ARG_PROXY));
                    if (optionWrapper.hasOption(ARG_PROXYPASS)) proxy.setPassword(optionWrapper.getOptionValue(ARG_PROXYPASS));
                    if (optionWrapper.hasOption(ARG_PROXYUSER)) proxy.setUsername(optionWrapper.getOptionValue(ARG_PROXYUSER));
                }

                Results result = API.runApdex(cfg, true,proxy);

                for (ResultLine l:result.getLines()) {
                    writer.append(CSVUtil.compileCSVLine(l.toCSVFields(),0,""))
                        .append("\n");

                }

                System.err.println("Processed :"+cfg.getAccountName()+"  .. proccessed a total of "+result.getTotalPages()+" In " + (result.getProcessingTime()/1000)+ " seconds.");

            }


        } catch (IOException e) {
            e.printStackTrace();
        }


        return 0;
    }

    @Override
    public String getName() {
        return "exportCSV";
    }

    @Override
    public String getDescription() {
        return "Reads a Config File (Json format) and writes the results as CSV.";
    }
}
