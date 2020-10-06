package Assignment;

import org.junit.Test;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.io.FileInputStream;

public class actionTest {

    JFrame f;
    JLabel label;
    JTextArea textArea;
    JFileChooser fileChooser;
    FileInputStream fileInStream;
    JMenuItem item0, item1, item2, item3, item4, editItem0, editItem1, editItem2, editItem3, editItem4, editItem5, aboutItem;
    JMenu menu1, menu2, menu3;

    // 系统剪贴板
    Toolkit toolkit=Toolkit.getDefaultToolkit();
    Clipboard clipBoard=toolkit.getSystemClipboard();

    @Test
    public void testInit(){
        action.main(null);
    }

}
