package plugins;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import collectd.ServerConnection;
import plugins.dto.SparkPluginParameters;
import utils.RestUtils;

/**
 * Created by sargonbenjamin on 9/9/19.
 */
public class TestPlugin {

    private static final String URL_NAME = "ServiceURL";
    private static final String ID_URL_NAME = "IdURL";
    private static final String TEST_PREFIX = "test.spark";
    private static final String SPARK_ID_NAME = "{spark_id}";

    private static final String serviceUrl = "http://localhost";

    private final String simpleName = getClass().getSimpleName();
    private final SparkPluginParameters sparkPluginParameters = new SparkPluginParameters();

    public TestPlugin() {
        registerPlugin();
    }

    private void registerPlugin(){
        sparkPluginParameters.setPluginName(simpleName);
        sparkPluginParameters.setMaprPrefix(TEST_PREFIX);

    }

    /**
     * Create connection and add it to the collectionList
     */
    private void createConnections() {
        final String pluginName = sparkPluginParameters.getPluginName();

        createUrlConnection(pluginName, serviceUrl, URL_NAME);
        createUrlConnection(pluginName, serviceUrl, ID_URL_NAME);
    }

    private void createUrlConnection(String pluginName, String serviceUrl, String urlName) {
        ServerConnection connection;
        try {
            System.out.println(String.format("%s: Creating connection to %s", pluginName, serviceUrl));
            connection = RestUtils
                .createConnection(urlName, serviceUrl, 120);
            sparkPluginParameters.addConnection(connection);
        } catch (IllegalArgumentException e) {
            System.out.println(String.format("%s: FAIL when create connection with exception ", pluginName));
        }
    }

    public int read() {
        final String pluginName = sparkPluginParameters.getPluginName();

        createConnections();

        if (sparkPluginParameters.getConnectionList() == null){
            System.out.println(String.format("%s: connection haven't created", pluginName));
            return 0;
        }

        final long previousCycleStarted = sparkPluginParameters.getPreviousCycleStarted();
        final long currentTime = System.currentTimeMillis();
        final long timeSinceLastCycle = TimeUnit.SECONDS.convert(currentTime - previousCycleStarted, TimeUnit.MILLISECONDS);

        if (previousCycleStarted == 0 || timeSinceLastCycle >= sparkPluginParameters.getInterval()) {
            System.out.println(String.format("%s: read() interval %d", pluginName, sparkPluginParameters.getInterval()));

            try {
                readSparkMetrics();
            } catch (Exception e) {
                System.out.println(String.format("%s: Failed to read json with error: ", pluginName));
                sparkPluginParameters.setPreviousCycleStarted(0);
            }

            sparkPluginParameters.setPreviousCycleStarted(currentTime);
        } else {
            int index = pluginName.indexOf("Plugin");
            String substring = pluginName.substring(0, index);
            System.out.println(String.format("%s: is not gathering %s data because the interval was not reached", pluginName, substring));
        }
        return 0;
    }


    /**
     * Get json from url, parse it
     * and submit metrics which are present at the same time at metricsMap and json
     * @throws Exception exception
     */
    private void readSparkMetrics() throws Exception {
        final String pluginName = sparkPluginParameters.getPluginName();
        final String sparkId = getSparkId();

        if (sparkId == null) {
            throw new Exception(String.format("%s: Failed to get spark_id from %s", pluginName, ID_URL_NAME));
        }

        sparkPluginParameters.setSparkId(sparkId);
        setSparkIdToTheMetricsList();

        final ServerConnection connection = sparkPluginParameters.getConnectionByServiceName(URL_NAME);

        try {
            String response = connection.getResponse();
            //getData(response, sparkPluginParameters.getMetricsMap(), sparkPluginParameters.getMaprPrefix());
        } catch (Exception e) {
            System.out.println(String.format("%s: Failed to get metrics for service: %s ", pluginName, connection.getService()));
        }
    }

    private String getSparkId() {
        final ServerConnection connection = sparkPluginParameters.getConnectionByServiceName(ID_URL_NAME);
        final String connUrl = connection.getConnectionUrl();
        final String pluginName = sparkPluginParameters.getPluginName();
        String sparkId = null;

        System.out.println(String.format("%s: Get spark id at url: %s", pluginName, connUrl));
        try {
            final String response = connection.getResponse();
            System.out.println("Response is " + response);
            //sparkId = getStringDataFromJson(response, "/0/id");
            sparkId = "123";
        } catch (Exception e) {
            System.out.println(String.format("%s: Failed to get metrics for service: %s at url: %s",
                pluginName, connection.getService(), connUrl));
        }

        return sparkId;
    }

    private void setSparkIdToTheMetricsList() {
        for (Map.Entry entry : sparkPluginParameters.getMetricsMap().entrySet()) {
            String key = entry.getKey().toString();
            String metricsPath = entry.getValue().toString().replace(SPARK_ID_NAME, sparkPluginParameters.getSparkId());
            sparkPluginParameters.getMetricsMap().put(key, metricsPath);
        }
    }


    public static void main(String args[]) throws InterruptedException {
        TestPlugin testPlugin = new TestPlugin();

        int counter = 0;
        while (counter < 100000) {
            testPlugin.read();
            Thread.sleep(100);
            counter++;
        }
    }

}
