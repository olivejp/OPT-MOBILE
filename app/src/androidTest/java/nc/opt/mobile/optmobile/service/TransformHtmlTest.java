package nc.opt.mobile.optmobile.service;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import nc.opt.mobile.optmobile.domain.suiviColis.ColisDto;
import nc.opt.mobile.optmobile.utils.HtmlTransformer;
import nc.opt.mobile.optmobile.utils.Utilities;

import static nc.opt.mobile.optmobile.utils.HtmlTransformer.RESULT_SUCCESS;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class TransformHtmlTest {

    private Context mContext;

    @Before
    public void precondition() {
        mContext = InstrumentationRegistry.getTargetContext();
    }

    @Test
    public void catchColisFromUrl() throws Exception {
        ColisDto colisDto = new ColisDto();
        String file = Utilities.loadStringFromAsset(mContext, "search_parcel_sample_result.html");
        int result = HtmlTransformer.getColisFromHtml(file, colisDto);
        Assert.assertEquals(RESULT_SUCCESS, result);

        Assert.assertEquals(12, colisDto.getEtapeAcheminementDtoArrayList().size());
    }
}