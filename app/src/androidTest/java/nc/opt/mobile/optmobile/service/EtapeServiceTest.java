package nc.opt.mobile.optmobile.service;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import nc.opt.mobile.optmobile.ProviderTestUtilities;
import nc.opt.mobile.optmobile.database.entity.ColisEntity;
import nc.opt.mobile.optmobile.database.entity.EtapeEntity;
import nc.opt.mobile.optmobile.database.services.ColisService;
import nc.opt.mobile.optmobile.database.services.EtapeService;
import nc.opt.mobile.optmobile.utils.DateConverter;

/**
 * Created by 2761oli on 07/11/2017.
 */

@RunWith(AndroidJUnit4.class)
public class EtapeServiceTest {

    private Context mContext;
    private String DATE_INCORRECTE = "2342348098kj";
    private String ID_COLIS = "RC123456789US";
    private String DESCRIPTION = "My Description";
    private String DESCRIPTION_ETAPE = "Etape description";

    @Before
    public void precondition() {
        mContext = InstrumentationRegistry.getTargetContext();
    }

    private long insertColis() {
        ColisEntity colisEntity = new ColisEntity();
        colisEntity.setIdColis(ID_COLIS);
        colisEntity.setDescription(DESCRIPTION);
        long id = ColisService.insert(mContext, colisEntity);
        Assert.assertTrue(id > 0);
        return id;
    }

    private void insertEtape(String idColis) {
        insertColis();
        EtapeEntity etape = new EtapeEntity();
        etape.setDate(DateConverter.convertDateAfterShipToEntity(DATE_INCORRECTE));
        etape.setDescription(DESCRIPTION_ETAPE);
        ColisEntity colis = ColisService.get(mContext, idColis);

        Assert.assertTrue(EtapeService.save(mContext, colis));
    }

    @Test
    public void testInsertEtape() {
        ProviderTestUtilities.deleteRecords(mContext, OptProvider.ListColis.LIST_COLIS);
        ProviderTestUtilities.deleteRecords(mContext, OptProvider.ListEtapeAcheminement.LIST_ETAPE);
        insertEtape(ID_COLIS);
    }

    @Test
    public void testInsertAndDeleteEtape() {
        ProviderTestUtilities.deleteRecords(mContext, OptProvider.ListColis.LIST_COLIS);
        ProviderTestUtilities.deleteRecords(mContext, OptProvider.ListEtapeAcheminement.LIST_ETAPE);

        insertEtape(ID_COLIS);

        List<EtapeEntity> listBefore = EtapeService.listFromProvider(mContext, ID_COLIS);
        Assert.assertNotNull(listBefore);
        Assert.assertTrue(listBefore.size() == 1);

        EtapeService.delete(mContext, ID_COLIS);

        List<EtapeEntity> listAfter = EtapeService.listFromProvider(mContext, ID_COLIS);
        Assert.assertNotNull(listAfter);
        Assert.assertTrue(listAfter.size() == 0);
    }
}
