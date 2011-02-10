/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.natty.parser;

import ru.natty.parser.files.MusicFile;



/**
 *
 * @author zayac
 */
public class Parser {
    public static void main(String[] args) {
        MusicFile file = new MusicFile("/home/zayac/classes/music.mpg");
        System.out.println(file.getFileNameWithoutExtenstion());
    }
}
