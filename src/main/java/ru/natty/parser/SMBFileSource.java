/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.natty.parser;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.log4j.Logger;
import jcifs.smb.SmbException;
import jcifs.smb.SmbFile;
import org.blinkenlights.jid3.io.IFileSource;

/**
 *
 * @author zayac
 */
public class SMBFileSource implements IFileSource{
    private final static Logger log = Logger.getLogger(SMBFileSource.class);
    public SmbFile file = null;

    public SMBFileSource(SmbFile file)
    {
        this.file = file;
    }
    
    public IFileSource createTempFile(String string, String string1) throws IOException {
        return new SMBFileSource(new SmbFile(string + string1));
    }

    public boolean delete() {
        try{
            file.delete();
        }catch(SmbException ex)
        {
            log.error(ex.getMessage());
            return false;
        }
        return true;
    }

    public String getName() {
        return file.getName();
    }

    public InputStream getInputStream() throws FileNotFoundException {
        try {
            return file.getInputStream();
        } catch (IOException ex) {
            log.error(ex.getMessage());
        }
        return null;
    }

    public OutputStream getOutputStream() throws FileNotFoundException {
        try {
            return file.getOutputStream();
        } catch (IOException ex) {
            log.error(ex.getMessage());
        }
        return null;
    }

    public long length() {
        try {
            return file.length();
        } catch (IOException ex) {
            log.error(ex.getMessage());
        }
        return 0;
    }

    public boolean renameTo(IFileSource ifs) throws IOException {
        try {
            file.renameTo(new SmbFile(ifs.getName()));
        }catch(SmbException ex)
        {
            log.error(ex.getMessage());
            return false;
        }
        return true;
    }

}
