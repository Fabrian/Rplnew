/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package movie.database;

import java.util.ArrayList;
/**
 *
 * @author Fabrian
 */
public class Movie {
    private String judul;
    private String sutradara;
    private ArrayList<String> genres;
    private int Releasedate;
    private String sinopsis;
    private String pathSampul;
    
    Movie(String judul,String sutradara, ArrayList<String> genres
        ,int Releasedate, String sinopsis,String pathSampul){
        this.judul = judul;
        this.sutradara = sutradara;
        this.genres = genres;
        this.sinopsis = sinopsis;
        this.Releasedate = Releasedate;
        this.pathSampul = pathSampul;
    }

    /**
     * @return the judul
     */
    public String getJudul() {
        return judul;
    }

    /**
     * @param judul the judul to set
     */
    public void setJudul(String judul) {
        this.judul = judul;
    }

    /**
     * @return the sutradara
     */
    public String getSutradara() {
        return sutradara;
    }

    /**
     * @param sutradara the sutradara to set
     */
    public void setSutradara(String sutradara) {
        this.sutradara = sutradara;
    }

    /**
     * @return the genres
     */
    public ArrayList<String> getGenres() {
        return genres;
    }

    /**
     * @param genres the genres to set
     */
    public void setGenres(ArrayList<String> genres) {
        this.genres = genres;
    }

    /**
     * @return the Releasedate
     */
    public int getReleasedate() {
        return Releasedate;
    }

    /**
     * @param Releasedate the Releasedate to set
     */
    public void setReleasedate(int Releasedate) {
        this.Releasedate = Releasedate;
    }

    /**
     * @return the sinopsis
     */
    public String getSinopsis() {
        return sinopsis;
    }

    /**
     * @param sinopsis the sinopsis to set
     */
    public void setSinopsis(String sinopsis) {
        this.sinopsis = sinopsis;
    }

    /**
     * @return the pathSampul
     */
    public String getPathSampul() {
        return pathSampul;
    }

    /**
     * @param pathSampul the pathSampul to set
     */
    public void setPathSampul(String pathSampul) {
        this.pathSampul = pathSampul;
    }
    
    
    
}
