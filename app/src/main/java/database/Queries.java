package database;


public class Queries
{
    public final static String getAllSongs = "SELECT * FROM songs";
    public final static String getSongForId = "SELECT * FROM songs WHERE number = :id";
}
