package ccu.pllab.tcgen.PapyrusCDParser;

import ccu.pllab.tcgen.AbstractType.*;

public class VariableInfo {
	// private String type ;
	private VariableType varType;
	private String name ;
	private String id ;
	private String visibility ;
	private String lowerValue ;
	private String upperValue ;
	private String className ;  // 所屬類別
	// private String size ;  // 陣列用
	 
	// private String value ;
	
	public VariableInfo() {
		// type = "" ;
		varType = null;
		name = "" ;
		id = "";
		visibility = "";
		lowerValue = "1";
		upperValue = "1" ;
		className = "" ;
	}
	
	public VariableInfo(/*String t,*/VariableType var, String n, String i, String v, String c) {
		// type = t;
		varType = var;
		name = n ;
		id = i ;
		visibility = v ;
		lowerValue = "1";
		upperValue = "1" ;
		className = c ;
	}
	
	public void setType(VariableType t ) {
		this.varType = t;
	}
	
	public void setName( String name ) {
		this.name = name ;
	}
	
	public void setID( String id ) {
		this.id = id ;
	}
	
	public void setVisibility( String visibility ) {
		this.visibility = visibility ;
	}
	
	
	public void setLowerValue( String s ) {
		this.lowerValue = s ;
	}
	
	
	public void setUpperValue( String s ) {
		this.upperValue = s ;
	}
	
	public void setClassName( String c_name ) {
		this.className = c_name ;
	}
	
	/*
	public void setSize( String size ) {
		this.size = size ;
	}
	*/
	
	

	
	public VariableType getType() {
		return varType ;
	}
	
	public String getName() {
		return name ;
	}
	
	public String getID() {
		return id ;
	}
	
	public String getVisibility() {
		return visibility ;
	}
	
	
	public String getLowerValue() {
		return this.lowerValue ;
	}
	
	
	public String getUpperValue() {
		return this.upperValue ;
	}
	
	public String getClassName() {
		return this.className ;
	}
	
	/*
	public String getSize() {
		return size ;
	}
	*/

}
