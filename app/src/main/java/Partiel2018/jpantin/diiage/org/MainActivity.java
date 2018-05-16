package Partiel2018.jpantin.diiage.org;

import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import Partiel2018.jpantin.diiage.org.Models.JSONParser;
import Partiel2018.jpantin.diiage.org.Models.Release;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Url de l'api stocker dans le fichier de ressources.
        String baseUrlApi = getResources().getString(R.string.base_url_api);

        //Creation d'une l'url via le string récupéré
        URL baseUrlPlaylist = null;
        try {
            // Url qui permet de récupérer les releases
            baseUrlPlaylist = new URL(baseUrlApi);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        AsyncTask<URL, Integer, ArrayList<Release>> task = new AsyncTask<URL, Integer, ArrayList<Release>>() {
            @Override
            protected ArrayList<Release> doInBackground(URL... urls)
            {
                // La liste des releases qui vont être récupérées
                ArrayList<Release> releases = new ArrayList<>();

                try {
                    InputStream inputStream = urls[0].openStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                    // Initialisation d'un StringBuilder pour stocker le contenu distant
                    StringBuilder stringBuilder = new StringBuilder();
                    String lineBuffer = null;
                    while ((lineBuffer = bufferedReader.readLine()) != null)
                    {
                        stringBuilder.append(lineBuffer);
                    }

                    String data = stringBuilder.toString();
                    JSONArray jsonArray = new JSONArray(data);

                    // Récupère toutes les releases retourné par l'api
                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        JSONParser jsonParser = new JSONParser();
                        releases.add(jsonParser.JsonToRelease(jsonObject));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return releases;
            }

            /**
             * Charge les données à la vue.
             * @param releases les releases sur lesquels effectuer le traitement
             */
            @Override
            protected void onPostExecute(ArrayList<Release> releases) {
                super.onPostExecute(releases);

                // On remplie la ListView
                ListView listView = findViewById(R.id.listViewReleases);

                // La ListView utile un temlate existant par défaut, elle vas contenir le titre de la release (cf méthode toString de la classe Release).
                listView.setAdapter(new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, releases));

                // Initialisation du helper avec les données récupérées via l'api
                DBHelper helper = new DBHelper(MainActivity.this, releases);

                // Gestion de la base de données.
                SQLiteDatabase db = helper.getWritableDatabase();
                db.close();

            }
        }.execute(baseUrlPlaylist);
    }
}
