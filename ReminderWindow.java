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

public class ReminderWindow extends JFrame {
    
    //instance
    JTextArea contentBox;
    JTextField nameBox;
    JTextField dateBox;
    JButton save, delete;
    private Container c = getContentPane();
    Reminder currReminder;
    Checklist cl;
    Repeat r;
    boolean checklist;

    public ReminderWindow() {
        
        buttons();

        draw();

        setSize(700, 400);
        setResizable(false);
        setVisible(false);
        setLocationRelativeTo(null);

    }

    public void open(Reminder openRem) {
        currReminder = openRem;
        nameBox.setText(currReminder.getName());
        contentBox.setText(currReminder.getContent());
        dateBox.setText(currReminder.getDate());
        setVisible(true);
    }


    public void buttons() {
        save = new JButton("SAVE");
        delete = new JButton("DELETE");

        save.addActionListener(
			new ActionListener() {
                public void actionPerformed(ActionEvent e) {
	                save();
                }
            }
		);

        delete.addActionListener(
			new ActionListener() {
                public void actionPerformed(ActionEvent e) {
	                delete();
                }
            }
		);
    }

    public void save() {
        currReminder.setName(nameBox.getText());
        currReminder.setContent(contentBox.getText());
        currReminder.setDate(dateBox.getText());
        if(checklist) {
            try {
                cl.writeFile();
            } catch(IOException ioe) {
                System.out.println("IOE IN SAVE");
            }
            cl.writeReminders(0);
        } else {
            try {
                r.writeFile();
            } catch(IOException ioe) {
                System.out.println("IOE IN SAVE");
            }
            r.writeReminders(0);
        }
        setVisible(false);
    }

    public void delete() {
        if(checklist) {
            cl.remove(currReminder);
            try {
                cl.writeFile();
            } catch(IOException ioe) {
                System.out.println("IOE IN DELETE");
            }
            cl.writeReminders(0);
        } else {
            r.remove(currReminder);
            try {
                r.writeFile();
            } catch(IOException ioe) {
                System.out.println("IOE IN DELETE");
            }
            r.writeReminders(0);
        }
            setVisible(false);
    }

    public void setCL(Checklist cl) {
        this.cl = cl;
        checklist = true;
        setTitle("Normal Window");
    }

    public void setR(Repeat r) {
        this.r = r;
        checklist = false;
        setTitle("Weekly Window");
    }


    public void draw() {
        SpringLayout layout = new SpringLayout();
		JPanel p = new JPanel(layout);
		Font f = new Font( "", Font.BOLD, 25 );
		c.add(p);
        p.setBackground( new Color(173, 216, 230) );

        nameBox = new JTextField(30);
        nameBox.setBackground( Color.WHITE );
        p.add(nameBox);
        nameBox.setBorder(BorderFactory.createLineBorder(Color.BLUE, 2));
        nameBox.setFont( f );
        nameBox.setEditable( true );
        nameBox.setHorizontalAlignment(JTextField.CENTER);

        layout.putConstraint(SpringLayout.WEST, nameBox, 25, SpringLayout.WEST, p);
		layout.putConstraint(SpringLayout.NORTH, nameBox, 10, SpringLayout.NORTH, p);

        contentBox = new JTextArea(7, 30);
        contentBox.setBackground( Color.WHITE );
        p.add(contentBox);
        contentBox.setBorder(BorderFactory.createLineBorder(Color.BLUE, 2));
        contentBox.setFont( f );
        contentBox.setEditable( true );

        layout.putConstraint(SpringLayout.WEST, contentBox, 0, SpringLayout.WEST, nameBox);
		layout.putConstraint(SpringLayout.NORTH, contentBox, 10, SpringLayout.SOUTH, nameBox);

        dateBox = new JTextField(4);
        dateBox.setBackground( Color.WHITE );
        p.add(dateBox);
        dateBox.setBorder(BorderFactory.createLineBorder(Color.BLUE, 2));
        dateBox.setFont( f );
        dateBox.setEditable( true );
        dateBox.setHorizontalAlignment(JTextField.CENTER);

        layout.putConstraint(SpringLayout.EAST, dateBox, 0, SpringLayout.EAST, contentBox);
		layout.putConstraint(SpringLayout.SOUTH, dateBox, -5, SpringLayout.SOUTH, p);

        //buttons
        p.add(save);
        layout.putConstraint(SpringLayout.EAST, save, -300, SpringLayout.EAST, p);
		layout.putConstraint(SpringLayout.SOUTH, save, -5, SpringLayout.SOUTH, p);

        p.add(delete);
        layout.putConstraint(SpringLayout.WEST, delete, 10, SpringLayout.WEST, p);
		layout.putConstraint(SpringLayout.SOUTH, delete, -5, SpringLayout.SOUTH, p);
    }



}
