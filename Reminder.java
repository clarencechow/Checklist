//package Checklist;

public class Reminder implements Comparable<Reminder>{
    String name;
    String content;
    String date; 
    int dateNum;

    public Reminder(String name, String content, String date) {
        this.name = name;
        this.content = content;
        this.date = date;
        try {
            dateNum = Integer.parseInt(date.replace("/", "").strip());
        } catch(NumberFormatException nfe) {
            dateNum = 0;
        }
    }

    public Reminder(Reminder rem) {
        this.name = rem.name;
        this.content = rem.content;
        this.date = rem.date;
        try {
            dateNum = Integer.parseInt(date.replace("/", "").strip());
        } catch(NumberFormatException nfe) {
            dateNum = 0;
        }
    }

    public String getName() {
        return name;
    }

    public String getContent() {
        return content;
    }

    public String getDate() {
        return date;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setContent(String content) {
        this.content = content;
    }
    
    public void setDate(String date) {
        this.date = date;
        try {
            String splitDate[] = date.split("/");
            if(splitDate.length < 2) {
                dateNum = 0;
                return;
            }
            if(splitDate[1].length() < 2)
                splitDate[1] = "0" + splitDate[1];
            dateNum = Integer.parseInt(splitDate[0] + splitDate[1]);
        } catch(NumberFormatException nfe) {
            dateNum = 0;
        }
    }

    public int compareTo(Reminder rem) {
        if(this.dateNum != rem.dateNum)
            return this.dateNum - rem.dateNum;
        String combine = name+content;
        return combine.compareTo(rem.name + rem.content);
    }
}
