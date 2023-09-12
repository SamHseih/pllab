package version_AST;

public class ReduceClassifierExp extends ReduceASTnode{
	private String classname;
	private ReduceStereoType inv;
	
	public ReduceClassifierExp(String classname) {
		super();
		this.classname = classname;
	}
	
	public void addReduceStereoType(String stereoType, String exception) {
		inv = new ReduceStereoType(stereoType, exception);
	}
	
	public ReduceStereoType getReduceStereoType() {
		return this.inv;
	}
	
	public String getClassname() {
		return classname;
	}

	public String genast2xml(String xmlcontent, int structurelevel) {
		for(int cnt = 0; cnt < structurelevel; cnt++) {xmlcontent+="  ";}
		xmlcontent += "<classifier name=\""+this.classname+"\">\n";
		structurelevel++;
		xmlcontent = this.inv.genast2xml(xmlcontent, structurelevel);
		structurelevel--;
		for(int cnt = 0; cnt < structurelevel; cnt++) {xmlcontent+="  ";}
		xmlcontent += "</classifier>\n";
		
		return xmlcontent;
	}
	public String getExpreesion() {
		return "classifierExp";
	}
}
