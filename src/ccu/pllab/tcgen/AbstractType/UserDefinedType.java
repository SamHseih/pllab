package ccu.pllab.tcgen.AbstractType;

import java.io.File;
import java.util.ArrayList;

import ccu.pllab.tcgen.AbstractSyntaxTree.MethodToken;
import ccu.pllab.tcgen.AbstractSyntaxTree.VariableToken;
import ccu.pllab.tcgen.PapyrusCDParser.ClassInfo;
import ccu.pllab.tcgen.PapyrusCDParser.OperationInfo;
import ccu.pllab.tcgen.PapyrusCDParser.VariableInfo;

public class UserDefinedType extends VariableType {
	ClassInfo content;
	// ArrayList<VariableInfo> attributes;
	// ArrayList<OperationInfo> operations;
	// ArrayList<VariableToken> attributes;
	// ArrayList<MethodToken> operations;
	
	public UserDefinedType(String name,String id) {
		super.typeName = name;
		super.typeID = id;
	}
	
	public UserDefinedType(String type,ClassInfo info) {
		super.typeName = type;
		content = info;
	}
	
	public UserDefinedType(String type)
	{
		typeName = type;
	}
	public UserDefinedType() {
		// TODO Auto-generated constructor stub
	}

	public String getTypeName() {
		return typeName;
	}
	
	public String getTypeID() {
		return content.getID();
	}

	
	public void setClassInfo(ClassInfo info) {
		content = info;
	}
	
	public ClassInfo getClassInfo() {
		return content ;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		// return typeName ;
		String s ;
		s = "Name: " +typeName+",,, ID: " + typeID ;
		if(content != null) {
		for(int j= 0; content.getProperties()!= null && j < content.getProperties().size();j++ ) {
    		VariableInfo p = content.getProperties().get(j);
    		s=s+p.getName()+":"+p.getType().toString()+" ";
    	}
    	
    	s=s+"\nOperations: ";
    	for(int k= 0; content.getOperations()!= null && k < content.getOperations().size();k++ ) {
    		OperationInfo o = content.getOperations().get(k);
    		s=s+" "+o.getReturnType().getType().typeName +" " + o.getName();
    		s=s+"Parameter: ";
    	    for(int index = 0 ;o.getParameter()!= null && index < o.getParameter().size();index++) {
	    		VariableInfo p = o.getParameter().get(index);
	    		s=s+"::"+p.getType().toString() + " " + p.getName();
    	    }
    	    s=s+"\n";
    	}
		}//if
    	
    	return s;
	}

	/*@Override
	public String genDomainCLP(String obj) {
		// TODO Auto-generated method stub
		String s="" ;
		String name=obj+"=[";
		for(int i = 0; i <content.getProperties().size();i++) {
			
			String temp=content.getProperties().get(i).getName();
			name=name+temp.substring(0, 1).toUpperCase()+temp.substring(1)+",";
		}
		name=name.substring(0, name.length()-1)+"],";
		for(int i = 0; i <content.getProperties().size();i++) {
			
			String temp=content.getProperties().get(i).getName();
			s=s+content.getProperties().get(i).getType().genDomainCLP(temp)+",";
		}
		return  name+s.substring(0,s.length()-1);
	}*/

}
