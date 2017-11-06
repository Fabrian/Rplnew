/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package movie.database;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.showMessageDialog;
/**
 *
 * @author Fabrian
 */
public class connect {
    static Connection conn = null;
     public static Connection getConnectDB(){
        
        String path="jdbc:sqlite:D:/Rpl/movie_db.db";
        if(conn == null){
        try{
            conn=DriverManager.getConnection(path);
        }
        catch(SQLException e){
            showMessageDialog(null,"Koneksi ke database gagal!","Error",JOptionPane.ERROR_MESSAGE);
        }
        }
        return conn;
    }
     public static ArrayList<Movie> selectSemuaMovie(){
        ArrayList<Movie> movies = new ArrayList<>();
        
        String sqlSelectmovie = "SELECT * FROM movie ORDER BY 1;";
        try (Connection conn = getConnectDB();
             Statement stmtSelectMovie  = conn.createStatement();
             ResultSet rsSelectmovie = stmtSelectMovie.executeQuery(sqlSelectmovie)){
            
            // loop through the result set            
            while (rsSelectmovie.next()) {
                //ambil genre
                String judul = rsSelectmovie.getString("Judul");
                String sutradara = rsSelectmovie.getString("Sutradara");
                
                ArrayList<String> genres = new ArrayList<>();
                String sqlSelectGenre = "SELECT genre, "
                        + "urutan "
                        + "FROM movie_genre "
                        + "WHERE Judul = \""+ judul + "\" AND "
                        + "Sutradara = \""+ sutradara + "\""
                        + "ORDER BY 2;";
                
                Statement stmtSelectGenre  = conn.createStatement();
                ResultSet rsSelectGenre = stmtSelectGenre.executeQuery(sqlSelectGenre);
                while (rsSelectGenre.next()) {
                    genres.add(rsSelectGenre.getString("genre"));
                }
                
                movies.add(new Movie(judul, 
                                   sutradara,
                                   genres, 
                                   rsSelectmovie.getInt("tahun_terbit"),
                                   rsSelectmovie.getString("sinopsis"),
                                   rsSelectmovie.getString("path_cover")
                                   ));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        
        return movies;
    }
    
    public static Movie selectMovie(String judul, String sutradara){
        Movie movie = null;
        String sqlSelectMovie = "SELECT * FROM movie WHERE Judul = ? AND Sutradara = ?;";
        try (Connection conn = getConnectDB();
            PreparedStatement pstmtSelectMovie  = conn.prepareStatement(sqlSelectMovie)){
            pstmtSelectMovie.setString(1,judul);
            pstmtSelectMovie.setString(2, sutradara);
            
            ResultSet rsSelectMovie = pstmtSelectMovie.executeQuery();
            // loop through the result set            
            while (rsSelectMovie.next()) {
                
                ArrayList<String> genres = new ArrayList<>();
                String sqlSelectGenre = "SELECT genre, "
                        + "urutan_masuk "
                        + "FROM movie_genre "
                        + "WHERE Judul = \""+ judul + "\" AND "
                        + "pengarang = \""+ sutradara + "\""
                        + "ORDER BY 2;";
                
                Statement stmtSelectGenre  = conn.createStatement();
                ResultSet rsSelectGenre = stmtSelectGenre.executeQuery(sqlSelectGenre);
                while (rsSelectGenre.next()) {
                    genres.add(rsSelectGenre.getString("genre"));
                }
                
                movie = new Movie(judul, 
                                   sutradara,
                                   
                                   genres,
                                   
                                   rsSelectMovie.getInt("tahun_release"),
                                   rsSelectMovie.getString("sinopsis"),
                                   
                                   rsSelectMovie.getString("path_cover")
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
        try (Connection conn = getConnectDB();
            Statement stmtSelectGenre  = conn.createStatement();
            ResultSet rsSelectGenre = stmtSelectGenre.executeQuery(sqlSelectGenre)){
            // loop through the result set            
            while (rsSelectGenre.next()) {
                String genre = rsSelectGenre.getString("genre");
                
                genres.add(genre);
            }
            
        }
        catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return genres;
    }   
    public static boolean createMovie(String judul, String sutradara,
            ArrayList<String> genres, int releasedate, String sinopsis,
             String pathSampul){
        String sqlInsertBuku = "INSERT INTO movie(Judul,Sutradara,penerbit"
                + "tahun_release,sinopsis,path_cover) VALUES(?,?,?,?,?,?,?,?);";
 
        try (Connection conn = getConnectDB();
            PreparedStatement pstmtInsertMovie = conn.prepareStatement(sqlInsertBuku)) {
            pstmtInsertMovie.setString(1, judul);
            pstmtInsertMovie.setString(2, sutradara);
            if(releasedate == 0) pstmtInsertMovie.setString(3, "");
            else pstmtInsertMovie.setInt(3, releasedate);
            if(sinopsis.equals("Sinopsis . . .")) pstmtInsertMovie.setString(4, "");
            else pstmtInsertMovie.setString(4, sinopsis);
            
            if(pathSampul.equals("")) pstmtInsertMovie.setString(5, "defaultImage.jpg");
            else pstmtInsertMovie.setString(5, pathSampul);
            int count = pstmtInsertMovie.executeUpdate();
            
            if(count > 0){
                if(genres.size() > 0){
                    String sqlInsertGenre = "INSERT INTO movie_genre(Judul,Sutradara,genre,urutan)"
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
        String sqlDeleteMovie = "DELETE FROM movie WHERE Judul = ? AND  Sutradara = ?;";
        try(Connection conn = getConnectDB();
            PreparedStatement pstmtDeleteMovie = conn.prepareStatement(sqlDeleteMovie)) {
            pstmtDeleteMovie.setString(1, judul);
            pstmtDeleteMovie.setString(2, sutradara);
            
            int count = pstmtDeleteMovie.executeUpdate();
            
            if(count > 0){
                String sqlDeleteGenreMovie = 
                        "DELETE FROM movie_genre WHERE Judul = ? AND  Sutradara = ?;";
                
                PreparedStatement pstmtDeleteGenreBuku = 
                        conn.prepareStatement(sqlDeleteGenreMovie);
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

    static boolean editBuku(String judul, String sutradara, ArrayList<String> genres, int releasedate, String sinopsis, String pathSampul) {
        String sqlUpdateMovie = "UPDATE movie SET tahun_release = ?, sinopsis = ?,"
                + "  path_cover = ?"
                + " WHERE Judul = ? AND Sutradara = ?;";
 
        try (Connection conn = getConnectDB();
            PreparedStatement pstmtUpdateMovie = conn.prepareStatement(sqlUpdateMovie)) {
            if(sutradara.equals("Sutradara")) pstmtUpdateMovie.setString(1, "");
            else pstmtUpdateMovie.setString(1, sutradara);
           
            if(releasedate == 0) pstmtUpdateMovie.setString(3, "");
            else pstmtUpdateMovie.setInt(3, releasedate);
            if(sinopsis.equals("Sinopsis . . .")) pstmtUpdateMovie.setString(4, "");
            else pstmtUpdateMovie.setString(4, sinopsis);
          
            if(pathSampul.equals("")) pstmtUpdateMovie.setString(6, "defaultImage.jpg");
            else pstmtUpdateMovie.setString(6, pathSampul);
            pstmtUpdateMovie.setString(7, judul);
            pstmtUpdateMovie.setString(8, sutradara);
            
            int count = pstmtUpdateMovie.executeUpdate();
            
            if(count > 0){
                if(genres.size() > 0){
                    String sqlDeleteGenre = "DELETE from movie_genre WHERE "
                            + "Judul = ? AND Sutradara= ?;";
                    PreparedStatement pstmtDeleteGenre = 
                            conn.prepareStatement(sqlDeleteGenre);
                    pstmtDeleteGenre.setString(1, judul);
                    pstmtDeleteGenre.setString(2, sutradara);
                    
                    int count3 = pstmtDeleteGenre.executeUpdate();
                    
                    if(count3 > 0){
                        String sqlInsertGenre = "INSERT INTO daftar_genre(judul,"
                                + "Sutradara,genre,urutan) "
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

