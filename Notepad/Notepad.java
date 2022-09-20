package notepad;

/**
 *
 * @author Asenov
 */


import java.awt.*;                 

import java.awt.event.*;          

import java.awt.datatransfer.*;   

import java.io.*;
import java.text.DateFormat;        
import java.text.SimpleDateFormat;
import java.util.Date;

public class Notepad extends Frame {    


    String filename;   

    TextArea tx;        

    Clipboard clip = getToolkit().getSystemClipboard(); 


    Notepad() {        

        setLayout(new GridLayout(1, 1));    

        tx = new TextArea();                

        add(tx);                           

        MenuBar mb = new MenuBar();    

        Menu F = new Menu("File");  

        MenuItem n = new MenuItem("New");   

        MenuItem o = new MenuItem("Open");  

        MenuItem s = new MenuItem("Save"); 

        MenuItem e = new MenuItem("Exit");  

        n.addActionListener(new New());     

        F.add(n);
        o.addActionListener(new Open());
        F.add(o);
        s.addActionListener(new Save());
        F.add(s);
        e.addActionListener(new Exit());
        F.add(e);
        mb.add(F);
        Menu E = new Menu("Edit");        

        MenuItem cut = new MenuItem("Cut");
        MenuItem copy = new MenuItem("Copy");
        MenuItem paste = new MenuItem("Paste");
        cut.addActionListener(new Cut());
        E.add(cut);
        copy.addActionListener(new Copy());
        E.add(copy);
        paste.addActionListener(new Paste());
        E.add(paste);
        mb.add(E);
        setMenuBar(mb);

        mylistener mylist = new mylistener();

        AddDate date = new AddDate();    

        addWindowListener(mylist);  
        tx.addKeyListener(date);   
    }

    class AddDate implements KeyListener {  

        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm"); 

       
        @Override
        public void keyTyped(KeyEvent e) {
        }

       @Override
        public void keyPressed(KeyEvent e) {
        }

        @Override
        public void keyReleased(KeyEvent e) { 
            Date date = new Date(); 
           
            if (e.getKeyCode() == 116) { 
                tx.append(" " + dateFormat.format(date)); 
            }
        }

    }

    class mylistener extends WindowAdapter { 


        public void windowClosing(WindowEvent e) {
            System.exit(0);                  

        }
    }

    class New implements ActionListener { 


        public void actionPerformed(ActionEvent e) {
            tx.setText(" ");              

            setTitle(filename);           

        }
    }

    class Open implements ActionListener {  


        public void actionPerformed(ActionEvent e) {
            if (tx.getText() != null && tx.getText() != "") {
                FileDialog fd = new FileDialog(Notepad.this, "Save Backup", FileDialog.SAVE); 
               
                
                fd.show();
                if (fd.getFile() != null) {     
                 

                    filename = fd.getDirectory() + fd.getFile();
                    setTitle(filename);
                    try {
                        DataOutputStream d = new DataOutputStream(new FileOutputStream(filename)); 

                        String line = tx.getText();     

                        BufferedReader br = new BufferedReader(new StringReader(line));  
                         

                        while ((line = br.readLine()) != null) {    

                            d.writeBytes(line + "\r\n");            

                            d.close();                             
                            

                        }
                    } catch (Exception ex) {
                        System.out.println("File not found");      

                    }
                }
            }
            FileDialog fd = new FileDialog(Notepad.this, "select File", FileDialog.LOAD); 

            fd.show();
            if (fd.getFile() != null) {     

                filename = fd.getDirectory() + fd.getFile();
                setTitle(filename);
                ReadFile();                

            }
            tx.requestFocus();             

        }
    }

    class Save implements ActionListener {  


        public void actionPerformed(ActionEvent e) {
            FileDialog fd = new FileDialog(Notepad.this, "Save File", FileDialog.SAVE); 
           

            fd.show();
            if (fd.getFile() != null) {    
           

                filename = fd.getDirectory() + fd.getFile();
                setTitle(filename);
                try {
                    DataOutputStream d = new DataOutputStream(new FileOutputStream(filename)); 

                    String line = tx.getText();   

                    BufferedReader br = new BufferedReader(new StringReader(line)); 
                   

                    while ((line = br.readLine()) != null) {   

                        d.writeBytes(line + "\r\n");          

                        d.close();                              
                      

                    }
                } catch (Exception ex) {
                    System.out.println("File not found");    

                }
                tx.requestFocus();                              

            }
        }
    }

    class Exit implements ActionListener {  


        public void actionPerformed(ActionEvent e) {
            System.exit(0);
        }
    }

    void ReadFile() {       

        BufferedReader d;
        StringBuffer sb = new StringBuffer();  

        try {
            d = new BufferedReader(new FileReader(filename));   

            String line;
            while ((line = d.readLine()) != null) {
                sb.append(line + "\n");
            }
            tx.setText(sb.toString());   

            d.close();               

        } catch (FileNotFoundException fe) {
            System.out.println("File not Found");
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    class Cut implements ActionListener {  


        public void actionPerformed(ActionEvent e) {
            String sel = tx.getSelectedText();
            StringSelection ss = new StringSelection(sel);
            clip.setContents(ss, ss);       

            tx.replaceRange(" ", tx.getSelectionStart(), tx.getSelectionEnd());
        }
    }

    class Copy implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            String sel = tx.getSelectedText();
            StringSelection clipString = new StringSelection(sel);
            clip.setContents(clipString, clipString);
        }
    }

    class Paste implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            Transferable cliptran = clip.getContents(Notepad.this);
            try {
                String sel = (String) cliptran.getTransferData(DataFlavor.stringFlavor);
                tx.replaceRange(sel, tx.getSelectionStart(), tx.getSelectionEnd());
            } catch (Exception exc) {
                System.out.println("not string flavour");
            }
        }
    }

    public static void main(String args[]) {   

        Frame f = new Notepad();    

        f.setSize(500, 400);       

        f.setVisible(true);         

    }
}