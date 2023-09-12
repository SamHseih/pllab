package ccu.pllab.tcgen.AbstractType;

import java.util.ArrayList;


public class TypeTable {
	private ArrayList<VariableType> typelist;
	
	public TypeTable(){
		typelist = new ArrayList<VariableType>();
		init();
	}
	
	public void init() {
		IntType intType = new IntType();
		StringType stringType = new StringType();
		BooleanType booleanType = new BooleanType();
		VoidType voidType = new VoidType();
		CharType charType = new CharType();
		
		typelist.add(intType);      // 0
		typelist.add(stringType);   // 1
		typelist.add(booleanType);  // 2
		typelist.add(voidType);     // 3
		typelist.add(charType);     // 4
	}
	
	public void add(VariableType type) {
		typelist.add(type);
	}
	
	public boolean containsType(String typeName, String typeID) {
		if( this.get(typeName, typeID) != null ) {
			return true;
		}
		
		else return false;	
	}
	
	public ArrayList<VariableType> getTypeList(){
		return this.typelist;
	}
	
	public VariableType get( String typeName, String typeID ) {
		if(typeName.equals("int") || typeName.equals("Integer")) return typelist.get(0);
		else if (typeName.equals("string") || typeName.equals("String")) return typelist.get(1);
		else if (typeName.equals("boolean") || typeName.equals("Boolean")) return typelist.get(2);
		else if (typeName.equals("void") || typeName.equals("OclVoid")) return typelist.get(3);
		else if (typeName.equals("char") || typeName.equals("Char")) return typelist.get(4);
		else {
			for(int i = 0; i < typelist.size();i++) {
				if ( typeName.equals(typelist.get(i).typeName) || typeID.equals(typelist.get(i).typeID))
					return typelist.get(i);
			}
			return null;
		} // else
	}
	
	public String printTypeTableInfo() {
		String s = "vvvvv測試TypeTable內容資訊vvvvv\n";
		
		for(int i = 0; i< this.typelist.size();i++) {
			s =s+"NO."+i+" >>> "+typelist.get(i).toString()+"\n";
		}
		
		s = s+"^^^^^測試TypeTable內容資訊^^^^^";
		return s ;
	}

}
