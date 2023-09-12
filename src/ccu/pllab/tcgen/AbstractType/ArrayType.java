package ccu.pllab.tcgen.AbstractType;

public class ArrayType extends VariableType {

	VariableType element;
	String size;
	// String low;
	// String up;
	// int dimension;
	
	public ArrayType(VariableType obj, String size) {
		element = obj;
		this.size=size;
		// dimension = dim;
	}
	
	/*
	public ArrayType(VariableType obj, String low, String up) {
		element = obj;
		this.low=low;
		this.up=up;
		// dimension = dim;
	}*/
	

	public VariableType getElement() {
		// TODO Auto-generated method stub
		return element;
	}


	public String getSize() {
		// TODO Auto-generated method stub
		return this.size;
	}


	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "Array["+size+"]";
	}

	@Override
	public String genDomainCLP(String obj) {
		// TODO Auto-generated method stub
		return null;
	}

}
