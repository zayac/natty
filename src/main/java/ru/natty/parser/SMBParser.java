/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.natty.parser;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import jcifs.smb.SmbException;
import jcifs.smb.SmbFile;
import org.apache.log4j.Logger;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagException;

/**
 *
 * @author zayac
 */
public class SMBParser implements Parser {
    private String homePath = null;
    private HashMap<String, Tag> files = null;
    private ArrayDeque<SmbFile> queue = null;
    private Set<SmbFile> parsedFiles = null;
    private final static Logger log = Logger.getLogger(SMBParser.class);

    public void parse()
    {
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
                        log.debug(filesInDirectory[0].getPath());
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
                                    //filesInDirectory[i].copyTo(new SmbFile("/tmp/music.mp3"));
                                    AudioFile audiof = AudioFileIO.read(new File(filesInDirectory[i].getPath()));
                                    log.debug("File is read successfully");
                                    Tag tag = audiof.getTag();
                                    files.put(filesInDirectory[i].getPath(), tag);
                                    log.debug("File " + filesInDirectory[i].getPath() + " is cached");
                                    // MusicFile fileWithTag = new MusicFile(filesInDirectory[i].getPath(), tag.get);
                                } catch (CannotReadException ex) {
                                    log.error("Can't read " + filesInDirectory[i].getPath());
                                } catch (IOException ex) {
                                    log.error("IO error with " + filesInDirectory[i].getPath());
                                } catch (TagException ex) {
                                    log.error("Error accessing tags in " + filesInDirectory[i].getPath());
                                } catch (ReadOnlyFileException ex) {
                                    log.error("Read only file " + filesInDirectory[i].getPath());
                                } catch (InvalidAudioFrameException ex) {
                                    log.error("Invalid audio frame in" + filesInDirectory[i].getPath());
                                }
                            }
                        }
                    }
                } catch (SmbException ex) {
                    log.error("Can't parse " + file.getPath());
                }
                parsedFiles.add(file);
            }
            log.debug("Parsing thread has finished to parse");
        } catch (MalformedURLException ex) {
            log.error("File path " + homePath + " is set incorrectly");
        }
    }
    public SMBParser()
    {
        files = new HashMap<String, Tag> ();
        queue = new ArrayDeque<SmbFile>();
        parsedFiles = new HashSet<SmbFile> ();
        log.debug("SMBMusicController() has been called");
    }

    public SMBParser(String path)
    {
        files = new HashMap<String, Tag> ();
        queue = new ArrayDeque<SmbFile>();
        parsedFiles = new HashSet<SmbFile> ();
        this.homePath = path;
        log.debug("SMBMusicController(String path) has been called. Path: " + path);
    }

    public void setHome(String path) {
        this.homePath = path;
    }

    public String getHome() {
        return this.homePath;
    }

    public HashMap<String, Tag> getTags() {
        return files;
    }

}
