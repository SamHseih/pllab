package version_control;

import java.awt.Color;
import java.io.File;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

public class TextCompare {

	public static void textcompare(JTextPane tp1,JTextPane tp2,JTextPane different) throws BadLocationException {	
		File file = new File("./classes/test/icon.gif");   
	    Icon image = new ImageIcon(file.getAbsoluteFile().toString()); 
	    String dif = "You use: word compare.\ndifferent line: \n";
	    
		String[] tp1string = (tp1.getText()+"\n").split("\n");
		String[] tp2string = (tp2.getText()+"\n").split("\n");
		tp1.setText("");  tp2.setText("");		
		tp1.insertIcon(image);
		tp2.insertIcon(image);
		SimpleAttributeSet attrSet1 = new SimpleAttributeSet();
		StyleConstants.setForeground(attrSet1,Color.red);
		SimpleAttributeSet attrSet2 = new SimpleAttributeSet();
		StyleConstants.setForeground(attrSet2,Color.black);
		Document doc1  = tp1.getDocument();
		Document doc2  = tp2.getDocument();
		for(int index = 0; index < Math.min(tp1string.length, tp2string.length); index++) {
			if(!tp1string[index].equals(tp2string[index])) {
				doc1.insertString(doc1.getLength(),tp1string[index]+"\n", attrSet1);
				doc2.insertString(doc2.getLength(),tp2string[index]+"\n", attrSet1);
				dif+=index+",\n";
			}
			else {
				doc1.insertString(doc1.getLength(),tp1string[index]+"\n", attrSet2);
				doc2.insertString(doc2.getLength(),tp2string[index]+"\n", attrSet2);
			}
		}
		//for某一textpane剩下行數新增
		if(tp1string.length > tp2string.length) {
			for(int index = tp2string.length; index < tp1string.length; index++) {
				doc1.insertString(doc1.getLength(),tp1string[index]+"\n", attrSet1);
				dif+=index+",\n";
			}
		}
		else if(tp1string.length < tp2string.length) {
			for(int index = tp1string.length; index < tp2string.length; index++) {
				doc2.insertString(doc2.getLength(),tp2string[index]+"\n", attrSet1); 
				dif+=index+",\n";
			}
		}
		//字體變黑
		doc1.insertString(doc1.getLength(), "", attrSet2);
		doc2.insertString(doc1.getLength(), "", attrSet2);
		different.setText(dif);
	}
	
	public static void newdeletecompare(JTextPane tp1,JTextPane tp2,JTextPane different) throws BadLocationException {
		File file = new File("./classes/test/icon.gif");   
	    Icon image = new ImageIcon(file.getAbsoluteFile().toString()); 
	    String dif = "You use: new/delete compare.\n";
	    String newstring = "new line: \n";
	    String deletestring = "delete line: \n";
		String[] tp1string = (tp1.getText()+"\n").split("\n");
		String[] tp2string = (tp2.getText()+"\n").split("\n");
		tp2.setText("");		
		tp1.insertIcon(image);
		tp2.insertIcon(image);
		SimpleAttributeSet attrSet1 = new SimpleAttributeSet();
		StyleConstants.setForeground(attrSet1,Color.red); //delete
		SimpleAttributeSet attrSet2 = new SimpleAttributeSet();
		StyleConstants.setForeground(attrSet2,Color.black);
		SimpleAttributeSet attrSet3 = new SimpleAttributeSet();
		StyleConstants.setForeground(attrSet3,Color.blue); //new
		Document doc  = tp2.getDocument();
		int cnt = 0;
		int cnt2 = 0;
		for(int index = 0; index < tp2string.length; index++) {		//ex: a.b.c.d.e  compare with a.b.n.c.e.f
			for(int j = cnt; j < tp1string.length; j++) {
				if(tp1string[j].equals(tp2string[index])) {
					for(int k = cnt2; k < j; k++) { //line: delete
						deletestring += (k+1) + ",\n";
						doc.insertString(doc.getLength(),tp1string[k]+"\n", attrSet1);
					}
					doc.insertString(doc.getLength(),tp2string[index]+"\n", attrSet2);  //line: same
					cnt = j+1;
					break;
				}
			}
			if(cnt == cnt2) {
				newstring += (index+1) + ",\n";
				doc.insertString(doc.getLength(),tp2string[index]+"\n", attrSet3); //line: new
			}
			cnt2 = cnt;
		}
		doc.insertString(doc.getLength(), "", attrSet2);
		different.setText(dif+newstring+"\n"+deletestring+"\n");
	}
	
	public static void addtextlinenumber(JTextPane tp1, JTextPane tp2) {
		String[] t = (tp2.getText()).split("\n");
		String settext = "";
		for(int index = 0; index < t.length; index++) {
			if(!t[index].equals("\n"))
				settext+=(index+1)+"\n";
		}
		tp1.setText(settext);
		tp1.setCaretPosition(0);  tp2.setCaretPosition(0);	//設置滾輪條最上面
	}
}
