/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.natty.parser.files;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.log4j.Logger;
/**
 *
 * @author zayac
 */
public class SimpleFile {
    private static final Logger log = Logger.getLogger(SimpleFile.class.getName());

    protected String fullPath;
    protected String extension;
    protected String fileName;
    protected String path;
    protected String FILE_PATTERN = "^(.+)?/(.+)?\\.(.+)?$";

    private void init(String fullPath)
    {
        Pattern pattern = Pattern.compile(FILE_PATTERN);
        Matcher matcher = pattern.matcher(fullPath);
        if (matcher.find())
        {
            this.fullPath = matcher.group(0);
            this.path = matcher.group(1);
            this.fileName = matcher.group(2);
            this.extension = matcher.group(3);
            log.debug("File path: " + this.path + ", name: " + this.fileName + ", extension: " + this.extension);
        }else{
            log.error("Can't parse file " + fullPath);
            throw new IllegalArgumentException();
        }
    }

    public SimpleFile(String fullPath)
    {
        init(fullPath);
    }

    protected SimpleFile(String fullPath, String filePattern)
    {
        FILE_PATTERN = filePattern;
        init(fullPath);
    }

    public String extenstion()
    {
        return extension;
    }

    public String getFileNameWithoutExtenstion()
    {
        return fileName;
    }

    public String getFileNameWithExtenstion()
    {
        return fileName + "." + extension;
    }

    public String getFullPath()
    {
        return fullPath;
    }

    public String getPath()
    {
        return path;
    }
}
