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


public class Repeat extends JFrame {
    
    //instance vars
    public ArrayList<Reminder> theRepeatList = new ArrayList<Reminder>();
    JTextField[] list;
    JButton forward, backward, newRem, store;
    int currPage;
    ReminderWindow rw;
    Checklist cl;
    private Container c = getContentPane();

    public Repeat() {
        super("Weekly");
        rw = new ReminderWindow();
        rw.setR(this);


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
        setVisible(false);

    }

    public void readFile() throws FileNotFoundException {
        File checkfile = new File("Repeat.txt");
        Scanner input = new Scanner(checkfile);
        input.useDelimiter(":;:");
        
        while(input.hasNext()) {
            theRepeatList.add(new Reminder(input.next(), input.next(), input.next()));
        }

        input.close();
    }

    public void writeFile() throws IOException {
        File checkfile = new File("Repeat.txt");
        FileWriter output = new FileWriter(checkfile);

        output.flush();
        sort();
        for(int i = 0; i < theRepeatList.size(); i++) {
            output.write(theRepeatList.get(i).getName() + ":;:");
            output.write(theRepeatList.get(i).getContent() + ":;:");
            output.write(theRepeatList.get(i).getDate() + ":;:");
        }
        

        output.close();
    }

    public void buttons() {
        forward = new JButton("Next");
        backward = new JButton("Prev");
        newRem = new JButton("New");
        store = new JButton("Store");
        
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

        store.addActionListener(
			new ActionListener() {
                public void actionPerformed(ActionEvent e) {
	                cl.storeRepeat();
                    setVisible(false);
                }
            }
		);
    }

    public void nextPage() {
        if(10 + 10 * currPage < theRepeatList.size()) {
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
        theRepeatList.add(nR);
        rw.open(nR);
    }

    public void remove(Reminder rem) {
        theRepeatList.remove(rem);
    }

    public void writeReminders(int page) {
        for(int i = 0; i < list.length; i++) {
            if(i + 10 * page < theRepeatList.size())
			    list[i].setText(theRepeatList.get(i + 10 * page).getName());
            else
                list[i].setText("");
		}
        currPage = page;
    }

    public void open(int index) {
        if(index < theRepeatList.size())
            rw.open(theRepeatList.get(index));
        else
            makeNew();
    }

    public void draw() {
        SpringLayout layout = new SpringLayout();
		JPanel p = new JPanel(layout);
		Font f = new Font( "", Font.BOLD, 25 );
		c.add(p);
        p.setBackground( new Color(128, 255, 100) );

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

        p.add(store);
        layout.putConstraint(SpringLayout.EAST, store, -450, SpringLayout.EAST, p);
		layout.putConstraint(SpringLayout.SOUTH, store, -5, SpringLayout.SOUTH, p);
    }

    public void sort() {
        for(int i = theRepeatList.size()-1; i >= 0; i--) {
            if(theRepeatList.get(i).getName().strip().equals(""))
            theRepeatList.remove(i);
        }
        for(int i = 1; i < theRepeatList.size(); i++) {
            for(int j = i; j > 0; j--) {
                if(theRepeatList.get(j).compareTo(theRepeatList.get(j-1)) < 0) {
                    Reminder temp = theRepeatList.get(j);
                    theRepeatList.set(j, theRepeatList.get(j-1));
                    theRepeatList.set(j-1, temp);
                } else {
                    break;
                }
            }
        }
    }

    public void setCL(Checklist cl) {
        this.cl = cl;
    }
    
}