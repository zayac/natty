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
        SMBParser musicController = new SMBParser();
        musicController.parse("smb://natalie.campus/music/");

    }
}
