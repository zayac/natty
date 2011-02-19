/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.natty.parser;

import jcifs.smb.SmbException;
import ru.natty.tags.TagsCommiter;

/**
 *
 * @author zayac
 */
public class MainClass {
    public static void main(String[] args) throws SmbException {
        SMBParser musicController = new SMBParser();
        musicController.parse("smb://natalie.campus/music/");
        TagsCommiter commiter = new TagsCommiter(musicController.getTags());
        commiter.commit();
        commiter.close();
    }
}
