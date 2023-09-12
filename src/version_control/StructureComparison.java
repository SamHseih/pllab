package version_control;

import java.io.IOException;
import java.net.SocketException;

import version_AST.ReduceClassExp;

public class StructureComparison {
	public static void main(String[] args) throws SocketException, IOException {
		ReduceClassExp a = new ReduceClassExp(FileHandleToServer.readFile("Grade//version 1//", "GradeAST2UML.uml"));
		ReduceClassExp b = new ReduceClassExp(FileHandleToServer.readFile("Grade//version 2//", "GradeAST2UML.uml"));
		
		
		
		System.out.println("finish!");
	}
	
	public void Structure_comparison_algo(String project, String versionOld, String versionNew) {
		if(versionNew.equals("1")) {  //第一版本 沒得比較
			
		}
		else { //第N版本 可以比較
			
		}
		
		
		
		
		System.out.println("Structure_comparison success!");
	}
}
