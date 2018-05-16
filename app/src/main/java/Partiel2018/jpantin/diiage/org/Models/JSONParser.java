package Partiel2018.jpantin.diiage.org.Models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

public class JSONParser
{
    /**
     * Convertir un objet jsonObject en objet Realease.
     * @param jsonObject Données contenant les informations de la release.
     * @return un objet Release contenant les informations récupérées depuis le JSONObject.
     * @throws JSONException
     */
    public Release JsonToRelease(JSONObject jsonObject) throws JSONException
    {
        Release release = new Release();
        release.setId(jsonObject.getInt("id"));
        release.setStatus(jsonObject.getString("status"));
        release.setThumb(jsonObject.getString("thumb"));
        release.setFormat(jsonObject.getString("format"));
        release.setTitle(jsonObject.getString("title"));
        release.setCatno(jsonObject.getString("catno"));
        release.setYear(jsonObject.optInt("year"));
        release.setResourceUrl(jsonObject.getString("resource_url"));
        release.setArtist(jsonObject.getString("artist"));

        return release;
    }
}
