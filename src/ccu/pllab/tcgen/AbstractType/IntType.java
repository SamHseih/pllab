package ccu.pllab.tcgen.AbstractType;

public class IntType extends VariableType {

	public IntType() {
		super.typeName="int";
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "int";
	}

	@Override
	public String genDomainCLP(String obj) {
		// TODO Auto-generated method stub
		String obj_name =obj.substring(0, 1).toUpperCase()+obj.substring(1);
		return "["+obj_name+"] :: -32768..32767";
	}
	
	

}
