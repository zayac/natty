/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.natty.parser;


import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;
import java.util.logging.Level;
import org.apache.log4j.Logger;
import org.blinkenlights.jid3.ID3Exception;
import org.blinkenlights.jid3.ID3Tag;
import org.blinkenlights.jid3.MP3File;
import org.blinkenlights.jid3.MediaFile;
import org.blinkenlights.jid3.io.TextEncoding;
import org.blinkenlights.jid3.v1.ID3V1Tag;
import org.blinkenlights.jid3.v2.ID3V2Tag;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagException;
import ru.natty.tags.TagsCommiter;

/**
 *
 * @author zayac
 */
public class LocalParser implements Parser {
    private String homePath = null;
    //private HashMap<String, ID3V2Tag> files = null;
    //private ArrayDeque<SmbFile> queue = null;
    private Set<Integer> parsedFiles = null;
    private final static Logger log = Logger.getLogger(SMBParser.class);
    private final static TagsCommiter commiter = TagsCommiter.getInstance();
    private static Stack<File> directories = new Stack<File>();
    private String prefix;
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
        prefix = path;
        if (path == null) {
            log.error("Home path for parsing is not set");
            return;
        }
        this.homePath = path;
        File file = new File(homePath);
        directories.add(file);
        log.debug("Starting to work with the file " + file.getPath());

        while (!directories.empty())
        {
            file = directories.pop();
            log.debug("Directory status: " + file.isDirectory());
            if (file.isDirectory()) {
                log.debug("File " + file.getPath() + " is a directory");
                File[] filesInDirectory = file.listFiles();
                log.debug("There are " + filesInDirectory.length + " files and directories");
                for (int i = 0; i < filesInDirectory.length; i++) {
                    //log.debug("Parsing " + filesInDirectory[i].getPath());
                    //if (!parsedFiles.contains(filesInDirectory[i].hashCode())) {
                        directories.push(filesInDirectory[i]);
                        log.debug("Directory " + filesInDirectory[i].getPath() + " has been added into the queue to be parsed");
                    //} else {
                    //    log.debug("Directory " + filesInDirectory[i].getPath() + " has been already parsed. Skipping");
                    //}
                }
            } else {
                if(!getExtension(file.getPath()).equalsIgnoreCase("mp3"))
                {
                    log.debug("File " + file.getPath() + " is not an mp3 file");
                } else
                {
                    try
                    {
                        AudioFile audiof = AudioFileIO.read(file);
                        log.debug("File is read successfully");
                        Tag tag = audiof.getTag();
                        commiter.commit(file.getPath().replaceFirst(prefix, ""), tag);
//                            for(int i = 0; i < tag.length; i++)
//                            {
//                                if (tag[i] instanceof ID3V1Tag)
//                                {
//                                    commiter.commit(file.getPath().replaceFirst(prefix, ""), (ID3V1Tag) tag[i]);
//                                }
//                            }
                        //TextEncoding.setDefaultTextEncoding(TextEncoding.ISO_8859_1);
                        //commiter.commit(file.getPath(), tag);
                        log.debug("File " + file.getPath() + " is cached");
                    }
                    catch (CannotReadException ex)
                    {
                        java.util.logging.Logger.getLogger(LocalParser.class.getName()).log(Level.SEVERE, null,ex);
                    } catch (IOException ex) {
                        java.util.logging.Logger.getLogger(LocalParser.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (TagException ex) {
                        java.util.logging.Logger.getLogger(LocalParser.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (ReadOnlyFileException ex) {
                        java.util.logging.Logger.getLogger(LocalParser.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (InvalidAudioFrameException ex) {
                        java.util.logging.Logger.getLogger(LocalParser.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
            parsedFiles.add(file.hashCode());
        }
        log.debug("Parser has finished to parse");
    }

    public LocalParser()
    {
        parsedFiles = new HashSet<Integer> ();
        log.debug("LocalMusicParser() has been called");
        //jcifs.Config.setProperty("jcifs.encoding", "ASCII");
        //jcifs.Config.setProperty("jcifs.smb.client.useUnicode", "false");
    }
}