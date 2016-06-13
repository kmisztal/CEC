package cec.monitoring;

import java.util.HashMap;
import java.util.Map;

import net.bull.javamelody.Parameter;

/**
 * Created by Mateusz Reczkowski on 12.06.2016.
 */
public class Monitor {

    public static void runMonitoring(String path, double seconds, int port) throws Exception {

        //enter localhost:8080 with 'admin' login and 'pwd' password to enter statistics
        final Map<Parameter, String> parameters = new HashMap<>();

        // you can add basic auth:
        parameters.put(Parameter.AUTHORIZED_USERS, "admin:pwd");

        // you can change the default storage directory:
        parameters.put(Parameter.STORAGE_DIRECTORY, "" + path);

        // you can enable hotspots sampling with a period of 1 second:
        parameters.put(Parameter.SAMPLING_SECONDS, "" + seconds);

        // set the path of the reports:
        parameters.put(Parameter.MONITORING_PATH, "/");

        // start the embedded http server with javamelody
        EmbeddedServer.start(port, parameters);
    }

    public static void main(String[] args) throws Exception {

        runMonitoring("C:\\diagrams",1.0,8080);

    }
}
