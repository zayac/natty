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
        SMBParser musicController = new SMBParser("smb://natalie.campus/music/");
        musicController.parse();
        
//        LinkedList<String> fList = new LinkedList<String>();
//	SmbFile f;
//        try {
//            f = new SmbFile("smb://natalie.campus/music/");
//            SmbFile[] fArr = f.listFiles();
//            for (int i= 0; i < fArr.length; i++)
//                System.out.println(fArr[i].getName());
//        } catch (MalformedURLException ex) {
//            Logger.getLogger(MainClass.class.getName()).log(Level.SEVERE, null, ex);
//        }

//        FileSystemManager fsManager;
//        try {
//            fsManager = VFS.getManager();
//            FileObject sourceFile = fsManager.resolveFile("smb://natalie.campus/incom23fing");
//
//        } catch (FileSystemException ex) {
//            Logger.getLogger(MainClass.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }
}
