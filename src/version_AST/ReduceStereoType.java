package version_AST;

import java.util.ArrayList;

import ccu.pllab.tcgen.AbstractSyntaxTree.*;

public class ReduceStereoType extends ReduceASTnode{
	private String stereoType;
	private String exception;
	private ReduceASTnode expression;
	
	public ReduceStereoType(String stereotype, String exception) {
		super();
		this.stereoType = stereotype;
		this.exception = exception;
	}
	
	
	public String getStereoType() {
		return this.stereoType;
	}
	
	public String getException() {
		return this.exception;
	}
	
	public ReduceASTnode getExpression() {
		return this.expression;
	}
	
	public void setExpression(ArrayList<String> expression) {
		int cnt = 1;
		String strcutlevel = expression.get(0).split("<")[0];
		if(expression.get(0).contains("leftOperand")) {
			ArrayList<String> leftcontent = new ArrayList<String>();
			String operatorContent;
			ArrayList<String> rightcontent = new ArrayList<String>();
			//��leftOperand����
			while(!expression.get(cnt).split("<")[0].equals(strcutlevel)) {
				leftcontent.add(expression.get(cnt++));
			}
			//��Operator����
			cnt++; //���L </leftOperand>
			operatorContent = expression.get(cnt++);
			//��rightOperand����
			cnt++; //���L <rightOperand>
			while(cnt != (expression.size()-1)) {
				rightcontent.add(expression.get(cnt++));
			}
			this.expression = new ReduceOperatorExp(leftcontent, operatorContent, rightcontent);
			leftcontent.clear();
			rightcontent.clear();
		}
		else if(expression.get(0).contains("<if>")) {
			ArrayList<String> ifcontent = new ArrayList<String>();
			ArrayList<String> thencontent = new ArrayList<String>();
			ArrayList<String> elsecontent = new ArrayList<String>();
			//��if����
			while(!expression.get(cnt).split("<")[0].equals(strcutlevel)) {
				ifcontent.add(expression.get(cnt++));
			}
			//��then����
			cnt++; //���L</if>
			cnt++; //���L<then>
			while(!expression.get(cnt).split("<")[0].equals(strcutlevel)) {
				thencontent.add(expression.get(cnt++));
			}
			//��else����
			cnt++; //���L</then>
			cnt++; //���L<else>
			while(cnt != (expression.size()-1)) {
				elsecontent.add(expression.get(cnt++));
			}
			this.expression = new ReduceIfExp(ifcontent, thencontent, elsecontent);
			ifcontent.clear();
			thencontent.clear();
			elsecontent.clear();
		}
		else if(expression.get(0).contains("<parameter>")) {
			this.expression = new ReducePropertyCallExp(expression.get(0));
		}
		else { //expression.get(0).contains("<literal>")
			this.expression = new ReduceLiteralExp(expression.get(0));
		}
	}
	
	public void setExpression(AbstractSyntaxTreeNode expressionNode) {
		if(expressionNode.getExpreesion().equals("IfExp")) {
			this.expression = new ReduceIfExp((IfExp)expressionNode);
		}
		else if( expressionNode.getExpreesion().equals("OperatorExp")) {
			this.expression = new ReduceOperatorExp((OperatorExp)expressionNode);
		}
		else if(expressionNode.getExpreesion().equals("PropertyCallExp")) {
			this.expression = new ReducePropertyCallExp(((PropertyCallExp)expressionNode).getType(), ((PropertyCallExp)expressionNode).getVariable());
		}
		else if(expressionNode.getExpreesion().equals("LiteralExp")){ //LiteralExp
			this.expression = new ReduceLiteralExp(((LiteralExp)expressionNode).getValue(), ((LiteralExp)expressionNode).getType());
		}
		else {
			System.out.println("no operator type");
		}
	}
	
	public String genast2xml(String xmlcontent, int structurelevel) {
		for(int cnt = 0; cnt < structurelevel; cnt++) {xmlcontent+="  ";}
		xmlcontent += "<stereotype name=\""+this.stereoType+"\" exception=\""+this.exception+"\">\n";
		structurelevel++;
		xmlcontent = expression.genast2xml(xmlcontent, structurelevel);
		structurelevel--;
		for(int cnt = 0; cnt < structurelevel; cnt++) {xmlcontent+="  ";}
		xmlcontent += "</stereotype>\n";
		return xmlcontent;
	}
	
	public String getExpreesion() {
		return "stereoExp";
	}
}
