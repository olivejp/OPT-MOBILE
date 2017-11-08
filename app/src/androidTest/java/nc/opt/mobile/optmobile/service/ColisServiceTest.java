package nc.opt.mobile.optmobile.service;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import nc.opt.mobile.optmobile.ProviderTestUtilities;
import nc.opt.mobile.optmobile.provider.OptProvider;
import nc.opt.mobile.optmobile.provider.entity.ColisEntity;
import nc.opt.mobile.optmobile.provider.services.ColisService;

/**
 * Created by 2761oli on 07/11/2017.
 */

@RunWith(AndroidJUnit4.class)
public class ColisServiceTest {

    private Context mContext;
    private String ID = "RC123456789US";
    private String DESCRIPTION = "My Description";

    @Before
    public void precondition() {
        mContext = InstrumentationRegistry.getTargetContext();
    }

    private long insertColis() {
        ColisEntity colisEntity = new ColisEntity();
        colisEntity.setIdColis(ID);
        colisEntity.setDescription(DESCRIPTION);
        long id = ColisService.insert(mContext, colisEntity);
        Assert.assertTrue(id > 0);
        return id;
    }

    @Test
    public void testInsertColis() {
        ProviderTestUtilities.deleteRecords(mContext, OptProvider.ListColis.LIST_COLIS);
        insertColis();
    }

    @Test
    public void testInsertAndDeleteColis() {
        ProviderTestUtilities.deleteRecords(mContext, OptProvider.ListColis.LIST_COLIS);
        insertColis();
        boolean deleted = ColisService.delete(mContext, ID);
        Assert.assertTrue(deleted);
    }

    @Test
    public void testInsertAndGetColis() {
        ProviderTestUtilities.deleteRecords(mContext, OptProvider.ListColis.LIST_COLIS);
        insertColis();
        ColisEntity colisEntity = ColisService.get(mContext, ID);
        if (colisEntity != null) {
            Assert.assertEquals(DESCRIPTION, colisEntity.getDescription());
        } else {
            Assert.fail();
        }
    }
}
