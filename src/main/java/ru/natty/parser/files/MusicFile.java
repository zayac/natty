/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.natty.parser.files;

import org.apache.log4j.Logger;


/**
 *
 * @author zayac
 */
public class MusicFile extends File{
    private static final Logger log = Logger.getLogger(MusicFile.class.getName());
    public MusicFile(String fullPath)
    {
        super(fullPath, "^(.+)?/(.+)?\\.(mp3|flac|wav|ogg)?$");
    }
}
