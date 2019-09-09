package plugins.dto;

/**
 * Created by sargonbenjamin on 9/9/19.
 */
public class GeneralPluginsParameters {

    /**
     * Plugin name
     */
    private String pluginName;
    /**
     * Interval (in seconds) for starting collection metrics
     */
    private long interval = 10 ;
    /**
     * Date of previous start in milliseconds
     */
    private long previousCycleStarted = 0;

    private String maprPrefix = null ;

    public String getPluginName() {
        return pluginName;
    }

    public void setPluginName(String pluginName) {
        this.pluginName = pluginName;
    }

    public long getInterval() {
        return interval;
    }

    public void setInterval(long interval) {
        this.interval = interval;
    }

    public long getPreviousCycleStarted() {
        return previousCycleStarted;
    }

    public void setPreviousCycleStarted(long previousCycleStarted) {
        this.previousCycleStarted = previousCycleStarted;
    }

    public String getMaprPrefix() {
        return maprPrefix;
    }

    public void setMaprPrefix(String maprPrefix) {
        this.maprPrefix = maprPrefix;
    }
}
