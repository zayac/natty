/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.natty.parser;

import org.apache.log4j.Logger;
import de.umass.lastfm.Track;
/**
 *
 * @author zayac
 */

import java.util.Collection;

public class RemoteFinder {
    private final static Logger log = Logger.getLogger(RemoteFinder.class);
    public static TagData findTrackName(String title, String artist)
    {
        title = title.replace(' ', '+');
        artist = artist.replace(' ', '+'); 
        Collection<Track> trCol = Track.search(artist, title, 5, "b25b959554ed76058ac220b7b2e0a026");
        if(!trCol.isEmpty())
        {
            TagData data = new TagData();
            Track tr = trCol.iterator().next();
            data.setAlbum(tr.getAlbum());
            data.setArtist(tr.getArtist());
            data.setTitle(tr.getName());
            return data;
        }
        else
            return null;
    }
}
