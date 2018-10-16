package nc.opt.mobile.optmobile.job.opt;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

import nc.opt.mobile.optmobile.Utilities;

/**
 * Created by 2761oli on 05/10/2017.
 * <p>
 * Transform a html document into a ColisDto
 * This class is only used with the OPT Network call
 */
public class HtmlTransformer {

    public static final int RESULT_NO_ITEM_FOUND = -1;
    public static final int RESULT_SUCCESS = 1;
    private static final String HTML_TAG_TABLE = "table";

    private HtmlTransformer() {
    }

    /**
     * Transform a html text into objects
     * <p>
     * If we find the keyword "objet introuvable" into a <p> then we send back null
     *
     * @param htmlToTransform
     * @param colisDto
     * @return RESULT_NO_ITEM_FOUND if no object found
     * RESULT_SUCCESS if everything's fine
     * @throws HtmlTransformerException
     */
    public static int getColisFromHtml(final String htmlToTransform, ColisDto colisDto) throws HtmlTransformerException {
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

        ArrayList<EtapeDto> listEtapeDto = new ArrayList<>();

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
                String status = getCorrespondingStatus(description);

                EtapeDto etapeDto = new EtapeDto(date, pays, localisation, description, commentaire, status);
                listEtapeDto.add(etapeDto);
            }
        }

        colisDto.setEtapeDtoArrayList(listEtapeDto);
        return RESULT_SUCCESS;
    }

    /**
     * Retrieve the step status corresponding on the description
     *
     * @param description
     * @return String that the method {@link Utilities#getStatusDrawable}
     * could transform in Drawable
     */
    private static String getCorrespondingStatus(String description) {
        String status;
        if (description.equals("Envoi distribué (Ent)")) {
            status = "Delivered";
        } else {
            status = "Pending";
        }
        return status;
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