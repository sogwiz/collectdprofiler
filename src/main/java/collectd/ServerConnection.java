package collectd;

import java.util.Timer;

/**
 * Created by sargonbenjamin on 9/6/19.
 */
public abstract class ServerConnection {
    public String connectionUrl;
    public String service;
    public Object connection;
    public Timer connectTimer;
    public long ttl;

    /**
     * @return the connectionUrl
     */
    public String getConnectionUrl() {
        return connectionUrl;
    }

    /**
     * @param connectionUrl the connectionUrl to set
     */
    public void setConnectionUrl(String connectionUrl) {
        this.connectionUrl = connectionUrl;
    }

    /**
     * @return the service
     */
    public String getService() {
        return service;
    }

    /**
     * @param service the service to set
     */
    public void setService(String service) {
        this.service = service;
    }

    /**
     * @param ttl the ttl to set
     */
    public void setTtl(long ttl) {
        this.ttl = ttl;
    }

    public abstract String getResponse() throws Exception;

    public abstract void connect();

    public abstract void setConnectTimer();

}
