package nc.opt.mobile.optmobile.service;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import nc.opt.mobile.optmobile.ProviderTestUtilities;
import nc.opt.mobile.optmobile.database.entity.ColisEntity;
import nc.opt.mobile.optmobile.database.services.ColisService;

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

        int deleted = ColisService.delete(mContext, ID);
        Assert.assertTrue(deleted == 1);

        int countNotDeleted = ColisService.count(mContext, false);
        Assert.assertTrue(countNotDeleted == 1);

        int countReallyDeleted = ColisService.count(mContext, true);
        Assert.assertTrue(countReallyDeleted == 0);

        ColisEntity colisEntity = ColisService.get(mContext, ID);
        Assert.assertNotNull(colisEntity);
        Assert.assertNotNull(colisEntity.getDeleted());
        Assert.assertEquals(new Integer(1), colisEntity.getDeleted());
    }

    @Test
    public void testInsertAndRealDeleteColis() {
        ProviderTestUtilities.deleteRecords(mContext, OptProvider.ListColis.LIST_COLIS);
        insertColis();

        int deleted = ColisService.realDelete(mContext, ID);
        Assert.assertTrue(deleted > 0);

        int countDeleted = ColisService.count(mContext, true);
        Assert.assertTrue(countDeleted == 0);

        int countRealDeleted = ColisService.count(mContext, false);
        Assert.assertTrue(countRealDeleted == 0);

        ColisEntity colisEntity = ColisService.get(mContext, ID);
        Assert.assertNull(colisEntity);
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
