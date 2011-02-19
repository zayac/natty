/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.natty.parser;


import java.net.MalformedURLException;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import jcifs.smb.SmbException;
import jcifs.smb.SmbFile;
import org.apache.log4j.Logger;
import org.blinkenlights.jid3.MP3File;
import org.blinkenlights.jid3.MediaFile;
import org.blinkenlights.jid3.v2.ID3V2Tag;

/**
 *
 * @author zayac
 */
public class SMBParser implements Parser {
    private String homePath = null;
    private HashMap<String, ID3V2Tag> files = null;
    private ArrayDeque<SmbFile> queue = null;
    private Set<SmbFile> parsedFiles = null;
    private final static Logger log = Logger.getLogger(SMBParser.class);

    public void parse(String path)
    {
        this.homePath = path;
        if(homePath == null)
        {
            log.error("Home path for parsing is not set");
            return;
        }
        try {
            files.clear();
            parsedFiles.clear();
            queue.add(new SmbFile(homePath));
            while (!queue.isEmpty()) {
                SmbFile file = queue.pop();
                log.debug("Starting to work with the file " + file.getPath());
                try {
                    if (file.isDirectory()) {
                        log.debug("File " + file.getPath() + " is a directory");
                        SmbFile[] filesInDirectory = file.listFiles();
                        for (int i = 0; i < filesInDirectory.length; i++) {
                            log.debug("Parsing " + filesInDirectory[i].getPath());
                            if (filesInDirectory[i].isDirectory()) {
                                if (!parsedFiles.contains(filesInDirectory[i])) {
                                    queue.add(filesInDirectory[i]);
                                    log.debug("Directory " + filesInDirectory[i].getPath() + " has been added into the queue to be parsed");
                                } else {
                                    log.debug("Directory " + filesInDirectory[i].getPath() + " has been already parsed. Skipping");
                                }
                            } else {
                                try {
                                    MediaFile audiof = new MP3File(new SMBFileSource(filesInDirectory[i]));
                                    log.debug("File is read successfully");
                                    ID3V2Tag tag = audiof.getID3V2Tag();
                                    files.put(filesInDirectory[i].getPath(), tag);
                                    log.debug("File " + filesInDirectory[i].getPath() + " is cached");
                                    // MusicFile fileWithTag = new MusicFile(filesInDirectory[i].getPath(), tag.get);
                                } catch (Exception ex) {
                                    log.error("Can't parse" + filesInDirectory[i].getPath() + ". " + ex.getMessage());
                                }
                            }
                        }
                    }
                } catch (SmbException ex) {
                    log.error("Can't parse " + file.getPath());
                }
                parsedFiles.add(file);
            }
            log.debug("Parser has finished to parse");
        } catch (MalformedURLException ex) {
            log.error("File path " + homePath + " is set incorrectly");
        }
    }
    public SMBParser()
    {
        files = new HashMap<String, ID3V2Tag> ();
        queue = new ArrayDeque<SmbFile>();
        parsedFiles = new HashSet<SmbFile> ();
        log.debug("SMBMusicController() has been called");
    }

    public HashMap<String, ID3V2Tag> getTags() {
        return files;
    }

}
