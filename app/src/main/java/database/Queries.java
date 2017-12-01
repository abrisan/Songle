package database;


public class Queries
{
    // Queries for SongDao
    public final static String getAllSongs = "SELECT * FROM songs";
    public final static String getSongForId = "SELECT * FROM songs WHERE number = :id";
    public final static String getGuessedSongs = "SELECT * FROM songs WHERE guessed=1";
    public final static String nukeSongsDB = "DELETE FROM songs WHERE 1 = 1";


    // Queries for LocationDao
    public final static String getUndiscoveredLocations =
            "SELECT * FROM locations WHERE discovered=0 AND available=1";
    public final static String countUndiscoveredLocations =
            "SELECT COUNT(*) FROM locations WHERE discovered=0 AND available=1";
    public final static String nukeLocationsDB = "DELETE FROM locations WHERE 1 = 1";

    // Queries for MarkerDao
    public final static String getMarkerForCategory =
            "SELECT * FROM markers WHERE category = :category ";
    public final static String getAllMarkers =
            "SELECT * FROM markers";

}
