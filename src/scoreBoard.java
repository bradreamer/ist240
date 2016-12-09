
import java.util.Date;
import java.util.Enumeration;
import java.util.Vector;
import javax.swing.BorderFactory;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Paul, Jasmeet
 */
public class scoreBoard {

    private Vector scores;

    String scoreFile = "scoresheet.xml";

    public scoreBoard() {
        XML_240 readScore; // XML reader for score
        readScore = new XML_240();
        readScore.openReaderXML(scoreFile);
        Object v = readScore.ReadObject();
        if (Vector.class.isInstance(v)) {
            scores = (Vector) v;
        }
        else {
            scores = new Vector();
        }
        readScore.closeReaderXML();
    }

    /**
     * @return the score
     */
    public Vector getScores() {
        return scores;
    }

    public void addScore(options o) {
        scores.add(o);
    }

    public JScrollPane displayXML() {
        DefaultTableModel model = new DefaultTableModel(0, 0);
        String[] cols = {"Name", "Difficulty", "Score"};
        model.setColumnIdentifiers(cols);
        JTable jt = new JTable();
        jt.setModel(model);
        jt.setAutoCreateRowSorter(true);
        jt.enable(false);

        Enumeration e = scores.elements(); 

        while(e.hasMoreElements()) {
            options o= (options) e.nextElement();
            Vector<Object> data = new Vector<Object>();
            data.add(o.getName());
            data.add(o.getDifficulty());
            data.add(String.format("%05d", o.getScore()));
            model.addRow(data);
        }

        //Create the scroll pane and add the table to it.
        JScrollPane scrollPane = new JScrollPane(jt);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Score Board [Click on Header to Sort]"));
        return scrollPane;
    }
    
    public void saveXML(){
        XML_240 saveScore; // XML reader for score
        saveScore = new XML_240();
        saveScore.openWriterXML(scoreFile);
        saveScore.writeObject(scores);
        saveScore.closeWriterXML();
    }
}
