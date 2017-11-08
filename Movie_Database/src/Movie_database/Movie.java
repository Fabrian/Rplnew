/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Movie_database;

import java.util.ArrayList;

/**
 *
 * @author Fabrian
 */
public class Movie {
    private String judul;
    private String sutradara;
    private String studio;
    private ArrayList<String> genres;
    private int peringkat;
    private int tahunrelease;
    private String sinopsis;
    private String status;
    private String pathSampul;
    
    Movie(String judul,String sutradara, String studio, ArrayList<String> genres
        ,int peringkat, int tahunrelease, String sinopsis, String status,String pathSampul){
        this.judul = judul;
        this.sutradara = sutradara;
        this.studio = studio;
        this.genres = genres;
        this.peringkat = peringkat;
        this.tahunrelease = tahunrelease;
        this.sinopsis = sinopsis;
        this.status = status;
        this.pathSampul = pathSampul;
    }
    
    public String getTranslatedStatus(){
        switch(this.getStatus()){
            
            case "2": return "Sudah";
            case "0": 
            default: return "Belum";
        }
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
     * @return the studio
     */
    public String getStudio() {
        return studio;
    }

    /**
     * @param studio the studio to set
     */
    public void setStudio(String studio) {
        this.studio = studio;
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
     * @return the peringkat
     */
    public int getPeringkat() {
        return peringkat;
    }

    /**
     * @param peringkat the peringkat to set
     */
    public void setPeringkat(int peringkat) {
        this.peringkat = peringkat;
    }

    /**
     * @return the tahunrelease
     */
    public int getTahunrelease() {
        return tahunrelease;
    }

    /**
     * @param tahunrelease the tahunrelease to set
     */
    public void setTahunrelease(int tahunrelease) {
        this.tahunrelease = tahunrelease;
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
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status;
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

    /**
     * @return the pengarang
     */
}