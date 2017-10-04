package nc.opt.mobile.optmobile.fragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Created by 2761oli on 05/10/2017.
 */

public class HtmlTransformer {

    public static final String SAMPLE_HTML = "<table cellspacing=\"0\" style=\"height: 24px;\">\r\n<tr class=\"tr-hover\">\r\n<th rowspan=\"15\" scope=\"row\">Network</th>\r\n<td class=\"ttl\"><a href=\"network-bands.php3\">Technology</a></td>\r\n<td class=\"nfo\"><a href=\"#\" class=\"link-network-detail collapse\">GSM</a></td>\r\n</tr>\r\n<tr class=\"tr-toggle\">\r\n<td class=\"ttl\"><a href=\"network-bands.php3\">2G bands</a></td>\r\n<td class=\"nfo\">GSM 900 / 1800 - SIM 1 & SIM 2</td>\r\n</tr>   \r\n<tr class=\"tr-toggle\">\r\n<td class=\"ttl\"><a href=\"glossary.php3?term=gprs\">GPRS</a></td>\r\n<td class=\"nfo\">Class 12</td>\r\n</tr>   \r\n<tr class=\"tr-toggle\">\r\n<td class=\"ttl\"><a href=\"glossary.php3?term=edge\">EDGE</a></td>\r\n<td class=\"nfo\">Yes</td>\r\n</tr>\r\n</table>";

    public static void transform(final String htmlToTransform) {
        Document document = Jsoup.parse(htmlToTransform);

        Element table = document.select("table").first();
        String arrayName = table.select("th").first().text();

        JSONObject jsonObj = new JSONObject();
        JSONArray jsonArr = new JSONArray();

        Elements ttls = table.getElementsByClass("ttl");
        Elements nfos = table.getElementsByClass("nfo");
        JSONObject jo = new JSONObject();
        for (int i = 0, l = ttls.size(); i < l; i++)
        {
            String key = ttls.get(i).text();
            String value = nfos.get(i).text();
            try {
                jo.put(key, value);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        jsonArr.put(jo);
        try {
            jsonObj.put(arrayName, jsonArr);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println(jsonObj.toString());
    }
}