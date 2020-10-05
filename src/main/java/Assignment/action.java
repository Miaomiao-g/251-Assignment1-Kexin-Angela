package Assignment;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.TextArea;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.management.RuntimeErrorException;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;


//import javafx.scene.control.Tab;

public class action {


    JFrame f;
    JLabel label;
    JTextArea textArea;
    JFileChooser fileChooser;
    FileInputStream fileInStream;

    public action() {
        // TODO Auto-generated constructor stub
        f=new JFrame("Test Editor");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setBounds(400, 300, 400, 200);

        Container contentPane=f.getContentPane();
        textArea=new JTextArea();
        JScrollPane scrollPane=new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(350, 300));
        final JTextArea textArea = new JTextArea(10, 10);
        JPanel panel=new JPanel();
        JMenuBar menuBar1=new JMenuBar();  //add menu group
        f.setJMenuBar(menuBar1);          //make the menu at the top of the screen
        JMenu menu1=new JMenu("File"); //add menu buttons
        JMenu menu2=new JMenu("Edit");
        JMenu menu3=new JMenu("View");
        JMenu menu4=new JMenu("Manage");
        JMenu menu5=new JMenu("Help");

        //Add the menu component to the menu bar component.
        menuBar1.add(menu1);
        menuBar1.add(menu2);
        menuBar1.add(menu3);
        menuBar1.add(menu4);
        menuBar1.add(menu5);


        //创建菜单项组件

        final JMenuItem item1=new JMenuItem("Open");
        final JMenuItem item2=new JMenuItem("Save");
        JMenuItem item3=new JMenuItem("Print");
        JMenuItem item4=new JMenuItem("Exit");
        menu1.add(item1);
        menu1.add(item2);
        menu1.addSeparator();           //菜单项之间的分隔线组件
        menu1.add(item3);
        menu1.addSeparator();
        menu1.add(item4);

        //设置顶层容器类的可见性
        f.setVisible(true);
        label=new JLabel("",JLabel.CENTER);
        contentPane.add(label, BorderLayout.NORTH);
        contentPane.add(scrollPane, BorderLayout.CENTER);
        contentPane.add(panel, BorderLayout.SOUTH);
        f.pack();
        //处理退出菜单项的动作事件
        item4.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed( ActionEvent e) {
                // TODO Auto-generated method stub
                int i=JOptionPane.showConfirmDialog(null, "Do you want to exit? ",
                        "Exit",JOptionPane.YES_NO_CANCEL_OPTION);
                //通过对话框中按钮的选择来决定结果，单机yes时，窗口直接消失
                if(i==0)
                    f.dispose();
            }
        });
        item1.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub
                JFileChooser chooser = new JFileChooser();
                if (chooser.showOpenDialog(item1)==JFileChooser.APPROVE_OPTION) {//
                    File file = chooser.getSelectedFile();
                    textArea.setText(file.getName()+":"+file.getPath()+"\n"+file.length());
                    readFile(file);
                };
            }
        });
        item2.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub
                JFileChooser chooser = new JFileChooser();
                if (chooser.showSaveDialog(item2)==JFileChooser.APPROVE_OPTION) {
                    File file = chooser.getSelectedFile();
                    writeFile(file.getPath());
                }
            }
        });
    }

    //    read files
    public void readFile(File file){
        BufferedReader bReader;
        try {
            bReader=new BufferedReader(new FileReader(file));
            StringBuffer sBuffer=new StringBuffer();
            String str;
            while((str=bReader.readLine())!=null){
                sBuffer.append(str+'\n');
                System.out.println(str);
            }
            textArea.setText(sBuffer.toString());
        } catch (Exception e) {
            // TODO: handle exception
        }
    }
    //    write files
    public void writeFile(String savepath){//写文件
        FileOutputStream fos= null;
        try {
            fos=new FileOutputStream(savepath);
            fos.write(textArea.getText().getBytes());
            fos.close();
            System.out.println("已保存");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        textArea.getText();
    }

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        new action();

    }
}
