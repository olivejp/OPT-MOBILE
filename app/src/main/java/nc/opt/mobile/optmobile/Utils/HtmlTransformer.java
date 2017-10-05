package nc.opt.mobile.optmobile.Utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

import nc.opt.mobile.optmobile.domain.ParcelSearchResult;
import nc.opt.mobile.optmobile.domain.StepParcelSearch;

/**
 * Created by 2761oli on 05/10/2017.
 */

public class HtmlTransformer {

    public static final String TAG = HtmlTransformer.class.getName();

    public static ParcelSearchResult transform(final String htmlToTransform, final String idParcel) {
        Document document = Jsoup.parse(htmlToTransform);

        ArrayList<StepParcelSearch> listStepParcelSearch = new ArrayList<>();

        // Positionnement sur le dernier élément <table> du document
        // C'est ici que son contenu les steps.
        Element lastTable = document.select("table").get(document.select("table").size() - 1);

        // Récupération de tous les éléments <tr>, les lignes du tableau
        Elements tableauTr = lastTable.getElementsByTag("tr");

        // Parcours de tous les éléments <tr> pour trouver les colonnes <td>
        for (int i = 1, l = tableauTr.size(); i < l; i++) {

            Element ligneTr = tableauTr.get(i);
            Elements colonneTd = ligneTr.select("td");

            // On ne prend pas les colonnes avec la classe tabmen
            // Ceux sont le colonne d'entête
            if (!colonneTd.hasClass("tabmen")) {
                String date = colonneTd.get(0).text();
                String pays = colonneTd.get(1).text();
                String localisation = colonneTd.get(2).text();
                String description = colonneTd.get(3).text();
                String commentaire = colonneTd.get(4).text();

                StepParcelSearch stepParcelSearch = new StepParcelSearch(date, pays, localisation, description, commentaire);
                listStepParcelSearch.add(stepParcelSearch);
            }
        }

        return new ParcelSearchResult(idParcel, listStepParcelSearch);
    }
}