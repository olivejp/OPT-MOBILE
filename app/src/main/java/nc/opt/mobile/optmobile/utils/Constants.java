package nc.opt.mobile.optmobile.utils;

/**
 * Created by orlanth23 on 08/08/2017.
 */

public class Constants {
    public static final String URL_API_AGENCIES_REST = "https://opendata.arcgis.com/datasets/f39610c502074eda9b5e575870fdb4ee_0.geojson";
    public static final String URL_API_FIBRE_REST = "https://opendata.arcgis.com/datasets/41bbc1919fad49528d688395bfbac5be_0.geojson";

    // Careful, JSON is heavy (51 Mb). Don't download this link with Data.
    public static final String URL_API_COUVERTURE_MOBILE_REST = "https://opendata.arcgis.com/datasets/37ff8020bb7c4585874c8eae4678e54b_0.geojson";
    public static final String URL_SUIVI_COLIS = "http://webtrack.opt.nc/ipswebtracking/";
    public static final String URL_SUIVI_SERVICE_OPT = "IPSWeb_item_events.asp";

    public static final String ENCODING_ISO = "iso8859-1";
    public static final String ENCODING_UTF_8 = "UTF-8";

    public static final String FIREBASE_DATABASE = "firebase_database";
    public static final String CLOUD_FIRESTORE = "cloud_firestore";
    public static final String DATABASE_USERS_REFERENCE = "users";

    // API aftership endpoint
    public static final String AFTER_SHIP_ENDPOINT = "https://api.aftership.com/v4/";

    /**
     * Minutes we will wait before launch the sync
     */
    public static final long PERIODIC_SYNC_JOB_MINS = 15;

    /**
     * How close to the end of the period the job should run
     */
    public static final long INTERVAL_SYNC_JOB_MINS = 5;


    public static final String PREF_USER = "POPULATE_USER";
}
