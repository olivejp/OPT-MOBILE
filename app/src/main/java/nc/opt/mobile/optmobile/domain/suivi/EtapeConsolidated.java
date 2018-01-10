package nc.opt.mobile.optmobile.domain.suivi;

import nc.opt.mobile.optmobile.database.entity.EtapeEntity;

/**
 * Created by 2761oli on 04/12/2017.
 */

public class EtapeConsolidated extends EtapeEntity {

    public enum TypeEtape {
        HEADER(0),
        DETAIL(1);

        private final int value;

        private TypeEtape(int value){
            this.value = value;
        }

        public int getTypeValue(){
            return this.value;
        }
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
