package nc.opt.mobile.optmobile;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.google.gson.Gson;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;

import nc.opt.mobile.optmobile.domain.FeatureCollection;


/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() throws Exception {
        Context appContext = InstrumentationRegistry.getTargetContext();
        Gson gson = new Gson();
        String agencies = Utilities.loadJSONFromAsset(appContext, "opt_agencies.json");
        FeatureCollection featureCollection = gson.fromJson(agencies, FeatureCollection.class);
        Assert.assertTrue(featureCollection.getFeatures().size() > 0);
    }
}
