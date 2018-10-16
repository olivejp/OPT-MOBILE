package nc.opt.mobile.optmobile.database.local;

/**
 * Created by 2761oli on 18/01/2018.
 */
public enum StepOrigine {
    OPT("OPT"),
    AFTER_SHIP("AFTERSHIP");

    private final String value;

    StepOrigine(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

    @Override
    public String toString() {
        return value;
    }
}
