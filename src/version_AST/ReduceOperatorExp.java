package version_AST;

import java.util.ArrayList;

import ccu.pllab.tcgen.AbstractSyntaxTree.*;

public class ReduceOperatorExp extends ReduceASTnode{
	private ReduceASTnode leftOperand;
	private String operator;
	private ReduceASTnode rightOperand;
	
	public ReduceOperatorExp(OperatorExp operator) {
		super();
		this.operator = operator.getOperator();
		setLeftOperand(operator.getLeftOperand());
		setRightOperand(operator.getRightOperand());
	}
	
	public ReduceOperatorExp(ArrayList<String> leftOperand, String operator, ArrayList<String> rightOperand) {
		super();
		setAllKindOperatorExp("leftOperand", leftOperand);
		this.operator = operator.split("\"")[1];
		setAllKindOperatorExp("rightOperand", rightOperand);
	}
	
	public void setAllKindOperatorExp(String kind, ArrayList<String> expression) {
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
				case "leftOperand":
					this.leftOperand = new ReduceOperatorExp(leftcontent, operatorContent, rightcontent);
					break;
				case "rightOperand":
					this.rightOperand = new ReduceOperatorExp(leftcontent, operatorContent, rightcontent);
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
				case "leftOperand":
					this.leftOperand = new ReduceIfExp(ifcontent, thencontent, elsecontent);
					break;
				case "rightOperand":
					this.rightOperand = new ReduceIfExp(ifcontent, thencontent, elsecontent);
					break;
			}
			ifcontent.clear();
			thencontent.clear();
			elsecontent.clear();
		}
		else if(expression.get(0).contains("<parameter>")) {
			switch (kind) {
				case "leftOperand":
					this.leftOperand = new ReducePropertyCallExp(expression.get(0));
					break;
				case "rightOperand":
					this.rightOperand = new ReducePropertyCallExp(expression.get(0));
					break;
			}
		}
		else { //expression.get(0).contains("<literal>")
			switch (kind) {
				case "leftOperand":
					this.leftOperand = new ReduceLiteralExp(expression.get(0));
					break;
				case "rightOperand":
					this.rightOperand = new ReduceLiteralExp(expression.get(0));
					break;
			}
		}
	}
	
	public ReduceASTnode getLeftOperand() {
		return this.leftOperand;
	}
	
	public String getOperator() {
		return this.operator;
	}
	
	public ReduceASTnode getRightOperand() {
		return this.rightOperand;
	}
	
	public void setLeftOperand(AbstractSyntaxTreeNode leftnode) {
		if(leftnode.getExpreesion().equals("IfExp")) {
			this.leftOperand = new ReduceIfExp((IfExp)leftnode);
		}
		else if( leftnode.getExpreesion().equals("OperatorExp")) {
			this.leftOperand = new ReduceOperatorExp((OperatorExp)leftnode);
		}
		else if(leftnode.getExpreesion().equals("PropertyCallExp")) {
			this.leftOperand = new ReducePropertyCallExp(((PropertyCallExp)leftnode).getType(), ((PropertyCallExp)leftnode).getVariable());
		}
		else if(leftnode.getExpreesion().equals("LiteralExp")){ //LiteralExp
			this.leftOperand = new ReduceLiteralExp(((LiteralExp)leftnode).getValue(), ((LiteralExp)leftnode).getType());
		}
		else {
			System.out.println("no operator type");
		}
	}
	
	public void setRightOperand(AbstractSyntaxTreeNode rightnode) {
		if(rightnode.getExpreesion().equals("IfExp")) {
			this.rightOperand = new ReduceIfExp((IfExp)rightnode);
		}
		else if( rightnode.getExpreesion().equals("OperatorExp")) {
			this.rightOperand = new ReduceOperatorExp((OperatorExp)rightnode);
		}
		else if(rightnode.getExpreesion().equals("PropertyCallExp")) {
			this.rightOperand = new ReducePropertyCallExp(((PropertyCallExp)rightnode).getType(), ((PropertyCallExp)rightnode).getVariable());
		}
		else if(rightnode.getExpreesion().equals("LiteralExp")){ //LiteralExp
			this.rightOperand = new ReduceLiteralExp(((LiteralExp)rightnode).getValue(), ((LiteralExp)rightnode).getType());
		}
		else {
			System.out.println("no operator type");
		}
	}
	
	public String genast2xml(String xmlcontent, int structurelevel) {
		for(int cnt = 0; cnt < structurelevel; cnt++) {xmlcontent+="  ";}
		xmlcontent += "<leftOperand>\n";
		structurelevel++;
		xmlcontent = this.leftOperand.genast2xml(xmlcontent, structurelevel);
		structurelevel--;
		for(int cnt = 0; cnt < structurelevel; cnt++) {xmlcontent+="  ";}
		xmlcontent += "</leftOperand>\n";
		for(int cnt = 0; cnt < structurelevel; cnt++) {xmlcontent+="  ";}
		xmlcontent += "<operator name=\""+ this.operator +"\"/>\n";
		for(int cnt = 0; cnt < structurelevel; cnt++) {xmlcontent+="  ";}
		xmlcontent += "<rightOperand>\n";
		structurelevel++;
		xmlcontent = this.rightOperand.genast2xml(xmlcontent, structurelevel);
		structurelevel--;
		for(int cnt = 0; cnt < structurelevel; cnt++) {xmlcontent+="  ";}
		xmlcontent += "</rightOperand>\n";	
		
		return xmlcontent;
	}
	
	public String getExpreesion() {
		return "operatorExp";
	}
}
