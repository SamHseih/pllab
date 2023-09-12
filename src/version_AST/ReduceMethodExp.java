package version_AST;

import java.util.ArrayList;

import ccu.pllab.tcgen.AbstractSyntaxTree.PropertyCallExp;

public class ReduceMethodExp extends ReduceASTnode{
	private String methodName;
	private String returntype = "";
	private ArrayList<ReducePropertyCallExp> parameters=new ArrayList<ReducePropertyCallExp>();
	private ArrayList<ReduceStereoType> stereotype = new ArrayList<ReduceStereoType>();
	
	public ReduceMethodExp(String methodname, String returntype, ArrayList<PropertyCallExp> parameter) {
		super();	//給ocl用
		this.methodName = methodname;
		this.returntype = returntype;
		for(int cnt = 0; cnt < parameter.size(); cnt++) {
			addParameter(parameter.get(cnt).getType(), parameter.get(cnt).getVariable());
		}
	}
	
	public ReduceMethodExp(ArrayList<String> methodontent) {
		super();	//給UML用
		this.methodName = methodontent.get(0).split("\"")[1]; //<method name="XXX">
		int stereoCnt = 0;
		int cnt = 1;
		while(methodontent.get(cnt).contains("parameter")) {
			this.parameters.add(new ReducePropertyCallExp(methodontent.get(cnt).split("\"")[3], methodontent.get(cnt++).split("\"")[1]));
		}
		if(methodontent.get(cnt).contains("returntype")) {
			this.returntype = methodontent.get(cnt++).split("\"")[1];
		} 
		ArrayList<String> stereotypeContent = new ArrayList<String>();
		for(;cnt < methodontent.size();cnt++) {
			if(methodontent.get(cnt).contains("<stereotype"))
				stereotype.add(new ReduceStereoType(methodontent.get(cnt).split("\"")[1], methodontent.get(cnt).split("\"")[3]));
				while(!methodontent.get(++cnt).contains("</stereotype")) {
					stereotypeContent.add(methodontent.get(cnt));
				}
				stereotype.get(stereoCnt++).setExpression(stereotypeContent);
				stereotypeContent.clear();
		}
		
	}

	public String getMethodName() {
		return methodName;
	}

	public void addReduceStereoType(String stereotype, String exception) {
		this.stereotype.add(new ReduceStereoType(stereotype, exception));
	}
	
	public ArrayList<ReduceStereoType> getReduceStereoType() {
		return this.stereotype;
	}
	
	public String getReturnTypeName() {
		return returntype;
	}
	
	public void addParameter(String type, String varaible) {
		this.parameters.add(new ReducePropertyCallExp(type, varaible));
	}
	
	public ArrayList<ReducePropertyCallExp> getParameternode() {
		return this.parameters;
	}
	
	public String genast2xml(String xmlcontent, int structurelevel) {
		for(int cnt = 0; cnt < structurelevel; cnt++) {xmlcontent+="  ";}
		xmlcontent += "<method name=\""+this.methodName+"\">\n";
		structurelevel++;
		if(!this.parameters.isEmpty()) {
			for(int cnt = 0; cnt < parameters.size(); cnt++) 
				xmlcontent = parameters.get(cnt).genast2xml(xmlcontent, structurelevel);	
		}
		if(!this.returntype.equals("")) {
			for(int cnt = 0; cnt < structurelevel; cnt++) {xmlcontent+="  ";}
			xmlcontent += "<returntype type=\""+ this.returntype+"\"/>\n";
		}
		for(int cnt = 0; cnt < stereotype.size(); cnt++) 
			xmlcontent = stereotype.get(cnt).genast2xml(xmlcontent, structurelevel);
		structurelevel--;
		for(int cnt = 0; cnt < structurelevel; cnt++) {xmlcontent+="  ";}
		xmlcontent += "</method>\n";
		return xmlcontent;
	}
	
	public String getExpreesion() {
		return "methodExp";
	}
}
