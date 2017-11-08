/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Movie_database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 *
 * @author Fabrian
 */
public class DatabaseUtil {
    
    
    /**
     * 
     * @return the Connection object
     */
    public static Connection connect() {
        // SQLite connection string
        String url = "jdbc:sqlite:database/movie.db";
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }
    
    public static ArrayList<Movie> selectSemuaMovie(){
        ArrayList<Movie> movies = new ArrayList<>();
        
        String sqlSelectMovie = "SELECT * FROM movie ORDER BY 1;";
        try (Connection conn = connect();
             Statement stmtSelectMovie  = conn.createStatement();
             ResultSet rsSelectMovie = stmtSelectMovie.executeQuery(sqlSelectMovie)){
            
            // loop through the result set            
            while (rsSelectMovie.next()) {
                //ambil genre
                String judul = rsSelectMovie.getString("judul");
                String sutradara = rsSelectMovie.getString("sutradara");
                
                ArrayList<String> genres = new ArrayList<>();
                String sqlSelectGenre = "SELECT nama_genre, "
                        + "urutan_masuk "
                        + "FROM daftar_genre "
                        + "WHERE judul = \""+ judul + "\" AND "
                        + "sutradara = \""+ sutradara + "\""
                        + "ORDER BY 2;";
                
                Statement stmtSelectGenre  = conn.createStatement();
                ResultSet rsSelectGenre = stmtSelectGenre.executeQuery(sqlSelectGenre);
                while (rsSelectGenre.next()) {
                    genres.add(rsSelectGenre.getString("nama_genre"));
                }
                
                movies.add(new Movie(judul, 
                                   sutradara,
                                   rsSelectMovie.getString("studio"),
                                   genres,
                                   rsSelectMovie.getInt("peringkat"),
                                   rsSelectMovie.getInt("tahun_release"),
                                   rsSelectMovie.getString("sinopsis"),
                                   rsSelectMovie.getString("status"),
                                   rsSelectMovie.getString("path_sampul")
                                   ));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        
        return movies;
    }
    
    public static Movie selectMovie(String judul, String sutradara){
        Movie movie = null;
        String sqlSelectMovie = "SELECT * FROM movie WHERE judul = ? AND sutradara = ?;";
        try (Connection conn = connect();
            PreparedStatement pstmtSelectMovie  = conn.prepareStatement(sqlSelectMovie)){
            pstmtSelectMovie.setString(1,judul);
            pstmtSelectMovie.setString(2, sutradara);
            
            ResultSet rsSelectMovie = pstmtSelectMovie.executeQuery();
            // loop through the result set            
            while (rsSelectMovie.next()) {
                
                ArrayList<String> genres = new ArrayList<>();
                String sqlSelectGenre = "SELECT nama_genre, "
                        + "urutan_masuk "
                        + "FROM daftar_genre "
                        + "WHERE judul = \""+ judul + "\" AND "
                        + "sutradara = \""+ sutradara + "\""
                        + "ORDER BY 2;";
                
                Statement stmtSelectGenre  = conn.createStatement();
                ResultSet rsSelectGenre = stmtSelectGenre.executeQuery(sqlSelectGenre);
                while (rsSelectGenre.next()) {
                    genres.add(rsSelectGenre.getString("nama_genre"));
                }
                
                movie = new Movie(judul, 
                                   sutradara,
                                   rsSelectMovie.getString("studio"),
                                   genres,
                                   rsSelectMovie.getInt("peringkat"),
                                   rsSelectMovie.getInt("tahun_release"),
                                   rsSelectMovie.getString("sinopsis"),
                                   rsSelectMovie.getString("status"),
                                   rsSelectMovie.getString("path_sampul")
                                   );
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return movie;
    }
    
    public static ArrayList<String> selectSemuaGenre(){
        ArrayList<String> genres = new ArrayList<>();

        String sqlSelectGenre = "SELECT * FROM genre ORDER BY 1;";
        try (Connection conn = connect();
            Statement stmtSelectGenre  = conn.createStatement();
            ResultSet rsSelectGenre = stmtSelectGenre.executeQuery(sqlSelectGenre)){
            // loop through the result set            
            while (rsSelectGenre.next()) {
                String genre = rsSelectGenre.getString("nama_genre");
                
                genres.add(genre);
            }
            
        }
        catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return genres;
    }   
    public static boolean createMovie(String judul, String sutradara, String studio,
            ArrayList<String> genres, int peringkat, int tahunrelease, String sinopsis,
            String status, String pathSampul){
        String sqlInsertMovie = "INSERT INTO movie(judul,sutradara,studio,peringkat,"
                + "tahun_release,sinopsis,status,path_sampul) VALUES(?,?,?,?,?,?,?,?);";
 
        try (Connection conn = connect();
            PreparedStatement pstmtInsertMovie = conn.prepareStatement(sqlInsertMovie)) {
            pstmtInsertMovie.setString(1, judul);
            pstmtInsertMovie.setString(2, sutradara);
            if(studio.equals("Studio")) pstmtInsertMovie.setString(3, "");
            else pstmtInsertMovie.setString(3, studio);
            if(peringkat <= 0 || peringkat > 10) pstmtInsertMovie.setInt(4, 0);
            else pstmtInsertMovie.setInt(4, peringkat);
            if(tahunrelease == 0) pstmtInsertMovie.setString(5, "");
            else pstmtInsertMovie.setInt(5, tahunrelease);
            if(sinopsis.equals("Sinopsis . . .")) pstmtInsertMovie.setString(6, "");
            else pstmtInsertMovie.setString(6, sinopsis);
            pstmtInsertMovie.setString(7, status);
            if(pathSampul.equals("")) pstmtInsertMovie.setString(8, "defaultImage.jpg");
            else pstmtInsertMovie.setString(8, pathSampul);
            int count = pstmtInsertMovie.executeUpdate();
            
            if(count > 0){
                if(genres.size() > 0){
                    String sqlInsertGenre = "INSERT INTO daftar_genre(judul,sutradara,nama_genre,urutan_masuk)"
                            + " VALUES(?,?,?,?);";
                    PreparedStatement pstmtInsertGenre = conn.prepareStatement(sqlInsertGenre);
                    pstmtInsertGenre.setString(1, judul);
                    pstmtInsertGenre.setString(2, sutradara);
                    int i = 1;

                    boolean isSuccessStatus = true;
                    for(String genre: genres){
                        pstmtInsertGenre.setString(3, genre);
                        pstmtInsertGenre.setInt(4, i);
                        
                        int count2 = pstmtInsertGenre.executeUpdate();
                        if(count2 <= 0){
                            isSuccessStatus = false;
                        } 
                        i++;
                    }
                    if(isSuccessStatus){
                        return true;
                    }
                }
                else{
                    return true;
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    public static boolean deleteMovie(String judul, String sutradara) {
        String sqlDeleteMovie = "DELETE FROM movie WHERE judul = ? AND  sutradara = ?;";
        try(Connection conn = connect();
            PreparedStatement pstmtDeleteMovie = conn.prepareStatement(sqlDeleteMovie)) {
            pstmtDeleteMovie.setString(1, judul);
            pstmtDeleteMovie.setString(2, sutradara);
            
            int count = pstmtDeleteMovie.executeUpdate();
            
            if(count > 0){
                String sqlDeleteGenreBuku = 
                        "DELETE FROM daftar_genre WHERE judul = ? AND  sutradara = ?;";
                
                PreparedStatement pstmtDeleteGenreBuku = 
                        conn.prepareStatement(sqlDeleteGenreBuku);
                pstmtDeleteGenreBuku.setString(1, judul);
                pstmtDeleteGenreBuku.setString(2, sutradara);
                int count2 = pstmtDeleteGenreBuku.executeUpdate();
                if(count2 > -1) return true;
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        
        return false;
    }

    static boolean editMovie(String judul, String sutradara, String studio, ArrayList<String> genres, int peringkat, int tahunrelease, String sinopsis, String status, String pathSampul) {
        String sqlUpdateMovie = "UPDATE movie SET studio = ?, peringkat = ?,"
                + " tahun_release = ?, sinopsis = ?, status = ?, path_sampul= ?"
                + " WHERE judul = ? AND sutradara = ?;";
 
        try (Connection conn = connect();
            PreparedStatement pstmtUpdateMovie = conn.prepareStatement(sqlUpdateMovie)) {
            if(studio.equals("studio")) pstmtUpdateMovie.setString(1, "");
            else pstmtUpdateMovie.setString(1, studio);
            if(peringkat <= 0 || peringkat > 10) pstmtUpdateMovie.setInt(2, 0);
            else pstmtUpdateMovie.setInt(2, peringkat);
            if(tahunrelease == 0) pstmtUpdateMovie.setString(3, "");
            else pstmtUpdateMovie.setInt(3, tahunrelease);
            if(sinopsis.equals("Sinopsis . . .")) pstmtUpdateMovie.setString(4, "");
            else pstmtUpdateMovie.setString(4, sinopsis);
            pstmtUpdateMovie.setString(5, status);
            if(pathSampul.equals("")) pstmtUpdateMovie.setString(6, "defaultImage.jpg");
            else pstmtUpdateMovie.setString(6, pathSampul);
            pstmtUpdateMovie.setString(7, judul);
            pstmtUpdateMovie.setString(8, sutradara);
            
            int count = pstmtUpdateMovie.executeUpdate();
            
            if(count > 0){
                if(genres.size() > 0){
                    String sqlDeleteGenre = "DELETE from daftar_genre WHERE "
                            + "judul = ? AND sutradara = ?;";
                    PreparedStatement pstmtDeleteGenre = 
                            conn.prepareStatement(sqlDeleteGenre);
                    pstmtDeleteGenre.setString(1, judul);
                    pstmtDeleteGenre.setString(2, sutradara);
                    
                    int count3 = pstmtDeleteGenre.executeUpdate();
                    
                    if(count3 > 0){
                        String sqlInsertGenre = "INSERT INTO daftar_genre(judul,"
                                + "sutradara,nama_genre,urutan_masuk) "
                                + "VALUES(?,?,?,?);";
                        PreparedStatement pstmtInsertGenre = 
                                conn.prepareStatement(sqlInsertGenre);
                        pstmtInsertGenre.setString(1, judul);
                        pstmtInsertGenre.setString(2, sutradara);
                        int i = 1;

                        boolean isSuccessStatus = true;
                        for(String genre: genres){
                            pstmtInsertGenre.setString(3, genre);
                            pstmtInsertGenre.setInt(4, i);

                            int count2 = pstmtInsertGenre.executeUpdate();
                            if(count2 < 0){
                                isSuccessStatus = false;
                            } 
                            i++;
                        }
                        if(isSuccessStatus){
                            return true;
                        }
                    }
                }
                else{
                    return true;
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }
}
