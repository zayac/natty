/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.natty.parser;


import java.net.MalformedURLException;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;
import java.util.logging.Level;
import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystemException;
import org.apache.commons.vfs.FileType;
import org.apache.commons.vfs.VFS;
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
public class LocalFilesParser implements Parser {
    private String homePath = null;
    //private HashMap<String, ID3V2Tag> files = null;
    //private ArrayDeque<SmbFile> queue = null;
    private Set<Integer> parsedFiles = null;
    private final static Logger log = Logger.getLogger(LocalFilesParser.class);
    private final static TagsCommiter commiter = TagsCommiter.getInstance();
    private static Stack<FileObject> directories = new Stack<FileObject>();

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

            FileObject file = VFS.getManager().resolveFile(homePath);
            directories.add(file);
            log.debug("Starting to work with the file " + file.getURL());
            ID3V2Tag tag;
            MediaFile audiof;
            while (!directories.empty())
            {
                log.debug("Stack size: " + directories.size());
                log.debug("Parsed files size: " + parsedFiles.size());
                file = directories.pop();
                if (file.getType() == FileType.FOLDER) {
                    log.debug("File " + file.getURL() + " is a directory");
                    FileObject[] filesInDirectory = file.getChildren();
                    log.debug("There are " + filesInDirectory.length + " files and directories");
                    for (int i = 0; i < filesInDirectory.length; i++) {
                        log.debug("Parsing " + filesInDirectory[i].getURL());
                        //if (!parsedFiles.contains(filesInDirectory[i].hashCode())) {
                            directories.push(filesInDirectory[i]);
                            log.debug("Directory " + filesInDirectory[i].getURL() + " has been added into the queue to be parsed");
                        //} else {
                        //    log.debug("Directory " + filesInDirectory[i].getPath() + " has been already parsed. Skipping");
                        //}
                    }
                } else {
                    if(!getExtension(file.getURL().getPath()).equalsIgnoreCase("mp3"))
                    {
                        log.debug("File " + file.getURL() + " is not an mp3 file");
                    } else
                    {
                        audiof = new MP3File(new FileObjectSource(file));
                        log.debug("File is read successfully");
                        try
                        {
                            tag = audiof.getID3V2Tag();
                            commiter.commit(file.getURL().toString(), tag);
                            log.debug("File " + file.getURL().toString() + " is cached");
                        }
                        catch (ID3Exception e)
                        {
                            log.error(e);
                        }
                    }
                }
                //parsedFiles.add(file.hashCode());
            }
            log.debug("Parser has finished to parse");
        } catch (FileSystemException ex) {
            log.error(ex);
        }
    }
    public LocalFilesParser()
    {
        parsedFiles = new HashSet<Integer> ();
        log.debug("SMBMusicController() has been called");
        //jcifs.Config.setProperty("jcifs.encoding", "ASCII");
        //jcifs.Config.setProperty("jcifs.smb.client.useUnicode", "false");
    }
}