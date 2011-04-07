/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.natty.parser;

import jcifs.smb.SmbException;

/**
 *
 * @author zayac
 */
public class MainClass {
    public static void main(String[] args) throws SmbException {
        //System.setProperty("file.encoding", "CP1251");
        SMBParser musicController = new SMBParser();
        musicController.parse("smb://natalie.campus/music/!All/");
        musicController.close();
        //TagsCommiter commiter = new TagsCommiter(musicController.getTags());
        //commiter.commit();
        //commiter.close();
    }
}
