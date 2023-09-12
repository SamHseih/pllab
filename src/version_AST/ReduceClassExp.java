package version_AST;

import java.util.ArrayList;

import ccu.pllab.tcgen.AbstractSyntaxTree.*;

public class ReduceClassExp extends ReduceASTnode{
	String packageName;
	String className;
	Boolean hasClassifier = false;
	private ReduceClassifierExp classifier;
	private ArrayList<ReduceMethodExp> context = new ArrayList<ReduceMethodExp>();
	
	public ReduceClassExp(String packageName,String className)
	{
		super();
		this.packageName=packageName;
		this.className=className;
	}
	
	public ReduceClassExp(String ast2xml)
	{
		super();
		String[] s = ast2xml.split("\n");
		ArrayList<String> expressionContent = new ArrayList<String>();
		if(s[0].contains("?xml")) { //確認是uml檔案的內容
			this.packageName = (s[1].split("\""))[1]; //<Package name="XXX">
			this.className = (s[2].split("\""))[1];
			for(int index = 3; index < s.length; index++) { //把classifier跟method做處理

				if(s[index].contains("classifier"))
				{
					hasClassifier = true;
					this.classifier = new ReduceClassifierExp((s[index].split("\""))[1]);
					this.classifier.addReduceStereoType("invariant",(s[++index].split("\""))[3]);
					while(!s[++index].contains("</stereotype")) {// 字串</stereotype> 前的expression
						expressionContent.add(s[index]);
					}
					this.classifier.getReduceStereoType().setExpression(expressionContent);
					expressionContent.clear();
					index++; //跳過</>
				}
				else if(s[index].contains("method"))
				{
					while(!s[index].contains("</method")) {// 字串</method> 前的expression
						expressionContent.add(s[index++]);
					}
					this.context.add(new ReduceMethodExp(expressionContent));
					expressionContent.clear();
				}
				else {
					//nothing to do
				}
			}
		}
		else {
			System.out.println("wrong uml file!");
		}
	}
	
	public ArrayList<ReduceMethodExp> getMethodNode()
	{
		return this.context;
	}
	
	public void addClassifierNode() {
		hasClassifier = true;
		classifier = new ReduceClassifierExp(this.className);
	}
	
	public ReduceClassifierExp getReduceClassifierNode() {
		return this.classifier;
	}
	
	public void addMethodChildNode(ReduceMethodExp childnode) {
		this.context.add(childnode);
	}
	
	public String genast2xml(String xmlcontent, int structurelevel) {
		for(int cnt = 0; cnt < structurelevel; cnt++) {xmlcontent+="  ";}
		xmlcontent += "<Package name=\"" + this.packageName+"\">\n";
		structurelevel++;
		for(int cnt = 0; cnt < structurelevel; cnt++) {xmlcontent+="  ";}
		xmlcontent += "<class name=\""+this.className+"\">\n";
		structurelevel++;
		if(hasClassifier) 
			xmlcontent = this.classifier.genast2xml(xmlcontent, structurelevel);
		for(int cnt = 0; cnt < this.context.size(); cnt++) {			
			xmlcontent = this.getMethodNode().get(cnt).genast2xml(xmlcontent, structurelevel);
		}
		structurelevel--;
		for(int cnt = 0; cnt < structurelevel; cnt++) {xmlcontent+="  ";}
		xmlcontent += "</class>\n";
		structurelevel--;
		for(int cnt = 0; cnt < structurelevel; cnt++) {xmlcontent+="  ";}
		xmlcontent += "</Package>\n";
		
		return xmlcontent;
	}
	
	public AbstractSyntaxTreeNode ReduceAST2AST(ReduceClassExp reduceAST) {
		PackageExp completeAST = new PackageExp(reduceAST.packageName);
		
		if(reduceAST.hasClassifier) { //有建構值
			completeAST.addTreeChildNode(new ClassifierContext(reduceAST.className));
			//inv
			ClassifierContext classifierExp= (ClassifierContext)completeAST.getTreeNode().get(0);
			classifierExp.setStereoType(new StereoType("invariant"));
			if(reduceAST.getReduceClassifierNode().getReduceStereoType().getException() != null) {
				classifierExp.getInv().setException(reduceAST.getReduceClassifierNode().getReduceStereoType().getException());
			}
			//expression
			classifierExp.getInv().setExpression(reduceAST.getReduceClassifierNode().getReduceStereoType().getExpression());
		}
		for(int i = 0; i < reduceAST.getMethodNode().size(); i++) { //method
			ReduceMethodExp methodNode = reduceAST.getMethodNode().get(i);
			completeAST.addTreeChildNode(new OperationContext(reduceAST.className, methodNode.getMethodName()));
			OperationContext operationExp;
			if(reduceAST.hasClassifier)
			   {operationExp = (OperationContext)completeAST.getTreeNode().get(i+1);}
			else {operationExp = (OperationContext)completeAST.getTreeNode().get(i);}
			//參數 
			for(int j = 0; j < reduceAST.getMethodNode().get(i).getParameternode().size(); j++) {
				operationExp.setParameters2(new PropertyCallExp(reduceAST.getMethodNode().get(i).getParameternode().get(j).getVariable()));
				operationExp.getParameters().get(j).setType(reduceAST.getMethodNode().get(i).getParameternode().get(j).getType());
			}
			//return
			operationExp.setReturnType(reduceAST.getMethodNode().get(i).getReturnTypeName());
			//stereo 
			for(int j = 0; j < methodNode.getReduceStereoType().size(); j++) {
				operationExp.setStereoType(new StereoType(methodNode.getReduceStereoType().get(j).getStereoType()));
				operationExp.getStereoType().get(j).setException(methodNode.getReduceStereoType().get(j).getException());
				// expression
				operationExp.getStereoType().get(j).setExpression(methodNode.getReduceStereoType().get(j).getExpression());
			}
		}
		
		
		
		return completeAST;
	}
	
	public String getExpreesion() {
		return "classExp";
	}
}
