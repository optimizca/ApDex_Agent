package de.appdynamics.ace.apdex.cli;

import com.appdynamics.ace.util.cli.api.api.CommandlineExecution;

/**
 * Created by stefan.marx on 16.03.15.
 */
public class Main {

    public static void main(String[] args) {
        CommandlineExecution cle = new CommandlineExecution("MetricExportCLI");

        cle.setHelpVerboseEnabled(true);

        cle.addCommand(new ExportApdexCommand());

        System.exit(cle.execute(args));
    }
}
