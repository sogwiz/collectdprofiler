package plugins.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import collectd.ServerConnection;

/**
 * Created by sargonbenjamin on 9/9/19.
 */
public class SparkPluginParameters extends GeneralPluginsParameters {
    /**
     * Url for json with metrics
     */
    private String serviceUrl;
    /**
     * Url for spark application id
     */
    private String idUrl;
    /**
     * Map with metric and it path in json file
     */
    private Map<String, String> metricsMap;
    /**
     * connection ttl
     */
    private long ttl;
    /**
     * spark application id
     */
    private String sparkId;
    /**
     * connection list
     */
    private List<ServerConnection> connectionList = new ArrayList<ServerConnection>();


    public String getServiceUrl() {
        return serviceUrl;
    }

    public void setServiceUrl(String url) {
        this.serviceUrl = url;
    }

    public String getIdUrl() {
        return idUrl;
    }

    public void setIdUrl(String idUrl) {
        this.idUrl = idUrl;
    }

    public Map<String, String> getMetricsMap() {
        return metricsMap;
    }

    public void setMetricsMap(Map<String, String> metricsMap) {
        this.metricsMap = metricsMap;
    }

    public long getTtl() {
        return ttl;
    }

    public void setTtl(long ttl) {
        this.ttl = ttl;
    }

    public String getSparkId() {
        return sparkId;
    }

    public void setSparkId(String sparkId) {
        this.sparkId = sparkId;
    }

    public List<ServerConnection> getConnectionList() {
        return connectionList;
    }

    public void setConnectionList(List<ServerConnection> connectionList) {
        this.connectionList = connectionList;
    }

    public void addConnection(ServerConnection connection){
        this.connectionList.add(connection);
    }

    public ServerConnection getConnectionByServiceName(String serviceName){
        for (ServerConnection connection: connectionList) {
            if (connection.getService().equals(serviceName)){
                return connection;
            }
        }
        throw new NoSuchElementException(String.format("Connection for %s", serviceName));
    }
}
