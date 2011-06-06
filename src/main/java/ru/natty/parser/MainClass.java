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
//        musicController.parse("/mnt/b", "/b");
//        musicController.parse("/mnt/a", "/a");
//        musicController.parse("/mnt/а", "/а");    
//        musicController.parse("/mnt/б", "/б");
//        musicController.parse("/mnt/в", "/в");    
//        musicController.parse("/mnt/г", "/г");
//        musicController.parse("/mnt/д", "/д");    
//        musicController.parse("/mnt/л", "/л");
//        musicController.parse("/mnt/м", "/м");    
//        musicController.parse("/mnt/н", "/н");
        musicController.parse("/mnt/о", "/о");    
        musicController.parse("/mnt/п", "/п");
        musicController.parse("/mnt/р", "/р");    
        musicController.parse("/mnt/с", "/с");
        musicController.parse("/mnt/т", "/т");
        musicController.parse("/mnt/у", "/у");    
        musicController.parse("/mnt/ф", "/ф");
        musicController.parse("/mnt/х", "/х");    
        musicController.parse("/mnt/ц", "/ц");
        musicController.parse("/mnt/ч", "/ч");    
        musicController.parse("/mnt/ш", "/ш");
        musicController.parse("/mnt/щ", "/щ");    
        musicController.parse("/mnt/ъ", "/ъ");
        musicController.parse("/mnt/ы", "/ы");    
        musicController.parse("/mnt/ь", "/ь");
        musicController.parse("/mnt/э", "/э");    
        musicController.parse("/mnt/ю", "/ю");
        musicController.parse("/mnt/я", "/я");
        musicController.close();
    }
}                                                                                                                             