package version_AST;

public class ReduceLiteralExp extends ReduceASTnode{
	private String value;
	private String type;
	
	public ReduceLiteralExp(String value, String type) {
		super();
		this.value = value;
		this.type = type;
	}
	public ReduceLiteralExp(String literalContent) {
		super();
		this.value = literalContent.split("\"")[1];
		this.type = literalContent.split("\"")[3];
	}
	public String getValue() {
		return this.value;
	}
	
	public String getType() {
		return this.type;
	}
	
	public String genast2xml(String xmlcontent, int structurelevel) {
		for(int cnt = 0; cnt < structurelevel; cnt++) {xmlcontent+="  ";}
		xmlcontent += "<literal name=\""+this.value+"\" type=\""+this.type+"\"/>\n";
		
		return xmlcontent;
	}
	public String getExpreesion() {
		return "literalExp";
	}
}
