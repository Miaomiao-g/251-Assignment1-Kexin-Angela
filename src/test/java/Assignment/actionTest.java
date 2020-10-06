package Assignment;

import org.junit.Before;
import org.junit.Test;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.io.FileInputStream;

public class actionTest {

    private int boundX = 400, boundY = 300;

    notePad note;

    @Before
    public void initTest(){
        note = new notePad();
        note.init();
    }

    @Test
    public void testInit(){
        action.main(null);
    }

    @Test
    public void testOpen(){
        note.openListen();
    }

    @Test
    public void testSave(){
        note.saveListen();
    }

    @Test
    public void testSearch(){
        note.searchListen();
    }

    @Test
    public void testCopy(){
        note.copyListen();
    }

}
