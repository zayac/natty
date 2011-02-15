/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.natty.parser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
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
import ru.natty.parser.files.SimpleFile;
import ru.natty.parser.files.MusicFile;

/**
 *
 * @author zayac
 */
public class SMBMusicController implements Controller {
    private String homePath = null;
    private Status workStatus = null;
    private ArrayList<MusicFile> files = null;
    private final static Logger log = Logger.getLogger(SMBMusicController.class);
    private Thread parsingThread = null;

    private class ParsingThread extends Thread {
        private final Logger log = Logger.getLogger(ParsingThread.class);
        private ArrayDeque<SmbFile> queue = null;
        private Set<SmbFile> parsedFiles = null;
	public ParsingThread(SmbFile homeFile)
        {
            queue = new ArrayDeque<SmbFile>();
            queue.add(homeFile);
            parsedFiles = new HashSet<SmbFile>();
            log.debug("Parsing Thread is initialized");
        }



        private File inputStreamToFile(String path, InputStream istream)
        {
            log.debug("Converting Input Stream to File " + path);
            File f = null;
            try {
                f = new File("/tmp/music.tmp");
                OutputStream out = new FileOutputStream(f);
                int read = 0;
                byte[] bytes = new byte[1024];
                while((read  = istream.read(bytes)) != -1)
                {
                    out.write(bytes, 0, read);
                }
                istream.close();
                out.flush();
                out.close();
            } catch (IOException ex) {
                log.error("IO exception occured converting to File " + path);
            }
            return f;
        }

        @Override
        public void run()
        {
            workStatus = Status.PARSING;
            while(!queue.isEmpty())
            {
                workStatus = Status.PARSING;
                SmbFile file = queue.pop();
                log.debug("Starting to work with the file " + file.getPath());
                try {
                    if (file.isDirectory()) {
                        log.debug("File " + file.getPath() + " is a directory");
                        SmbFile[] filesInDirectory = file.listFiles();
                        log.debug(filesInDirectory[0].getPath());
                        for (int i = 0; i < filesInDirectory.length; i++)
                        {
                            log.debug("Parsing " + filesInDirectory[i].getPath());
                            if (filesInDirectory[i].isDirectory())
                            {
                                if (!parsedFiles.contains(filesInDirectory[i]))
                                {
                                    queue.add(filesInDirectory[i]);
                                    log.debug("Directory " + filesInDirectory[i].getPath() + " has been added into the queue to be parsed");
                                } else
                                {
                                    log.debug("Directory " + filesInDirectory[i].getPath() + " has been already parsed. Skipping");
                                }
                            } else{
                                try {
                                    //filesInDirectory[i].copyTo(new SmbFile("/tmp/music.mp3"));
                                    AudioFile audiof = AudioFileIO.read(new File(filesInDirectory[i].getPath()));
                                    log.debug("File is read successfully");
                                    Tag tag = audiof.getTag();
                                    MusicFile fileWithTag = new MusicFile(filesInDirectory[i].getPath());
                                    files.add(fileWithTag);
                                    log.debug("File " + filesInDirectory[i].getPath() + " is cached");
                                    // MusicFile fileWithTag = new MusicFile(filesInDirectory[i].getPath(), tag.get);
                                    
                                } catch (CannotReadException ex) {
                                    log.error("Can't read " + filesInDirectory[i].getPath());
                                    workStatus = Status.FAIL;
                                } catch (IOException ex) {
                                    log.error("IO error with " + filesInDirectory[i].getPath());
                                    workStatus = Status.FAIL;
                                } catch (TagException ex) {
                                    log.error("Error accessing tags in " + filesInDirectory[i].getPath());
                                    workStatus = Status.FAIL;
                                } catch (ReadOnlyFileException ex) {
                                    log.error("Read only file " + filesInDirectory[i].getPath());
                                    workStatus = Status.FAIL;
                                } catch (InvalidAudioFrameException ex) {
                                    log.error("Invalid audio frame in" + filesInDirectory[i].getPath());
                                    workStatus = Status.FAIL;
                                }
                            }
                        }
                    }
                } catch (SmbException ex) {
                    log.error("Can't parse " + file.getPath());
                    workStatus = Status.FAIL;
                }
                parsedFiles.add(file);
            }
            workStatus = Status.FINISH;
            log.debug("Parsing thread has finished to parse");
        }
    }
    public SMBMusicController()
    {
        log.debug("SMBMusicController() has been called");
        this.workStatus = Status.NULL;
        this.homePath = null;
        this.files = new ArrayList<MusicFile>();
    }

    public SMBMusicController(String path)
    {
        log.debug("SMBMusicController(String path) has been called. Path: " + path);
        this.workStatus = Status.READY;
        this.homePath = path;
        this.files = new ArrayList<MusicFile>();
    }

    public void setHome(String path) {
        this.workStatus = Status.READY;
        this.homePath = path;
    }

    public String getHome() {
        return this.homePath;
    }

    public void startParsing() {
        try {
            parsingThread = new ParsingThread(new SmbFile(homePath));
            if (workStatus.equals(Status.READY))
            {
                parsingThread.start();
                log.debug("Parsing thread has been started");
            }
        } catch (MalformedURLException ex) {
            log.error("Can't access to " + homePath);
            workStatus = Status.FAIL;
        }
    }

    public Status getStatus() {
        return this.workStatus;
    }

    public Collection<MusicFile> getFiles() {
        return files;
    }

}
