package Partiel2018.jpantin.diiage.org;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import Partiel2018.jpantin.diiage.org.Models.Release;

public class DBHelper extends SQLiteOpenHelper {

    /**
     * Le numéro de la version en cours
     */
    private static final int VERSION = 2;

    /**
     * Le nom de la table contenant les releases
     */
    public static final String NAME_TABLE_RELEASE = "[RELEASE]";

    /**
     * Le nom de la table contenant les artistes
     */
    public static final String NAME_TABLE_ARTIST = "ARTIST";

    /**
     * Une liste de releases
     * Utilisé lors de la première version de la base de données.
     */
    private static ArrayList<Release> releases;

    /**
     * Initialisation du db helper
     * @param context le context en cours.
     */
    public DBHelper(Context context)
    {
        super(context, "partiel2018", null, VERSION);
    }

    /**
     * Initialisation de l'helper avec une liste de données pour remplir la base
     * @param context le context en cours
     * @param releases la liste des releases par défaut (pour la primière version)
     */
    public DBHelper(Context context, ArrayList<Release> releases)
    {
        super(context, "partiel2018", null, VERSION);
        this.releases = releases;
    }

    /**
     * A la création de la base (Si la base n'es pas déjà créée)
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        for (int i = 1; i <= VERSION; i++)
        {
            upgradeTo(db, i);
        }
    }

    /**
     * Mise à jour de la base lorsque la version est trop vieilles.
     * @param db la base de données
     * @param oldVersion Version de l'utilisateur
     * @param newVersion Version actuelles
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        // Mise à jour de tous les niveaux de versions manquantes
        for (int i = oldVersion + 1; i <= newVersion; i++)
        {
            upgradeTo(db, i);
        }
    }

    /**
     * Update la base selon la version voulue.
     * @param db la base de données.
     * @param numVersion le numéro de version.
     */
    private void upgradeTo(SQLiteDatabase db, int numVersion) {
        switch (numVersion)
        {
            // Version 1
            case 1 :
                // Création de la table "Release".
                db.execSQL("CREATE TABLE " + NAME_TABLE_RELEASE + " ( 'id' INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "'title' TEXT, " +
                        "'status' TEXT, " +
                        "'thumb' TEXT, " +
                        "'format' TEXT, " +
                        "'catno' TEXT, " +
                        "'resource_url' TEXT, " +
                        "'artist' TEXT, " +
                        "'year' INTEGER )");

                // Insertion de toutes les releases
                for(Release release : this.releases)
                {
                    db.insert(NAME_TABLE_RELEASE, null, release.toContentValues());
                }

                break;

            // Version 2
            case 2 :
                // Création de la table "Artist".
                db.execSQL("CREATE TABLE " + NAME_TABLE_ARTIST + " ( 'id' INTEGER PRIMARY KEY AUTOINCREMENT, 'nom' TEXT)");
                break;
        }
    }
}
