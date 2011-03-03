/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.natty.parser;


import java.net.MalformedURLException;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import jcifs.smb.SmbException;
import jcifs.smb.SmbFile;
import org.apache.log4j.Logger;
import org.blinkenlights.jid3.ID3Exception;
import org.blinkenlights.jid3.MP3File;
import org.blinkenlights.jid3.MediaFile;
import org.blinkenlights.jid3.v2.ID3V2Tag;
import ru.natty.tags.TagsCommiter;

/**
 *
 * @author zayac
 */
public class SMBParser implements Parser {
    private String homePath = null;
    //private HashMap<String, ID3V2Tag> files = null;
    //private ArrayDeque<SmbFile> queue = null;
    private Set<Integer> parsedFiles = null;
    private final static Logger log = Logger.getLogger(SMBParser.class);
    private final static TagsCommiter commiter = new TagsCommiter();
    
    public void parse(String path)
    {
        try {
            if (path == null) {
                log.error("Home path for parsing is not set");
                return;
            }
            this.homePath = path;
            SmbFile file = new SmbFile(homePath);
            log.debug("Starting to work with the file " + file.getPath());

            if (file.isDirectory()) {
                log.debug("File " + file.getPath() + " is a directory");
                SmbFile[] filesInDirectory = file.listFiles();
                log.debug("There are " + filesInDirectory.length + " files and directories");
                for (int i = 0; i < filesInDirectory.length; i++) {
                    log.debug("Parsing " + filesInDirectory[i].getPath());
                    if (filesInDirectory[i].isDirectory()) {
                        if (!parsedFiles.contains(filesInDirectory[i].hashCode())) {
                            parse(filesInDirectory[i].getPath());
                            log.debug("Directory " + filesInDirectory[i].getPath() + " has been added into the queue to be parsed");
                        } else {
                            log.debug("Directory " + filesInDirectory[i].getPath() + " has been already parsed. Skipping");
                        }
                    } else {
                        MediaFile audiof = new MP3File(new SMBFileSource(filesInDirectory[i]));
                        log.debug("File is read successfully");
                        ID3V2Tag tag = audiof.getID3V2Tag();
                        commiter.commit(filesInDirectory[i].getPath(), tag);
                        log.debug("File " + filesInDirectory[i].getPath() + " is cached");
                    }
                }
                parsedFiles.add(file.hashCode());
            }
            log.debug("Parser has finished to parse");
        } catch (ID3Exception ex) {
            log.error(path + " " + ex);
        } catch (SmbException ex) {
            log.error(path + " " + ex);
        } catch (MalformedURLException ex) {
            log.error(path + " " + ex);
        }
    }
    public SMBParser()
    {
        parsedFiles = new HashSet<Integer> ();
        log.debug("SMBMusicController() has been called");
        //jcifs.Config.setProperty("jcifs.encoding", "ASCII");
        //jcifs.Config.setProperty("jcifs.smb.client.useUnicode", "false");
    }
}