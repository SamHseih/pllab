package ccu.pllab.tcgen.AbstractType;

public class CharType extends VariableType {

	public CharType() {
		super.typeName="char";
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "char";
	}

	@Override
	public String genDomainCLP(String obj) {
		// TODO Auto-generated method stub
		String obj_name =obj.substring(0, 1).toUpperCase()+obj.substring(1);
		return "["+obj_name+"] :: ['a', 'b','c']";
	}
}
