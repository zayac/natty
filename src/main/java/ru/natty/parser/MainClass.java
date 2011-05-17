/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.natty.parser;

import jcifs.smb.SmbException;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 *
 * @author zayac
 */
public class MainClass {
    public static void main(String[] args) throws SmbException {
        //System.setProperty("file.encoding", "CP1251");
        LocalParser musicController = new LocalParser();
        //musicController.parse("smb://natalie.campus/music/Alternative/");
        //musicController.parse("smb://natalie.campus/music/Avantgarde/");
        //musicController.parse("smb://natalie.campus/music/Blues/");
        //musicController.parse("smb://natalie.campus/music/Classic/");  
        //musicController.parse("smb://natalie.campus/music/Electronic/");   
        musicController.parse("/mnt/");                 
        musicController.close();
        //TagsCommiter commiter = new TagsCommiter(musicController.getTags());
        //commiter.commit();
        //commiter.close();
    }
}