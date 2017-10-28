package nc.opt.mobile.optmobile.provider.services;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import org.chalup.microorm.MicroOrm;

import java.util.ArrayList;
import java.util.List;

import nc.opt.mobile.optmobile.domain.EtapeAcheminementDto;
import nc.opt.mobile.optmobile.provider.OptProvider;
import nc.opt.mobile.optmobile.provider.entity.EtapeAcheminementEntity;
import nc.opt.mobile.optmobile.utils.DateConverter;

import static nc.opt.mobile.optmobile.provider.interfaces.EtapeAcheminementInterface.COMMENTAIRE;
import static nc.opt.mobile.optmobile.provider.interfaces.EtapeAcheminementInterface.DATE;
import static nc.opt.mobile.optmobile.provider.interfaces.EtapeAcheminementInterface.DESCRIPTION;
import static nc.opt.mobile.optmobile.provider.interfaces.EtapeAcheminementInterface.ID_COLIS;
import static nc.opt.mobile.optmobile.provider.interfaces.EtapeAcheminementInterface.LOCALISATION;
import static nc.opt.mobile.optmobile.provider.interfaces.EtapeAcheminementInterface.PAYS;
import static nc.opt.mobile.optmobile.utils.DateConverter.convertDateDtoToEntity;

/**
 * Created by 2761oli on 23/10/2017.
 */

public class EtapeAcheminementService {

    private static final MicroOrm uOrm = new MicroOrm();

    // Check existence
    private static String mWhereEtapeExistenceWhere = ID_COLIS.concat("=? AND ")
            .concat(DATE).concat("=? AND ")
            .concat(DESCRIPTION).concat("=? AND ")
            .concat(COMMENTAIRE).concat("=? AND ")
            .concat(LOCALISATION).concat("=? AND ")
            .concat(PAYS).concat("=?");

    public static List<EtapeAcheminementEntity> listFromProvider(Context context, String idColis) {
        List<EtapeAcheminementEntity> etapeList = new ArrayList<>();

        // Query the content provider to get a cursor of Etape
        Cursor cursorListEtape = context.getContentResolver().query(OptProvider.ListEtapeAcheminement.withIdColis(idColis), null, null, null, null);

        if (cursorListEtape != null) {
            while (cursorListEtape.moveToNext()) {
                EtapeAcheminementEntity etapeEntity = getFromCursor(cursorListEtape);
                etapeList.add(etapeEntity);
            }
            cursorListEtape.close();
        }
        return etapeList;
    }

    /**
     * Vérification si cette étape était déjà enregistrée, sinon on la créé.
     *
     * @param context
     * @param idColis
     * @param listEtape
     * @return true if at least one etape has been created, false instead.
     */
    public static boolean checkAndInsert(Context context, String idColis, List<EtapeAcheminementDto> listEtape) {
        boolean creation = false;
        for (EtapeAcheminementDto etape : listEtape) {
            if (!exist(context, idColis, etape)) {
                // Création
                creation = true;
                context.getContentResolver().insert(OptProvider.ListEtapeAcheminement.LIST_ETAPE, putToContentValues(etape, idColis));
            }
        }
        return creation;
    }

    private static ContentValues putToContentValues(EtapeAcheminementDto etapeAcheminementDto, String idColis) {

        ContentValues contentValues = new ContentValues();
        contentValues.put(PAYS, etapeAcheminementDto.getPays());
        contentValues.put(LOCALISATION, etapeAcheminementDto.getLocalisation());
        contentValues.put(COMMENTAIRE, etapeAcheminementDto.getCommentaire());
        contentValues.put(DESCRIPTION, etapeAcheminementDto.getDescription());
        contentValues.put(DATE, convertDateDtoToEntity(etapeAcheminementDto.getDate()));
        contentValues.put(ID_COLIS, idColis);
        return contentValues;
    }

    private static EtapeAcheminementEntity getFromCursor(Cursor cursor) {
        return uOrm.fromCursor(cursor, EtapeAcheminementEntity.class);
    }

    private static boolean exist(Context context, String idColis, EtapeAcheminementDto etape) {
        String[] args = new String[]{idColis,
                String.valueOf(DateConverter.convertDateDtoToEntity(etape.getDate())),
                etape.getDescription(),
                etape.getCommentaire(),
                etape.getLocalisation(),
                etape.getPays()};

        Cursor cursor = context.getContentResolver()
                .query(OptProvider.ListEtapeAcheminement.LIST_ETAPE, null, mWhereEtapeExistenceWhere, args, null);
        if ((cursor != null) && (cursor.moveToFirst())) {
            cursor.close();
            return true;
        }
        return false;
    }
}
