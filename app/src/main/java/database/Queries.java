package database;


public class Queries
{
    // Queries for SongDao
    public final static String getAllSongs = "SELECT * FROM songs";
    public final static String getSongForId = "SELECT * FROM songs WHERE number = :id";
    public final static String getGuessedSongs = "SELECT * FROM songs WHERE guessed=1";
    public final static String nukeSongsDB = "DELETE FROM songs WHERE 1 = 1";
    public final static String getRandomSong = "SELECT * FROM songs WHERE guessed=0 ORDER BY RANDOM() LIMIT 1";



    // Queries for LocationDao
    public final static String getAllLocations =
            "SELECT * FROM locations where map_number = :map_number LIMIT 10";
    public final static String getUndiscoveredLocations =
            "SELECT * FROM locations WHERE discovered=0 AND available=1 AND map_number = :map_number AND songId = :id";
    public final static String countUndiscoveredLocations =
            "SELECT COUNT(*) FROM locations WHERE discovered=0 AND available=1";
    public final static String nukeLocationsDB = "DELETE FROM locations WHERE 1 = 1";
    public final static String getGuessedWords = "SELECT * FROM locations WHERE discovered=1 AND songId = :id";
    public final static String countByCategory = "SELECT category, COUNT(*) FROM locations WHERE discovered = 0 AND songId= :id GROUP BY category";
    public final static String getRandomLocations =
            "SELECT * FROM locations WHERE discovered=0 AND available=1 AND songId = :id AND category IN (:cats) ORDER BY RANDOM()";
    public final static String getDiscoveredLocations =
            "SELECT * FROM locations WHERE discovered = 1";



    // Queries for MarkerDao
    public final static String getMarkerForCategory =
            "SELECT * FROM markers WHERE category = :category ";
    public final static String getAllMarkers =
            "SELECT * FROM markers";

}
