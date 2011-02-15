/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.natty.parser.files;

import org.apache.log4j.Logger;


/**
 *
 * @author zayac
 */
public class MusicFile extends SimpleFile{
    private static final Logger log = Logger.getLogger(MusicFile.class.getName());
    private static final String MUSIC_PATTERN = "^(.+)?/(.+)?\\.(mp3|flac|wav|ogg)?$";
    private String track = null;
    private String artist = null;
    private String genre = null;
    private String album = null;

    public MusicFile(String fullPath)
    {
        super(fullPath, MUSIC_PATTERN);
    }

    public String getTrack()
    {
        return track;
    }

    public String getArtist()
    {
        return artist;
    }

    public String getGenre()
    {
        return genre;
    }

    public String getAlbum()
    {
        return album;
    }

    public void setTrack(String track)
    {
        this.track = track;
    }

    public void setArtist(String artist)
    {
        this.artist = artist;
    }

    public void setGenre(String genre)
    {
        this.genre = genre;
    }

    public void setAlbum(String album)
    {
        this.album = album;
    }
    
    public MusicFile(String fullPath, String track, String artist, String genre, String album)
    {
        super(fullPath, MUSIC_PATTERN);
        this.track = track;
        this.artist = artist;
        this.genre = genre;
        this.album = album;
    }
}
