package collectd;

import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;

/**
 * Created by sargonbenjamin on 9/6/19.
 */
public class RestConnection extends ServerConnection {

    private WebResource connection;
    private Client client;
    private ClientResponse response;

    public void connect() {
        client = Client.create();
        addFilter();

        connection = client.resource(connectionUrl);
    }

    @Override
    public void setConnectTimer() {
        connectTimer = new Timer("Connect-" + connectionUrl, true);
        if (ttl > 0) {
            connectTimer.schedule(new ReconnectTask(), TimeUnit.MILLISECONDS.convert(ttl, TimeUnit.SECONDS));
        }
    }

    public WebResource getConnection(){
        return connection;
    }

    private class ReconnectTask extends TimerTask {
        @Override
        public void run() {
            System.out.println(String.format("Error or TTL Expiration for %s forcing reconnect..", connectionUrl));
            connection = null;
            client.destroy();
            response.close();
        }
    }

    /*
    @Override
    public void connect() {
        client = Client.create();

    }*/

    private void addFilter() {
        try {
            final URL url = new URL(connectionUrl);

            if (url.getUserInfo() != null) {
                System.out.println("Found user info on URL, using basic authentication");
                // there's a username and password on the URL. Use it in the filter
                //final String[] unPswd = url.getUserInfo().split(":");
                String[] unPswd = {"mapr","mapr"};
                client.addFilter(new HTTPBasicAuthFilter(unPswd[0], unPswd[1]));
                System.out.println(String.format("Added client filter for user: %s%n", unPswd[0]));
            }
        }
        catch (Exception e) {
            System.out.println("Could not add filter to client connection: " + e);
        }
    }


    public String getResponse() throws Exception {
        if (connection == null){
            connect();
        }

        response = connection.accept("application/json").get(ClientResponse.class);

        if(response.getStatus()!=200){
            throw new Exception(String.format("Failed : HTTP error code : %d", response.getStatus()));
        }

        //get the response
        return response.getEntity(String.class);

    }
}
