package nc.opt.mobile.optmobile.domain.suiviColis;

import nc.opt.mobile.optmobile.provider.entity.EtapeEntity;

/**
 * Created by 2761oli on 04/12/2017.
 */

public class EtapeConsolidated extends EtapeEntity {

    public enum TypeEtape {
        HEADER,
        DETAIL
    }

    private TypeEtape type;

    public TypeEtape getType() {
        return type;
    }

    public void setType(TypeEtape type) {
        this.type = type;
    }

    public EtapeConsolidated(TypeEtape type, EtapeEntity etapeAcheminementEntity) {
        this.idEtapeAcheminement = etapeAcheminementEntity.getIdEtapeAcheminement();
        this.idColis = etapeAcheminementEntity.getIdColis();
        this.date = etapeAcheminementEntity.getDate();
        this.pays = etapeAcheminementEntity.getPays();
        this.localisation = etapeAcheminementEntity.getLocalisation();
        this.description = etapeAcheminementEntity.getDescription();
        this.commentaire = etapeAcheminementEntity.getCommentaire();
        this.type = type;
    }
}
