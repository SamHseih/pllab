package ccu.pllab.tcgen.AbstractType;

public class BooleanType extends VariableType {

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "boolean";
	}

	@Override
	public String genDomainCLP(String obj) {
		// TODO Auto-generated method stub
		return ":: [true, false]";
	}

}