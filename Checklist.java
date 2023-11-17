//package Checklist;

import java.awt.image.BufferedImage;
import javax.swing.BorderFactory; 
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;

import java.util.*;


public class Checklist extends JFrame {
    
    //instance vars
    ArrayList<Reminder> theReminderList = new ArrayList<Reminder>();
    JTextField[] list;
    JButton forward, backward, newRem, repeat;
    int currPage;
    ReminderWindow rw;
    Repeat r;
    private Container c = getContentPane();

    public Checklist() {
        super("List");
        rw = new ReminderWindow();
        rw.setCL(this);
        r = new Repeat();
        r.setCL(this);


        try {
            readFile();
        } catch(FileNotFoundException fnfe) {
            System.out.println("FILE NOT FOUND");
        }

        buttons();

        draw();

        writeReminders(0);
        

        setSize(700, 510);
        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);

    }

    public void readFile() throws FileNotFoundException {
        File checkfile = new File("Checklist.txt");
        Scanner input = new Scanner(checkfile);
        input.useDelimiter(":;:");
        
        while(input.hasNext()) {
            theReminderList.add(new Reminder(input.next(), input.next(), input.next()));
        }

        input.close();
    }

    public void writeFile() throws IOException {
        File checkfile = new File("Checklist.txt");
        FileWriter output = new FileWriter(checkfile);

        output.flush();
        sort();
        for(int i = 0; i < theReminderList.size(); i++) {
            output.write(theReminderList.get(i).getName() + ":;:");
            output.write(theReminderList.get(i).getContent() + ":;:");
            output.write(theReminderList.get(i).getDate() + ":;:");
        }
        

        output.close();
    }

    public void buttons() {
        forward = new JButton("Next");
        backward = new JButton("Prev");
        newRem = new JButton("New");
        repeat = new JButton("Weekly");
        
        forward.addActionListener(
			new ActionListener() {
                public void actionPerformed(ActionEvent e) {
	                nextPage();
                }
            }
		);

        backward.addActionListener(
			new ActionListener() {
                public void actionPerformed(ActionEvent e) {
	                prevPage();
                }
            }
		);

        newRem.addActionListener(
			new ActionListener() {
                public void actionPerformed(ActionEvent e) {
	                makeNew();
                }
            }
		);

        repeat.addActionListener(
			new ActionListener() {
                public void actionPerformed(ActionEvent e) {
	                openRepeat();
                }
            }
		);

    }

    public void nextPage() {
        if(10 + 10 * currPage < theReminderList.size()) {
            writeReminders(currPage+1);
        }
    }

    public void prevPage() {
        if(currPage > 0) {
            writeReminders(currPage-1);
        }
    }

    public void makeNew() {
        Reminder nR = new Reminder("", "", "");
        theReminderList.add(nR);
        rw.open(nR);
    }

    public void openRepeat() {
        r.setVisible(true);
    }

    public void storeRepeat() {
        
        String monthDay[] = JOptionPane.showInputDialog(null, "Enter start date:", null).split("/");
        int month = 0;
        int day = 0;
        try {
            month = Integer.parseInt(monthDay[0]);
            day = Integer.parseInt(monthDay[1]);
        } catch(NumberFormatException nfe) {
            JOptionPane.showMessageDialog(null, "Must enter valid date", null, -1);
        }
        for(int i = 0; i < r.theRepeatList.size(); i++) {
            Reminder temp = new Reminder((r.theRepeatList.get(i)));
            temp.setDate(month + "/" + (day + temp.dateNum));

            theReminderList.add(temp);
        }

        sort();
        try {
            writeFile();
        } catch(IOException ioe) {
            System.out.println("IOE IN STORE");
        }
        writeReminders(0);
    }

    public void remove(Reminder rem) {
        theReminderList.remove(rem);
    }

    public void writeReminders(int page) {
        for(int i = 0; i < list.length; i++) {
            if(i + 10 * page < theReminderList.size())
			    list[i].setText(theReminderList.get(i + 10 * page).getName());
            else
                list[i].setText("");
		}
        currPage = page;
    }

    public void open(int index) {
        if(index < theReminderList.size())
            rw.open(theReminderList.get(index));
        else
            makeNew();
    }

    public void draw() {
        SpringLayout layout = new SpringLayout();
		JPanel p = new JPanel(layout);
		Font f = new Font( "", Font.BOLD, 25 );
		c.add(p);
        p.setBackground( new Color(255, 128, 100) );

        list = new JTextField[10];

        for(int i = 0; i < list.length; i++) { //textfields
			list[i] = new JTextField(30);
            
			list[i].setBackground( Color.WHITE );
			p.add(list[i]);
    		list[i].setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
    		list[i].setFont( f );
    		list[i].setEditable( false );
            list[i].setHorizontalAlignment(JTextField.CENTER);

            int x = i;
            list[i].addMouseListener(new MouseAdapter(){
                public void mouseClicked(MouseEvent e){
                    open(x + 10 * currPage);
                }
            });
		}

        layout.putConstraint(SpringLayout.WEST, list[0], 25, SpringLayout.WEST, p);
		layout.putConstraint(SpringLayout.NORTH, list[0], 10, SpringLayout.NORTH, p);
        for(int i = 1; i < list.length; i++) {
            layout.putConstraint(SpringLayout.WEST, list[i], 0, SpringLayout.WEST, list[i-1]);
            layout.putConstraint(SpringLayout.NORTH, list[i], 5, SpringLayout.SOUTH, list[i-1]);
        }  

        //buttons
        p.add(backward);
        layout.putConstraint(SpringLayout.WEST, backward, 10, SpringLayout.WEST, p);
		layout.putConstraint(SpringLayout.NORTH, backward, 15, SpringLayout.SOUTH, list[9]);

        p.add(forward);
        layout.putConstraint(SpringLayout.EAST, forward, -10, SpringLayout.EAST, p);
		layout.putConstraint(SpringLayout.NORTH, forward, 15, SpringLayout.SOUTH, list[9]);

        p.add(newRem);
        layout.putConstraint(SpringLayout.EAST, newRem, -300, SpringLayout.EAST, p);
		layout.putConstraint(SpringLayout.SOUTH, newRem, -5, SpringLayout.SOUTH, p);

        p.add(repeat);
        layout.putConstraint(SpringLayout.EAST, repeat, -450, SpringLayout.EAST, p);
		layout.putConstraint(SpringLayout.SOUTH, repeat, -5, SpringLayout.SOUTH, p);
    }

    public void sort() {
        for(int i = theReminderList.size()-1; i >= 0; i--) {
            if(theReminderList.get(i).getName().strip().equals(""))
                theReminderList.remove(i);
        }
        for(int i = 1; i < theReminderList.size(); i++) {
            for(int j = i; j > 0; j--) {
                if(theReminderList.get(j).compareTo(theReminderList.get(j-1)) < 0) {
                    Reminder temp = theReminderList.get(j);
                    theReminderList.set(j, theReminderList.get(j-1));
                    theReminderList.set(j-1, temp);
                } else {
                    break;
                }
            }
        }
    }
    

    public static void main( String args[] ) {
        Checklist cl = new Checklist();
		
        cl.addWindowListener(
            new WindowAdapter() {    		
                public void windowClosing( WindowEvent e ) {
                    System.exit(0);
	    		} 
        	}
		);
    }
}
