/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.natty.parser;

import java.net.MalformedURLException;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import jcifs.smb.SmbException;
import jcifs.smb.SmbFile;
import ru.natty.parser.files.MusicFile;



/**
 *
 * @author zayac
 */
public class Parser {
    public static void main(String[] args) throws SmbException {
        SMBMusicController musicController = new SMBMusicController("smb://natalie.campus/music/");
        musicController.startParsing();
        
//        LinkedList<String> fList = new LinkedList<String>();
//	SmbFile f;
//        try {
//            f = new SmbFile("smb://natalie.campus/music/");
//            SmbFile[] fArr = f.listFiles();
//            for (int i= 0; i < fArr.length; i++)
//                System.out.println(fArr[i].getName());
//        } catch (MalformedURLException ex) {
//            Logger.getLogger(Parser.class.getName()).log(Level.SEVERE, null, ex);
//        }

//        FileSystemManager fsManager;
//        try {
//            fsManager = VFS.getManager();
//            FileObject sourceFile = fsManager.resolveFile("smb://natalie.campus/incom23fing");
//
//        } catch (FileSystemException ex) {
//            Logger.getLogger(Parser.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }
}
