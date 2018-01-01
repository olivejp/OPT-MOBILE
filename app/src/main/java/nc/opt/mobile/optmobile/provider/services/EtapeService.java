package nc.opt.mobile.optmobile.provider.services;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import org.chalup.microorm.MicroOrm;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import nc.opt.mobile.optmobile.R;
import nc.opt.mobile.optmobile.domain.suivi.EtapeDto;
import nc.opt.mobile.optmobile.domain.suivi.aftership.Checkpoint;
import nc.opt.mobile.optmobile.provider.OptProvider;
import nc.opt.mobile.optmobile.provider.entity.ColisEntity;
import nc.opt.mobile.optmobile.provider.entity.EtapeEntity;
import nc.opt.mobile.optmobile.provider.interfaces.ColisInterface;
import nc.opt.mobile.optmobile.provider.interfaces.EtapeAcheminementInterface;
import nc.opt.mobile.optmobile.utils.DateConverter;

import static nc.opt.mobile.optmobile.provider.interfaces.EtapeAcheminementInterface.COMMENTAIRE;
import static nc.opt.mobile.optmobile.provider.interfaces.EtapeAcheminementInterface.DATE;
import static nc.opt.mobile.optmobile.provider.interfaces.EtapeAcheminementInterface.DESCRIPTION;
import static nc.opt.mobile.optmobile.provider.interfaces.EtapeAcheminementInterface.ID_COLIS;
import static nc.opt.mobile.optmobile.provider.interfaces.EtapeAcheminementInterface.LOCALISATION;
import static nc.opt.mobile.optmobile.provider.interfaces.EtapeAcheminementInterface.PAYS;
import static nc.opt.mobile.optmobile.provider.interfaces.EtapeAcheminementInterface.STATUS;

/**
 * Created by 2761oli on 23/10/2017.
 */

public class EtapeService {

    private EtapeService() {
    }

    private static final MicroOrm uOrm = new MicroOrm();

    private static final String AND = "=? AND ";

    // Check existence
    private static String mWhereEtapeExistenceWhere = ID_COLIS.concat(AND)
            .concat(DATE).concat(AND)
            .concat(DESCRIPTION).concat(AND)
            .concat(COMMENTAIRE).concat(AND)
            .concat(LOCALISATION).concat(AND)
            .concat(PAYS).concat("=?");

    /**
     * @param context
     * @param idColis
     * @return
     */
    public static List<EtapeEntity> listFromProvider(@NotNull Context context, @NotNull String idColis) {
        List<EtapeEntity> etapeList = new ArrayList<>();

        // Query the content provider to get a cursor of Etape
        Cursor cursorListEtape = context.getContentResolver().query(OptProvider.ListEtapeAcheminement.withIdColis(idColis), null, null, null, EtapeAcheminementInterface.DATE + " ASC");

        if (cursorListEtape != null) {
            while (cursorListEtape.moveToNext()) {
                etapeList.add(getFromCursor(cursorListEtape));
            }
            cursorListEtape.close();
        }
        return etapeList;
    }

    /**
     * @param context
     * @param idColis
     * @return
     */
    public static boolean delete(@NotNull Context context, @NotNull String idColis) {
        return context
                .getContentResolver()
                .delete(OptProvider.ListEtapeAcheminement.LIST_ETAPE, ColisInterface.ID_COLIS.concat("=?"), new String[]{idColis}) >= 1;
    }

    /**
     * @param context
     * @param etape
     * @param colis
     * @return
     */
    private static long insert(@NotNull Context context, @NotNull EtapeEntity etape, @NotNull ColisEntity colis) {
        Uri uri = context.getContentResolver().insert(OptProvider.ListEtapeAcheminement.LIST_ETAPE, putToContentValues(etape, colis.getIdColis()));
        return ContentUris.parseId(uri);
    }

    /**
     * Vérification si cette étape était déjà enregistrée, sinon on la créé.
     * On ne fait pas de mise à jour d'étape, soit l'étape existe, soit elle est créée.
     *
     * @param context
     * @param colis
     * @return
     */
    public static boolean save(@NotNull Context context, @NotNull ColisEntity colis) {
        boolean creation = false;
        if (colis.getEtapeAcheminementArrayList() != null && !colis.getEtapeAcheminementArrayList().isEmpty()) {
            for (EtapeEntity etape : colis.getEtapeAcheminementArrayList()) {
                if (!exist(context, colis.getIdColis(), etape)) {
                    creation = true;
                    insert(context, etape, colis);
                }
            }
        }
        return creation;
    }

    /**
     * Return true if a new etape is detected in the list
     *
     * @param context
     * @param colis
     * @return
     */
    public static boolean shouldInsertNewEtape(@NotNull Context context, @NotNull ColisEntity colis) {
        for (EtapeEntity etape : colis.getEtapeAcheminementArrayList()) {
            if (!exist(context, colis.getIdColis(), etape)) {
                return true;
            }
        }
        return false;
    }

    private static ContentValues putToContentValues(@NotNull EtapeEntity etapeEntity, @NotNull String idColis) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(PAYS, etapeEntity.getPays());
        contentValues.put(LOCALISATION, etapeEntity.getLocalisation());
        contentValues.put(COMMENTAIRE, etapeEntity.getCommentaire());
        contentValues.put(DESCRIPTION, etapeEntity.getDescription());
        contentValues.put(DATE, etapeEntity.getDate());
        contentValues.put(STATUS, etapeEntity.getStatus());
        contentValues.put(ID_COLIS, idColis);
        return contentValues;
    }

    private static EtapeEntity getFromCursor(@NotNull Cursor cursor) {
        return uOrm.fromCursor(cursor, EtapeEntity.class);
    }

    private static boolean exist(@NotNull Context context, @NotNull String idColis, @NotNull EtapeEntity etape) {
        String[] args = new String[]{idColis,
                String.valueOf(etape.getDate()),
                etape.getDescription(),
                etape.getCommentaire(),
                etape.getLocalisation(),
                etape.getPays()};

        Cursor cursor = context.getContentResolver()
                .query(OptProvider.ListEtapeAcheminement.LIST_ETAPE, null, mWhereEtapeExistenceWhere, args, null);
        if ((cursor != null) && cursor.getCount() > 0) {
            cursor.close();
            return true;
        }
        return false;
    }

    static EtapeEntity convertToEntity(@NotNull EtapeDto dto) {
        EtapeEntity entity = new EtapeEntity();
        entity.setDate(DateConverter.convertDateDtoToEntity(dto.getDate()));
        entity.setCommentaire(dto.getCommentaire());
        entity.setDescription(dto.getDescription());
        entity.setLocalisation(dto.getLocalisation());
        entity.setStatus(dto.getStatus());
        entity.setPays(dto.getPays());
        return entity;
    }

    /**
     * Va créer une étape à partir d'un checkpoint
     *
     * @param idColis
     * @param checkpoint
     * @return EtapeEntity
     */
    static EtapeEntity createEtapeFromCheckpoint(@NotNull String idColis, @NotNull Checkpoint checkpoint) {
        EtapeEntity etape = new EtapeEntity();
        etape.setIdColis(idColis);
        if (checkpoint.getCheckpointTime() != null) {
            etape.setDate(DateConverter.convertDateAfterShipToEntity(checkpoint.getCheckpointTime()));
        } else {
            etape.setDate(0L);
        }
        etape.setCommentaire("");
        etape.setLocalisation((checkpoint.getLocation() != null) ? checkpoint.getLocation().toString() : "");
        etape.setStatus((checkpoint.getTag() != null) ? checkpoint.getTag() : "");
        etape.setDescription((checkpoint.getMessage() != null) ? checkpoint.getMessage() : "");
        etape.setPays((checkpoint.getCountryName() != null) ? checkpoint.getCountryName().toString() : "");
        return etape;
    }

    public static int getStatusDrawable(@NotNull String status) {
        if (status != null) {
            switch (status) {
                case "InfoReceived":
                    return R.drawable.ic_status_info_receive;
                case "AttemptFail":
                    return R.drawable.ic_status_attemptfail;
                case "Delivered":
                    return R.drawable.ic_status_delivered;
                case "Exception":
                    return R.drawable.ic_status_exception;
                case "Expired":
                    return R.drawable.ic_status_expired;
                case "InTransit":
                    return R.drawable.ic_status_in_transit;
                case "OutForDelivery":
                    return R.drawable.ic_status_out_for_delivery;
                case "Pending":
                    return R.drawable.ic_status_pending;
                default:
                    return R.drawable.ic_status_pending;
            }
        }
        return R.drawable.ic_status_pending;
    }
}
