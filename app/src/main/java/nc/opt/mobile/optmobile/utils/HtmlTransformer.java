package nc.opt.mobile.optmobile.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

import nc.opt.mobile.optmobile.domain.suiviColis.Colis;
import nc.opt.mobile.optmobile.domain.suiviColis.EtapeAcheminementDto;

/**
 * Created by 2761oli on 05/10/2017.
 */

public class HtmlTransformer {

    public static final int RESULT_NO_ITEM_FOUND = -1;
    public static final int RESULT_SUCCESS = 1;
    public static final String HTML_TAG_TABLE = "table";

    private HtmlTransformer() {
    }

    /**
     * Transform a html text into objects
     * <p>
     * If we find the keyword "objet introuvable" into a <p> then we send back null
     *
     * @param htmlToTransform
     * @param colis
     * @return RESULT_NO_ITEM_FOUND if no object found
     * RESULT_SUCCESS if everything's fine
     * @throws HtmlTransformerException
     */
    public static int getParcelResultFromHtml(final String htmlToTransform, Colis colis) throws HtmlTransformerException {
        Document document = Jsoup.parse(htmlToTransform);

        // Si on trouve la chaine "objet introuvable", on renvoie RESULT_NO_ITEM_FOUND
        Elements tableauP = document.select("p");
        for (int i = 1, l = tableauP.size(); i < l; i++) {
            if (tableauP.get(i).text().equals("objet introuvable")) {
                return RESULT_NO_ITEM_FOUND;
            }
        }

        if (document.select(HTML_TAG_TABLE).isEmpty()) {
            throw new HtmlTransformerException("Aucune balise table récupérée.");
        }

        // Positionnement sur le dernier élément <table> du document
        // C'est ici que son contenu les steps.
        int nbTable = document.select(HTML_TAG_TABLE).size();
        Element lastTable = document.select(HTML_TAG_TABLE).get(nbTable - 1);

        // Récupération de tous les éléments <tr>, les lignes du tableau
        Elements tableauTr = lastTable.getElementsByTag("tr");

        if (tableauTr.isEmpty()) {
            throw new HtmlTransformerException("Aucune balise <tr> dans le résultat");
        }

        ArrayList<EtapeAcheminementDto> listEtapeAcheminementDto = new ArrayList<>();

        // Parcours de tous les éléments <tr> pour trouver les colonnes <td>
        for (int i = 1, l = tableauTr.size(); i < l; i++) {

            Element ligneTr = tableauTr.get(i);
            Elements colonneTd = ligneTr.select("td");

            if (colonneTd.isEmpty()) {
                throw new HtmlTransformerException("Aucune balise <td> dans le résultat");
            }

            if (colonneTd.size() < 5) {
                throw new HtmlTransformerException("5 balises <td> par ligne <tr> requises");
            }

            // On ne prend pas les colonnes avec la classe tabmen
            // Ceux sont le colonne d'entête
            if (!colonneTd.hasClass("tabmen")) {
                String date = colonneTd.get(0).text();
                String pays = colonneTd.get(1).text();
                String localisation = colonneTd.get(2).text();
                String description = colonneTd.get(3).text();
                String commentaire = colonneTd.get(4).text();

                EtapeAcheminementDto etapeAcheminementDto = new EtapeAcheminementDto(date, pays, localisation, description, commentaire);
                listEtapeAcheminementDto.add(etapeAcheminementDto);
            }
        }

        colis.setEtapeAcheminementDtoArrayList(listEtapeAcheminementDto);
        return RESULT_SUCCESS;
    }

    /**
     * Personal Exception
     */
    public static class HtmlTransformerException extends Exception {
        HtmlTransformerException(String message) {
            super(message);
        }
    }
}