/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.natty.parser;

import java.io.IOException;
import java.util.logging.LogManager;
import jcifs.smb.SmbException;

/**
 *
 * @author zayac
 */
public class MainClass {
    public static void main(String[] args) throws SmbException, IOException {
        LogManager.getLogManager().readConfiguration();
        LocalParser musicController = new LocalParser();
        musicController.parse("/mnt/");                 
        musicController.close();
    }
}