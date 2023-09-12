package ccu.pllab.tcgen.AbstractSyntaxTree;

import ccu.pllab.tcgen.AbstractType.*;

public class VariableToken {
	String variableName;
	// String type;
	VariableType type;
	String lowerValue;
	String highValue;

	
	public VariableToken(String name/*,String type*/, VariableType vt) {
		// TODO Auto-generated constructor stub
		this.variableName=name;
		// this.type=type;
		this.lowerValue="1";
		this.highValue="1";
		this.type=vt;
	}
	
	public String getVariableName()
	{
		return this.variableName;
	}
	/*
	public String getTypeOld()
	{
		return this.type;
	}*/
	
	public VariableType getType() {
		return this.type;
	}
}
