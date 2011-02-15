/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.natty.parser;

import java.util.Collection;
import ru.natty.parser.files.SimpleFile;

/**
 *
 * @author zayac
 */
public interface Controller {
    public enum Status { NULL, READY, PARSING, FINISH, FAIL}
    public void setHome(String path);
    public String getHome();
    public void startParsing();
    public Status getStatus();
    public Collection<? extends SimpleFile> getFiles();
}
