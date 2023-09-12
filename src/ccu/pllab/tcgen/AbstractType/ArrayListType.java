package ccu.pllab.tcgen.AbstractType;

public class ArrayListType extends VariableType {

	VariableType element;

	
	public ArrayListType(VariableType obj) {
		element = obj;

	}
	

	public ArrayListType() {
		// TODO Auto-generated constructor stub
	}


	public VariableType getElement() {
		// TODO Auto-generated method stub
		return element;
	}
	
	public String getSize() {
		return "x";
	}


	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "ArrayList<"+element.toString()+">";
	}


	@Override
	public String genDomainCLP(String obj) {
		// TODO Auto-generated method stub
		return null;
	}


}
