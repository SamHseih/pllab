package ccu.pllab.tcgen.pathCLPFinder;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.parctechnologies.eclipse.EXDRInputStream;
import com.parctechnologies.eclipse.EclipseEngine;
import com.parctechnologies.eclipse.EclipseException;
import com.parctechnologies.eclipse.EmbeddedEclipse;
import com.parctechnologies.eclipse.FromEclipseQueue;

import ccu.pllab.tcgen.DataWriter.DataWriter;
import ccu.pllab.tcgen.TestCase.TestData;
import ccu.pllab.tcgen.TestCase.TestDataClassLevel;
import ccu.pllab.tcgen.exe.main.Main;
import ccu.pllab.tcgen.pathCLP2data.CLP2Data;
import ccu.pllab.tcgen.pathCLP2data.CLP2DataFactory;
import ccu.pllab.tcgen.pathCLP2data.ECLiPSe_CompoundTerm;
import scala.collection.generic.BitOperations.Int;
import tcgenplugin_2.handlers.SampleHandler;

public class CLPSolver {
	private CLP2Data clp2data;
	private String EclDirectPath = DataWriter.output_folder_path+"/ecl/";

	private List<ECLiPSe_CompoundTerm> sol;
	private TestData testData;
	private TestDataClassLevel testDataclass;
	public CLPSolver() {

	}
	
//	for路徑縮減
	public boolean solvingInfeasiblePath(String className, String methodName, int pathNum,int testcaseID, boolean isConstructor, String retType, String objPre, String argPre, String objPost, String argPost,
			String retVal) {
		
		this.EclDirectPath = "Examples/eclSIP/";
		File eclFile = new File(EclDirectPath + className + methodName + "SIP" + pathNum + ".ecl");
		int testCaseID = 1;
		try {
//			
			this.connectCLPSolver();
			this.clp2data.compile(eclFile);
			this.sol = this.clp2data.solvingCSP_term("test" + className + methodName, objPre, argPre, objPost, argPost, retVal, 5);
			this.testData = new TestData(className, methodName, pathNum, testCaseID, isConstructor, retType, this.sol);
			System.out.println("TD: " + testData.toString());
			return true;
		} catch (Exception e) {
			eclFile.renameTo(new File(eclFile.getParentFile(), eclFile.getName()));
			//e.printStackTrace();
			return false;
		}
	}
	
	public boolean solving(String className, String methodName, int pathNum,int testcaseID, boolean isConstructor, String retType, String objPre, String argPre, String objPost, String argPost,
			String retVal) {
		//this.EclDirectPath = DataWriter.output_folder_path + "C:\\Users\\chienLung\\tcgen\\examples\\output";
		//this.EclDirectPath = "C:\\Users\\chienLung\\tcgen\\examples\\output\\CLG\\ECL\\";
//		this.EclDirectPath = Main.output_folder_path+"\\ECL\\";
//		因應外掛修改
		
		String TestTypeSign = "";
		
//		判別測試種類為黑箱或白箱等
		if(Main.TestType == "BlackBox") {
			TestTypeSign = "B";
		}else if(Main.TestType == "WhiteBox") {
			TestTypeSign = "W";
		}else {
			TestTypeSign = "C";
		}
		
		this.EclDirectPath = DataWriter.testCons_output_path;
		String Time_EclDirectPath = DataWriter.testCons_output_path.replace(className+"_BlackBox", "Time_BlackBox");
		String Date_EclDirectPath = DataWriter.testCons_output_path.replace(className+"_BlackBox", "Date_BlackBox");
		System.out.println(this.EclDirectPath);
		System.out.println(Time_EclDirectPath );
		System.out.println(Date_EclDirectPath );
		File eclFile = new File(EclDirectPath + className + methodName + TestTypeSign + pathNum + ".ecl");
		int testCaseID = 1;
		try {
//			
			this.connectCLPSolver();
			
			File tempF1= new File(Date_EclDirectPath+"DateDate.ecl");
			File tempF2= new File(Date_EclDirectPath+"DateGetYear.ecl");
			File tempF3= new File(Date_EclDirectPath+"DateGetMonth.ecl");
			File tempF4= new File(Date_EclDirectPath+"DateGetDay.ecl");
			File tempF5= new File(Date_EclDirectPath+"DateNext.ecl");
			
			if( tempF1.exists() ) this.clp2data.compile(tempF1);
			if( tempF2.exists() ) this.clp2data.compile(tempF2);
			if( tempF3.exists() ) this.clp2data.compile(tempF3);
			if( tempF4.exists() ) this.clp2data.compile(tempF4);
			if( tempF5.exists() ) this.clp2data.compile(tempF5);

			
			File tempF6= new File(Time_EclDirectPath+"TimeTime.ecl");
			File tempF7= new File(Time_EclDirectPath+"TimeGetHour.ecl");
			File tempF8= new File(Time_EclDirectPath+"TimeGetMinute.ecl");
			File tempF9= new File(Time_EclDirectPath+"TimeGetSecond.ecl");
			File tempF10= new File(Time_EclDirectPath+"TimeNext.ecl");
			
			if( tempF6.exists() ) this.clp2data.compile(tempF6);
			if( tempF7.exists() ) this.clp2data.compile(tempF7);
			if( tempF8.exists() ) this.clp2data.compile(tempF8);
			if( tempF9.exists() ) this.clp2data.compile(tempF9);
			if( tempF10.exists() ) this.clp2data.compile(tempF10);
			//File tempF11= new File("C:\\runtime-EclipseApplication\\Clock\\test constraints\\Clock_BlackBox_DCC\\ClockClock.ecl");
			//this.clp2data.compile(tempF11);
			
			this.clp2data.compile(eclFile);
			
			this.sol = this.clp2data.solvingCSP_term("test" + className + methodName, objPre, argPre, objPost, argPost, retVal, 5);
			this.testData = new TestData(className, methodName, pathNum, testCaseID, isConstructor, retType, this.sol);
			System.out.println("TD: " + testData.toString());
			return true;
		} catch (Exception e) {
			eclFile.renameTo(new File(eclFile.getParentFile(), eclFile.getName()));
			//e.printStackTrace();
			return false;
		}
	}
  
	public boolean compiling(String folderName, String fileName) {
		//compile method, needn't get testData
		this.EclDirectPath = DataWriter.output_folder_path + "\\" + folderName + "\\";
		File eclFile = new File(EclDirectPath + fileName + ".ecl");
		try {
			this.connectCLPSolver();
			this.clp2data.compile(eclFile);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	public boolean solving(String className, String methodName, int pathNum, int testcaseID, boolean isConstructor, String retType, String objPre, String argPre, String objPost, String argPost,
			String retVal,String output_path) {
		
		String TestTypeSign = "";
		
//		判別測試種類為黑箱或白箱等
		if(Main.TestType == "BlackBox") {
			TestTypeSign = "B";
		}else if(Main.TestType == "WhiteBox") {
			TestTypeSign = "W";
		}else {
			TestTypeSign = "C";
		}
		
//		this.EclDirectPath = output_path+"\\ECL\\";
//		因應外掛修改
		this.EclDirectPath = DataWriter.testCons_output_path;
		File eclFile = new File(EclDirectPath + className + methodName + TestTypeSign + pathNum + ".ecl");
		int testCaseID = 1;
		try {
//			
			this.connectCLPSolver();

			
			this.sol = this.clp2data.solvingCSP_term("test" + className + methodName, objPre, argPre, objPost, argPost, retVal, 4);
			this.testData = new TestData(className, methodName, pathNum, testCaseID, isConstructor, retType, this.sol);
			System.out.println("TD: " + testData.toString());
			return true;
		} catch (Exception e) {
			eclFile.renameTo(new File(eclFile.getParentFile(), eclFile.getName()));
			//e.printStackTrace();
			return false;
		}
	}
	//824
	public boolean solving(String classN, int pathNum, ArrayList<String> methodN){
		File eclFile = null;		
		int testCaseID = 1;
		try {	
//			String pathecl ="src/ccu/pllab/tcgen/TCGenExample824/ECL/";
			String pathecl =ccu.pllab.tcgen.DataWriter.DataWriter.testCons_output_path;
			//this.EclDirectPath = ccu.pllab.tcgen.DataWriter.DataWriter.output_folder_path+"ECL/";
			eclFile = new File(pathecl + classN+"_"+pathNum + ".ecl");
			this.connectCLPSolver();	
			
			this.clp2data.compile(new File("E:\\pllab20150831\\eclipse\\workspace\\tcgen\\examples\\testmethodpath\\CLPCoffeeMachine.ecl"));
			this.clp2data.compile(new File("E:\\pllab20150831\\eclipse\\workspace\\tcgen\\examples\\testmethodpath\\CLPInsert.ecl"));
			this.clp2data.compile(new File("E:\\pllab20150831\\eclipse\\workspace\\tcgen\\examples\\testmethodpath\\CLPWithdraw.ecl"));
			this.clp2data.compile(new File("E:\\pllab20150831\\eclipse\\workspace\\tcgen\\examples\\testmethodpath\\CLPCook.ecl"));
			this.clp2data.compile(new File("E:\\pllab20150831\\eclipse\\workspace\\tcgen\\examples\\testmethodpath\\CLPDone.ecl"));
			
			this.clp2data.compile(eclFile);
			
			String comstr = "testpath"+pathNum+"(Obj_pre, Arg_pre, Obj_post, Arg_post, Ret_val).";
			this.sol= this.clp2data.solvingCSP_new(comstr, 5);
			
			if(!this.sol.contains(null)){
				this.testDataclass = new TestDataClassLevel(classN,methodN,pathNum,testCaseID,this.sol);
			}
			else {
				this.testDataclass = null; //pathNum-=1;
			}
//			this.testDataclass = new TestDataClassLevel(classN,methodN,pathNum,testCaseID,this.sol);
			return true;
			
		} catch (Exception e) {
			this.testDataclass = null;
			eclFile.renameTo(new File(this.EclDirectPath + "Fail_"+classN+methodN+"_"+pathNum + ".ecl"));
		//	e.printStackTrace();
			return false;
		}
	}
	
	public TestData getTestData(){
		return this.testData;
	}
	
	public String getCLPTestDataString(){
		return "OBJ_PRE = "+this.testData.getObjPre()+
				", ARG_PRE = "+this.testData.getArgPre()+
				",OBJ_POST = "+this.testData.getObjPost()+
				",ARG_POST = "+this.testData.getArgPost()+
				", RETVAL = "+this.testData.getRetVal()+
				", EXCEPTION ="+this.testData.getException();
	}
	
	public TestDataClassLevel getTestDataclass(){
		return this.testDataclass;
	}

	public void connectCLPSolver() throws EclipseException, IOException {
		clp2data = CLP2DataFactory.getEcl2DataInstance();
	}

	public void disconnectCLPSolver() {
		clp2data.Destroy();
	}

}
