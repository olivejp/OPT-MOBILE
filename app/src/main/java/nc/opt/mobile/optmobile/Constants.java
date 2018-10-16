package nc.opt.mobile.optmobile;

/**
 * Created by orlanth23 on 08/08/2017.
 */

public class Constants {
    public static final String URL_SUIVI_COLIS = "http://webtrack.opt.nc/";
    public static final String URL_AFTERSHIP_BASE_URL = "https://api.aftership.com/";
    public static final String AFTER_SHIP_KEY = "e1bc990c-5652-4c88-8332-f60188329fe0";

    /**
     * Minutes we will wait before launch the sync
     */
    public static final long PERIODIC_SYNC_JOB_MINS = 15;

    /**
     * How close to the end of the period the job should run
     */
    public static final long INTERVAL_SYNC_JOB_MINS = 5;
}
