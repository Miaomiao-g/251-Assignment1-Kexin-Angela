package Assignment;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.Calendar;
import java.util.Date;

public class notePad {
    private JFrame f;
    private JLabel label;
    private JTextArea textArea;
    private JFileChooser fileChooser;
    private FileInputStream fileInStream;
    private JMenuItem item0, item1, item2, item3, item4, editItem0, editItem1, editItem2, editItem3, editItem4, editItem5, aboutItem;
    private JMenu menu1, menu2, menu3;

    // 系统剪贴板
    private Toolkit toolkit=Toolkit.getDefaultToolkit();
    private Clipboard clipBoard=toolkit.getSystemClipboard();

    private int boundX = 400, boundY = 300;

    public void init(){
        // TODO Auto-generated constructor stub
        f=new JFrame("Test Editor");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setBounds(boundX, boundY, 400, 200);

        Container contentPane=f.getContentPane();
        textArea=new JTextArea();
        JScrollPane scrollPane=new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(350, 300));
        JPanel panel=new JPanel();
        JMenuBar menuBar1=new JMenuBar();  //添加菜单条组件
        f.setJMenuBar(menuBar1);          //将菜单栏添加到顶层容器中

        menu1=new JMenu("File");
        menu2=new JMenu("Edit");
        menu3=new JMenu("About");

        //将菜单组件添加到菜单条组件中
        menuBar1.add(menu1);
        menuBar1.add(menu2);
        menuBar1.add(menu3);

        //创建file菜单项组件
        item0 = new JMenuItem("New");
        item1=new JMenuItem("Open");
        item2=new JMenuItem("Save");
        item3=new JMenuItem("Print");
        item4=new JMenuItem("Exit");
        menu1.add(item0);
        menu1.add(item1);
        menu1.add(item2);
        menu1.addSeparator();           //菜单项之间的分隔线组件
        menu1.add(item3);
        menu1.addSeparator();
        menu1.add(item4);

        //创建edit菜单项组件
        editItem0 = new JMenuItem("Search");
        editItem1 = new JMenuItem("Select All");
        editItem2 = new JMenuItem("Copy");
        editItem3 = new JMenuItem("Past");
        editItem4 = new JMenuItem("Cut");
        editItem5 = new JMenuItem("Time And Date");
        menu2.add(editItem0);
        menu2.addSeparator();
        menu2.add(editItem1);
        menu2.add(editItem2);
        menu2.add(editItem3);
        menu2.add(editItem4);
        menu2.addSeparator();
        menu2.add(editItem5);

        //创建about组件
        aboutItem = new JMenuItem("About");
        menu3.add(aboutItem);

        //设置顶层容器类的可见性
        f.setVisible(true);
        label=new JLabel("",JLabel.CENTER);
        contentPane.add(label, BorderLayout.NORTH);
        contentPane.add(scrollPane, BorderLayout.CENTER);
        contentPane.add(panel, BorderLayout.SOUTH);
        f.pack();
    }

    // new事件监听
    public void newListen(){
        item0.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                int i=JOptionPane.showConfirmDialog(null, "Please confirm your file was saved before open a new",
                        "Warning",JOptionPane.YES_NO_CANCEL_OPTION);
                //通过对话框中按钮的选择来决定结果，单机yes时，窗口直接消失
                if(i==0){
                    f.dispose();
                    boundX += 100;
                    boundY += 100;
                    init();
                    eventListener();
                }

            }
        });
    }

    //open 事件
    public void openListen(){
        item1.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub
                JFileChooser chooser = new JFileChooser();
                if (chooser.showOpenDialog(item1)==JFileChooser.APPROVE_OPTION) {//
                    File file = chooser.getSelectedFile();
                    textArea.setText(file.getName()+":"+file.getPath()+"\n"+file.length());
                    readFile(file);
                }

            }
        });
    }

    //save事件
    public void saveListen(){
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

    //处理退出菜单项的动作事件
    public void exitListen(){
        item4.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed( ActionEvent e) {
                // TODO Auto-generated method stub
                int i=JOptionPane.showConfirmDialog(null, "是否真的退出系统",
                        "退出确认对话框",JOptionPane.YES_NO_CANCEL_OPTION);
                //通过对话框中按钮的选择来决定结果，单机yes时，窗口直接消失
                if(i==0)
                    f.dispose();

            }
        });
    }

    // search event
    public void searchListen(){
        editItem0.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textArea.requestFocus();
                find();
            }
        });
    }

    // search algorithm
    public void find(){
        //create event buttons
        //Keep other windows was active when Search window was opened
        final JDialog findDialog=new JDialog(f, "Search", false);//false时允许其他窗口同时处于激活状态(即无模式)
        Container con=findDialog.getContentPane();//返回此对话框的contentPane对象
        con.setLayout(new FlowLayout(FlowLayout.LEFT));

        //Search text table
        JLabel findContentLabel=new JLabel("Search content：");
        final JTextField findText=new JTextField(15);

        // cearte buttons
        JButton findNextButton=new JButton("Search next：");
        final JCheckBox matchCheckBox=new JCheckBox("Match case");
        ButtonGroup bGroup=new ButtonGroup();
        final JRadioButton upButton=new JRadioButton("Up Search");
        final JRadioButton downButton=new JRadioButton("Down Search");
        downButton.setSelected(true);
        bGroup.add(upButton);
        bGroup.add(downButton);
        JButton cancel=new JButton("cancel");

        // all button event listener
        //取消按钮事件处理
        cancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                findDialog.dispose();
            }
        });
        //"查找下一个"按钮监听
        findNextButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //"区分大小写(C)"的JCheckBox是否被选中
                int k;
                final String str1,str2,str3,str4,strA,strB;
                str1=textArea.getText();
                str2=findText.getText();
                str3=str1.toUpperCase();
                str4=str2.toUpperCase();
                if(matchCheckBox.isSelected()){ //区分大小写
                    strA=str1;
                    strB=str2;
                }
                else{ //不区分大小写,此时把所选内容全部化成大写(或小写)，以便于查找
                    strA=str3;
                    strB=str4;
                }
                if(upButton.isSelected()) {
                    if(textArea.getSelectedText()==null)
                        k=strA.lastIndexOf(strB,textArea.getCaretPosition()-1);
                    else
                        k=strA.lastIndexOf(strB, textArea.getCaretPosition()-findText.getText().length()-1);
                    if(k>-1) {
                        textArea.setCaretPosition(k);
                        textArea.select(k,k+strB.length());
                    }
                    else {
                        JOptionPane.showMessageDialog(null,"Can't Searched it！","Search",JOptionPane.INFORMATION_MESSAGE);
                    }
                }
                else if(downButton.isSelected()) {
                    if(textArea.getSelectedText()==null)
                        k=strA.indexOf(strB,textArea.getCaretPosition()+1);
                else
                    k=strA.indexOf(strB, textArea.getCaretPosition()-findText.getText().length()+1);
                    if(k>-1) {
                        textArea.setCaretPosition(k);
                        textArea.select(k,k+strB.length());
                    }
                    else {
                        JOptionPane.showMessageDialog(null,"Can't Searched it！","Search",JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            }
        });

        // make a search panel
        JPanel panel1=new JPanel();
        JPanel panel2=new JPanel();
        JPanel panel3=new JPanel();
        JPanel directionPanel=new JPanel();
        directionPanel.setBorder(BorderFactory.createTitledBorder("Search Direction"));
        directionPanel.add(upButton);
        directionPanel.add(downButton);
        panel1.setLayout(new GridLayout(2,1));
        panel1.add(findNextButton);
        panel1.add(cancel);
        panel2.add(findContentLabel);
        panel2.add(findText);
        panel2.add(panel1);
        panel3.add(matchCheckBox);
        panel3.add(directionPanel);
        con.add(panel2);
        con.add(panel3);
        findDialog.setSize(410,180);
        findDialog.setResizable(false);//不可调整大小
        findDialog.setLocation(230,280);
        findDialog.setVisible(true);
    }

    // select all 事件触发
    public void selectAllListen(){
        editItem1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textArea.selectAll();
            }
        });
    }

    // copy事件
    public void copyListen(){
        editItem2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textArea.requestFocus();
                String text = textArea.getSelectedText();
                StringSelection selection = new StringSelection(text);
                clipBoard.setContents(selection,null);
            }
        });
    }

    // past 事件
    public void pastListen(){
        editItem3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textArea.requestFocus();
                Transferable contents = clipBoard.getContents(this);
                if(contents == null) return;
                String text = "";
                try
                { text=(String)contents.getTransferData(DataFlavor.stringFlavor);
                }
                catch (Exception ignored)
                {
                }
                textArea.replaceRange(text, textArea.getSelectionStart(), textArea.getSelectionEnd());
            }
        });
    }

    // cut 事件
    public void cutListen(){
        editItem4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textArea.requestFocus();
                String text = textArea.getSelectedText();
                StringSelection selection = new StringSelection(text);
                clipBoard.setContents(selection,null);
                textArea.replaceRange("", textArea.getSelectionStart(), textArea.getSelectionEnd());
            }
        });
    }

    //time and date事件
    public void timeListen(){
        editItem5.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Calendar rightNow=Calendar.getInstance();
                Date date=rightNow.getTime();
                textArea.insert(date.toString(),textArea.getCaretPosition());
            }
        });
    }

    // about event
    public void aboutListen(){
        aboutItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                JOptionPane.showMessageDialog(null, "Members: Wang Kexin, Zhao Yizhen","About us", JOptionPane.PLAIN_MESSAGE);
            }
        });
    }

    //事件监听
    public void eventListener(){

        newListen();

        openListen();

        saveListen();

        exitListen();

        searchListen();

        selectAllListen();

        copyListen();

        pastListen();

        cutListen();

        timeListen();

        aboutListen();
    }

    public void readFile(File file){//读文件
        BufferedReader bReader;
        try {
            bReader=new BufferedReader(new FileReader(file));
            StringBuilder sBuffer=new StringBuilder();
            String str;
            while((str=bReader.readLine())!=null){
                sBuffer.append(str).append('\n');
                System.out.println(str);
            }
            textArea.setText(sBuffer.toString());
        } catch (Exception e) {
            // TODO: handle exception
        }
    }
    public void writeFile(String savepath){//写文件
        FileOutputStream fos;
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
}
