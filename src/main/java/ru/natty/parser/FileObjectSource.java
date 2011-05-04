/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.natty.parser;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.commons.vfs.FileSystemException;
import org.apache.log4j.Logger;
import jcifs.smb.SmbFile;
import org.apache.commons.vfs.FileObject;

import org.blinkenlights.jid3.io.IFileSource;

/**
 *
 * @author zayac
 */
public class FileObjectSource implements IFileSource{
    private final static Logger log = Logger.getLogger(SMBFileSource.class);
    public FileObject file = null;

    public FileObjectSource(FileObject file)
    {
        this.file = file;
    }

    public IFileSource createTempFile(String string, String string1) throws IOException {
        return new SMBFileSource(new SmbFile(string + string1));
    }

    public boolean delete() {
        try {
            file.delete();
            return true;
        } catch (FileSystemException ex) {
            log.error(ex);
        }
        return false;
    }

    public String getName() {
        return file.getName().getPath();
    }

    public InputStream getInputStream() throws FileNotFoundException {
        try {
            return file.getContent().getInputStream();
        } catch (IOException ex) {
            log.error(ex.getMessage());
        }
        return null;
    }

    public OutputStream getOutputStream() throws FileNotFoundException {
        try {
            return file.getContent().getOutputStream();
        } catch (IOException ex) {
            log.error(ex.getMessage());
        }
        return null;
    }

    public long length() {
        try {
            return file.getContent().getSize();
        } catch (IOException ex) {
            log.error(ex.getMessage());
        }
        return 0;
    }

    public boolean renameTo(IFileSource ifs) throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }



}
