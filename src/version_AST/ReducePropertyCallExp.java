package version_AST;

public class ReducePropertyCallExp extends ReduceASTnode{
	private String type;
	private String variable;
	
	public ReducePropertyCallExp(String type, String variable) {
		super();
		this.type = type;
		this.variable = variable;
	}
	public ReducePropertyCallExp(String parameterContent) {
		super();
		this.type = parameterContent.split("\"")[3];
		this.variable = parameterContent.split("\"")[1];
	}
	public String getType() {
		return this.type;
	}
	
	public String getVariable() {
		return this.variable;
	}

	@Override
	public String genast2xml(String xmlcontent, int structurelevel) {
		for(int cnt = 0; cnt < structurelevel; cnt++) {xmlcontent+="  ";}
		xmlcontent += "<parameter name=\""+this.variable+"\" type=\""+this.type+"\"/>\n";
		
		return xmlcontent;
	}
	
	public String getExpreesion() {
		return "propertyExp";
	}
}
