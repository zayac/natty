/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.natty.parser;

import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.apache.log4j.Logger;
import org.blinkenlights.jid3.v1.ID3V1Tag;
import org.blinkenlights.jid3.v2.ID3V2Tag;
import ru.natty.persist.Track;
import ru.natty.remote.HTTPRequestPoster;

/**
 *
 * @author zayac
 */
public class RemoteFinder {
    private final static Logger log = Logger.getLogger(RemoteFinder.class);
    public Track findTrack(ID3V1Tag tag)
    {
        String trName = tag.getTitle().replace(' ', '+');
        String artName = tag.getArtist().replace(' ', '+'); 
        String xml = HTTPRequestPoster.sendGetRequest("http://ws.audioscrobbler.com/2.0/", "&method=track.search&track=" + trName + "&artist=" + artName + "&api_key=b25b959554ed76058ac220b7b2e0a026");

        
        return null;
    }
}
