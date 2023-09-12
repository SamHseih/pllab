package version_AST;

import java.io.File;
import java.io.IOException;
import java.net.SocketException;

import ccu.pllab.tcgen.AbstractSyntaxTree.ClassifierContext;
import ccu.pllab.tcgen.AbstractSyntaxTree.OperationContext;
import ccu.pllab.tcgen.AbstractSyntaxTree.PackageExp;
import ccu.pllab.tcgen.AbstractSyntaxTree.StereoType;
import ccu.pllab.tcgen.DataWriter.DataWriter;
import ccu.pllab.tcgen.transform.OCL2AST;
import version_control.FileHandleToServer;

public class ReduceASTCreate {
	private String classname;
	private ReduceClassExp reduceAST;//完整ast 建成 '比較(reduce)'要用的ast
	private PackageExp completeAST;//完整ast
	private OperationContext OperationContextExp;
	private StereoType stereoType;
	Boolean hasClassifier = false;	
	
	public static void main(String[] args) throws SocketException, IOException {
		ReduceClassExp a = new ReduceClassExp(FileHandleToServer.readFile("Date//version 1//", "DateAST2UML.uml"));
		DataWriter.writeInfo(ast2xml(a), "ast", "uml", "C:\\Users\\user\\Desktop","uml");
		System.out.println("finish");
	}
	
	public ReduceASTCreate(File ocl, String classname) throws Exception {
		OCL2AST makeReduceAST = new OCL2AST(ocl);
		this.classname = classname;
		this.completeAST = (PackageExp) makeReduceAST.getAbstractSyntaxTree();
		//start change to reduce AST
		this.reduceAST = new ReduceClassExp(completeAST.getpackageName(),classname);
		for(int index = 0; index < completeAST.getTreeNode().size(); index++) {
			if(completeAST.getTreeNode().get(index).getExpreesion().equals("OperationContext")) {
				OperationContextExp = (OperationContext) completeAST.getTreeNode().get(index);
				this.reduceAST.addMethodChildNode(new ReduceMethodExp(OperationContextExp.getMethodName(), OperationContextExp.getReturnType(), OperationContextExp.getParameters()));
				for(int i = 0; i < OperationContextExp.getStereoType().size(); i++) {
					this.stereoType = OperationContextExp.getStereoType().get(i);
					if(hasClassifier) {
						this.reduceAST.getMethodNode().get(index-1).addReduceStereoType(this.stereoType.getStereoType(), this.stereoType.getException());
						this.reduceAST.getMethodNode().get(index-1).getReduceStereoType().get(i).setExpression(this.stereoType.getExpression());
					}
					else {
						this.reduceAST.getMethodNode().get(index).addReduceStereoType(this.stereoType.getStereoType(), this.stereoType.getException());
						this.reduceAST.getMethodNode().get(index).getReduceStereoType().get(i).setExpression(this.stereoType.getExpression());
					}
				}							
			}else if(completeAST.getTreeNode().get(index).getExpreesion().equals("ClassifierContext")) {
				this.reduceAST.addClassifierNode();
				this.stereoType = ((ClassifierContext)completeAST.getTreeNode().get(index)).getInv();
				this.reduceAST.getReduceClassifierNode().addReduceStereoType(this.stereoType.getStereoType(), this.stereoType.getException());
				this.reduceAST.getReduceClassifierNode().getReduceStereoType().setExpression(this.stereoType.getExpression());
				hasClassifier = true;
			}
		}
		System.out.println("Create ReduceAST success!");
	}
	
	public String getClassname() {
		return this.classname;
	}
	
	public ReduceClassExp getReduceAST() {
		return this.reduceAST;
	}
	
	public static String ast2xml(ReduceClassExp p) {
		String astcontent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
		int spacecnt = 0;
		astcontent = p.genast2xml(astcontent, spacecnt);
		System.out.println("ast2xml string success!");
		return astcontent;
		//DataWriter.writeInfo(astcontent, "ast2xml", "uml", "C:\\Users\\user\\Desktop","uml");
	}
}
