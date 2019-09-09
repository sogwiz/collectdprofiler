package utils;

import java.net.URL;

import collectd.RestConnection;
import collectd.ServerConnection;

/**
 * Created by sargonbenjamin on 9/8/19.
 */
public class RestUtils {

    private RestUtils() {
    }

    public static ServerConnection createConnection(String serviceName, String serviceUrl, long ttl){
        final ServerConnection connection = getConnection(serviceUrl);

        connection.setService(serviceName);
        connection.setConnectionUrl(serviceUrl);
        connection.setTtl(ttl);

        // Check if all the required values have been extracted
        if (connection.getConnectionUrl() == null) {
            throw new IllegalArgumentException("No service url was defined.");
        }
        if (connection.getService() == null) {
            throw new IllegalArgumentException("No service name was defined.");
        }

        connection.setConnectTimer();
        // Establish the connection
        connection.connect();

        return connection;
    }

    private static ServerConnection getConnection(String serviceUrl){
        ServerConnection connection = new RestConnection();

        try {
            final URL url = new URL(serviceUrl);
        }catch (Exception e){
            e.printStackTrace();
        }

        return connection;

    }
}
