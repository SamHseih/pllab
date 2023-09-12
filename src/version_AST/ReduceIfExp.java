package version_AST;

import java.util.ArrayList;

import ccu.pllab.tcgen.AbstractSyntaxTree.*;

public class ReduceIfExp extends ReduceASTnode{
	private ReduceASTnode ifconditionExp;
	private ReduceASTnode elseExp;
	private ReduceASTnode thenExp;
	
	public ReduceIfExp(IfExp ifExp) {
		super();
		setIfExp(ifExp.getCondition());
		setThenExp(ifExp.getThen());
		setElseExp(ifExp.getElse());
	}
	
	public ReduceIfExp(ArrayList<String> ifcontent, ArrayList<String> thencontent, ArrayList<String> elsecontent) {
		super();
		setAllKindIfExp("if", ifcontent);
		setAllKindIfExp("then", thencontent);
		setAllKindIfExp("else", elsecontent);
	}
	
	public ReduceASTnode getIfExp() {
		return this.ifconditionExp;
	}
	
	public ReduceASTnode getElseExp() {
		return this.elseExp;
	}
	
	public ReduceASTnode getThenExp() {
		return this.thenExp;
	}
	
	public void setAllKindIfExp(String kind, ArrayList<String> expression) {
		int cnt = 1;
		String strcutlevel = expression.get(0).split("<")[0];
		if(expression.get(0).contains("leftOperand")) {
			ArrayList<String> leftcontent = new ArrayList<String>();
			String operatorContent;
			ArrayList<String> rightcontent = new ArrayList<String>();
			//leftOperand场だ
			while(!expression.get(cnt).split("<")[0].equals(strcutlevel)) {
				leftcontent.add(expression.get(cnt++));
			}
			//Operator场だ
			cnt++; //铬筁 </leftOperand>
			operatorContent = expression.get(cnt++);
			//rightOperand场だ
			cnt++; //铬筁 <rightOperand>
			while(cnt != (expression.size()-1)) {
				rightcontent.add(expression.get(cnt++));
			}
			switch (kind) {
				case "if":
					this.ifconditionExp = new ReduceOperatorExp(leftcontent, operatorContent, rightcontent);
					break;
				case "then":
					this.thenExp = new ReduceOperatorExp(leftcontent, operatorContent, rightcontent);
					break;
				case "else":
					this.elseExp = new ReduceOperatorExp(leftcontent, operatorContent, rightcontent);
					break;
			}
			leftcontent.clear();
			rightcontent.clear();
		}
		else if(expression.get(0).contains("<if>")) {
			ArrayList<String> ifcontent = new ArrayList<String>();
			ArrayList<String> thencontent = new ArrayList<String>();
			ArrayList<String> elsecontent = new ArrayList<String>();
			//if场だ
			while(!expression.get(cnt).split("<")[0].equals(strcutlevel)) {
				ifcontent.add(expression.get(cnt++));
			}
			//then场だ
			cnt++; //铬筁</if>
			cnt++; //铬筁<then>
			while(!expression.get(cnt).split("<")[0].equals(strcutlevel)) {
				thencontent.add(expression.get(cnt++));
			}
			//else场だ
			cnt++; //铬筁</then>
			cnt++; //铬筁<else>
			while(cnt != (expression.size()-1)) {
				elsecontent.add(expression.get(cnt++));
			}
			switch (kind) {
				case "if":
					this.ifconditionExp = new ReduceIfExp(ifcontent, thencontent, elsecontent);
					break;
				case "then":
					this.thenExp = new ReduceIfExp(ifcontent, thencontent, elsecontent);
					break;
				case "else":
					this.elseExp = new ReduceIfExp(ifcontent, thencontent, elsecontent);
					break;
			}
			ifcontent.clear();
			thencontent.clear();
			elsecontent.clear();
		}
		else if(expression.get(0).contains("<parameter>")) {
			switch (kind) {
				case "if":
					this.ifconditionExp = new ReducePropertyCallExp(expression.get(0));
					break;
				case "then":
					this.thenExp = new ReducePropertyCallExp(expression.get(0));
					break;
				case "else":
					this.elseExp = new ReducePropertyCallExp(expression.get(0));
					break;
			}
		}
		else { //expression.get(0).contains("<literal>")
			switch (kind) {
				case "if":
					this.ifconditionExp = new ReduceLiteralExp(expression.get(0));
					break;
				case "then":
					this.thenExp = new ReduceLiteralExp(expression.get(0));
					break;
				case "else":
					this.elseExp = new ReduceLiteralExp(expression.get(0));
					break;
			}
		}
	}
	
	public void setIfExp(AbstractSyntaxTreeNode ifconditionExp) {
		if(ifconditionExp.getExpreesion().equals("IfExp")) {
			this.ifconditionExp = new ReduceIfExp((IfExp)ifconditionExp);
		}
		else if( ifconditionExp.getExpreesion().equals("OperatorExp")) {
			this.ifconditionExp = new ReduceOperatorExp((OperatorExp)ifconditionExp);
		}
		else if(ifconditionExp.getExpreesion().equals("PropertyCallExp")) {
			this.ifconditionExp = new ReducePropertyCallExp(((PropertyCallExp)ifconditionExp).getType(), ((PropertyCallExp)ifconditionExp).getVariable());
		}
		else if(ifconditionExp.getExpreesion().equals("LiteralExp")){ //LiteralExp
			this.ifconditionExp = new ReduceLiteralExp(((LiteralExp)ifconditionExp).getValue(), ((LiteralExp)ifconditionExp).getType());
		}
		else {
			System.out.println("no operator type");
		}
	}
	
	public void setThenExp(AbstractSyntaxTreeNode thenExp) {
		if(thenExp.getExpreesion().equals("IfExp")) {
			this.thenExp = new ReduceIfExp((IfExp)thenExp);
		}
		else if( thenExp.getExpreesion().equals("OperatorExp")) {
			this.thenExp = new ReduceOperatorExp((OperatorExp)thenExp);
		}
		else if(thenExp.getExpreesion().equals("PropertyCallExp")) {
			this.thenExp = new ReducePropertyCallExp(((PropertyCallExp)thenExp).getType(), ((PropertyCallExp)thenExp).getVariable());
		}
		else if(thenExp.getExpreesion().equals("LiteralExp")){ //LiteralExp
			this.thenExp = new ReduceLiteralExp(((LiteralExp)thenExp).getValue(), ((LiteralExp)thenExp).getType());
		}
		else {
			System.out.println("no operator type");
		}
	}
	
	public void setElseExp(AbstractSyntaxTreeNode elseExp) {
		if(elseExp.getExpreesion().equals("IfExp")) {
			this.elseExp = new ReduceIfExp((IfExp)elseExp);
		}
		else if( elseExp.getExpreesion().equals("OperatorExp")) {
			this.elseExp = new ReduceOperatorExp((OperatorExp)elseExp);
		}
		else if(elseExp.getExpreesion().equals("PropertyCallExp")) {
			this.elseExp = new ReducePropertyCallExp(((PropertyCallExp)elseExp).getType(), ((PropertyCallExp)elseExp).getVariable());
		}
		else if(elseExp.getExpreesion().equals("LiteralExp")){ //LiteralExp
			this.elseExp = new ReduceLiteralExp(((LiteralExp)elseExp).getValue(), ((LiteralExp)elseExp).getType());
		}
		else {
			System.out.println("no operator type");
		}
	}
	
	public String genast2xml(String xmlcontent, int structurelevel) {
		for(int cnt = 0; cnt < structurelevel; cnt++) {xmlcontent+="  ";}
		xmlcontent += "<if>\n";
		structurelevel++;
		xmlcontent = this.ifconditionExp.genast2xml(xmlcontent, structurelevel);
		structurelevel--;
		for(int cnt = 0; cnt < structurelevel; cnt++) {xmlcontent+="  ";}
		xmlcontent += "</if>\n";
		for(int cnt = 0; cnt < structurelevel; cnt++) {xmlcontent+="  ";}
		xmlcontent += "<then>\n";
		structurelevel++;
		xmlcontent = this.thenExp.genast2xml(xmlcontent, structurelevel);
		structurelevel--;
		for(int cnt = 0; cnt < structurelevel; cnt++) {xmlcontent+="  ";}
		xmlcontent += "</then>\n";
		for(int cnt = 0; cnt < structurelevel; cnt++) {xmlcontent+="  ";}
		xmlcontent += "<else>\n";
		structurelevel++;
		xmlcontent = this.elseExp.genast2xml(xmlcontent, structurelevel);
		structurelevel--;
		for(int cnt = 0; cnt < structurelevel; cnt++) {xmlcontent+="  ";}
		xmlcontent += "</else>\n";
		
		return xmlcontent;
	}
	public String getExpreesion() {
		return "ifExp";
	}
}
