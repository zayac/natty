/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.natty.parser;


import java.net.MalformedURLException;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;
import jcifs.smb.SmbException;
import jcifs.smb.SmbFile;
import org.apache.log4j.Logger;
import org.blinkenlights.jid3.ID3Exception;
import org.blinkenlights.jid3.MP3File;
import org.blinkenlights.jid3.MediaFile;
import org.blinkenlights.jid3.io.TextEncoding;
import org.blinkenlights.jid3.v1.ID3V1Tag;
import org.blinkenlights.jid3.v2.ID3V2Tag;
import ru.natty.tags.TagsCommiter;

/**
 *
 * @author zayac
 */
public class SMBParser implements Parser {
    private String homePath = null;
    private Set<Integer> parsedFiles = null;
    private final static Logger log = Logger.getLogger(SMBParser.class);
    private final static TagsCommiter commiter = TagsCommiter.getInstance();
    private static Stack<SmbFile> directories = new Stack<SmbFile>();
    public String getExtension(String path)
    {
        int dot = path.lastIndexOf(".");
        return path.substring(dot + 1);
    }

    public void close()
    {
        commiter.close();
    }
    public void parse(String path)
    {
        try {
            if (path == null) {
                log.error("Home path for parsing is not set");
                return;
            }
            this.homePath = path;
            SmbFile file = new SmbFile(homePath);
            directories.add(file);
            log.debug("Starting to work with the file " + file.getPath());
            while (!directories.empty())
            {
                file = directories.pop();
                log.debug("Directory status: " + file.isDirectory());
                if (file.isDirectory()) {
                    log.debug("File " + file.getPath() + " is a directory");
                    SmbFile[] filesInDirectory = file.listFiles();
                    log.debug("There are " + filesInDirectory.length + " files and directories");
                    for (int i = 0; i < filesInDirectory.length; i++) {
                        log.debug("Parsing " + filesInDirectory[i].getPath());
                        if (!parsedFiles.contains(filesInDirectory[i].hashCode())) {
                            directories.push(filesInDirectory[i]);
                            log.debug("Directory " + filesInDirectory[i].getPath() + " has been added into the queue to be parsed");
                        } else {
                            log.debug("Directory " + filesInDirectory[i].getPath() + " has been already parsed. Skipping");
                        }
                    }
                } else {
                    if(!getExtension(file.getPath()).equalsIgnoreCase("mp3"))
                    {
                        log.debug("File " + file.getPath() + " is not an mp3 file");
                    } else
                    {
//                        AudioFile audiof = AudioFileIO.read(file);
//                        log.debug("File is read successfully");
//                        try
//                        {
//                            log.debug("File is read successfully");
//                            Tag tag = audiof.getTag();
//                            commiter.commit(file.getPath(), tag);
//                            log.debug("File " + file.getPath() + " is cached");
//                        }
//                        catch (ID3Exception e)
//                        {
//                            log.error(e);
//                        }
                    }
                }
                parsedFiles.add(file.hashCode());
            }
            log.debug("Parser has finished to parse");
        }
        catch (SmbException ex) {
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