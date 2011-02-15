/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.natty.parser;
import java.util.Map;
import org.jaudiotagger.tag.Tag;

/**
 * An interface that describes methods of a parser that runs over file system.
 * The parser should start from the Home directory.
 * All cached files information should be stored in the Tags structure,
 * where the key is file's path, and Tag is for Tag in jAudioTagger library.
 * @see org.jaudiotagger.tag.Tag
 */
public interface Parser {
    /*
     * Setter for Home path.
     * Set Home directory, that will be used as the root directory for parser.
     * "/" (slash) should end the path.
     * @param a string that contains path, i.e. /home/musiclover/music/
     */

    public void setHome(String path);
    /*
     * Getter for Home path.
     * @return a string that contains Home path
     */
    public String getHome();
    /*
     * Start parsing file system.
     * Beware, you can't start parsing unless Home path is set.
     * You can't access to Tags Map unless you have run this method.
     */
    public void parse();

    /*
     * Get Tags Map. Should be accessed after parse is run.
     * @return A map where the key is for file absolute path and the value is for Tag from jAudioTagger library
     * @see org.jaudiotagger.tag.Tag
     */
    public Map<String, Tag> getTags();
}
