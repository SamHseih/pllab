package ccu.pllab.tcgen.launcher;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.internal.core.JavaProject;
import org.xml.sax.SAXException;

import com.parctechnologies.eclipse.EclipseException;

import ccu.pllab.tcgen.AbstractCLG.CLGGraph;
import ccu.pllab.tcgen.AbstractCLG.CLGStartNode;
import ccu.pllab.tcgen.AbstractSyntaxTree.AbstractSyntaxTreeNode;
import ccu.pllab.tcgen.AbstractSyntaxTree.SymbolTable;
import ccu.pllab.tcgen.AbstractType.TypeTable;
import ccu.pllab.tcgen.AbstractType.UserDefinedType;
import ccu.pllab.tcgen.DataWriter.DataWriter;
import ccu.pllab.tcgen.TestCase.TestData;
import ccu.pllab.tcgen.clg2path.CriterionFactory.Criterion;
import ccu.pllab.tcgen.clgGraph2Path.CoverageCriterionManager;
import ccu.pllab.tcgen.exe.main.Main;
import ccu.pllab.tcgen.transform.AST2CLG;
import ccu.pllab.tcgen.transform.CLG2Path;
import ccu.pllab.tcgen.transform.OCL2AST;
import ccu.pllab.tcgen.transform.Splitter;
import tudresden.ocl20.pivot.model.ModelAccessException;
import tudresden.ocl20.pivot.parser.ParseException;
import tudresden.ocl20.pivot.tools.template.exception.TemplateException;
import version_control.FileHandleToServer;
import tcgenplugin_2.handlers.BlackBoxHandler;;

public class BlackBoxLauncher {
	private File ocl;
	private File classUml;
	private OCL2AST oclParser;
	private AST2CLG clgParser;
	private CLG2Path pathParser;
	private AbstractSyntaxTreeNode oclAst;
	private SymbolTable symbolTable;
	private ArrayList<CLGGraph> clgGraph;
	private CLGGraph invCLG;
	private IProgressMonitor pmonitor;
	public static TypeTable typeTable;
	public static boolean generate_testinfo = true; //for版本控制 只要產生test data 其他clg、path等都不用產生
	public static String version_saveFilePath = "";
	public BlackBoxLauncher(File ocl, File classUml, IProgressMonitor monitor) {
		this.ocl = ocl;
		this.classUml = classUml;
		this.pmonitor = monitor;
		this.typeTable = new TypeTable();
	}
	
	public BlackBoxLauncher(File ocl, File classUml) {
		this.ocl = ocl;
		this.classUml = classUml;
		this.typeTable = new TypeTable();
	}
	
	public AbstractSyntaxTreeNode getAST() {
		return this.oclAst;
	}
	
	public SymbolTable getSymbolTable() {
		return this.symbolTable;
	}
	
	public ArrayList<CLGGraph> getCLGGraph(){
		return this.clgGraph;
	}
	
	public CLGGraph getInvCLG() {
		return this.invCLG;
	}
	
	public void genBlackBoxTestScripts() {
		try {
//			OCL -> AST
			this.oclParser = new OCL2AST();
			oclParser.makeAST(ocl);
			oclParser.makeSymbolTable(classUml);
		    oclParser.typeToAst();
		    this.oclAst=oclParser.getAbstractSyntaxTree();
			oclAst.toGraphViz();
			pmonitor.worked(10);
//			AST -> CLG
			this.clgParser = new AST2CLG();
			clgParser.genCLG(oclAst);
			this.clgGraph = clgParser.getCLGGraph();
			this.invCLG = clgParser.getInvCLG();
			pmonitor.worked(10);

			
//			CLG -> JUnit
//			this.pathParser = new CLG2Path();
//			this.symbolTable = oclParser.getSymbolTable();
//			pathParser.setAttribute(invCLG, symbolTable);
//			pathParser.genTestScripts(clgGraph, classUml);
			pmonitor.worked(10);
			this.pathParser = new CLG2Path();
			this.symbolTable = oclParser.getSymbolTable();
			pathParser.setAttribute(invCLG, symbolTable);
			pathParser.init(clgGraph);
			pmonitor.worked(10);
			for(int number=0;number<clgGraph.size();number++) {
				pathParser.genTestData(clgGraph,number);
				pathParser.genTestScript(classUml);
				pmonitor.worked(60/clgGraph.size());
				if (pmonitor.isCanceled()) {
			        throw new OperationCanceledException();
			    }
			}
			
			String testScript = pathParser.getTestScript();
			String className = pathParser.getClassName();
			testScript += "}";
			String testOutputPath = DataWriter.output_folder_path+ "/test script/team/ccu/pllab/";
			switch (BlackBoxHandler.coverageCriteria) {
				case "DC":					 	
					DataWriter.writeInfo(testScript, className + "Test", "java", testOutputPath+"dc/");
					break;
				case "DCC":					 	
					DataWriter.writeInfo(testScript, className + "Test", "java", testOutputPath+"dcc/");
					break;
				case "MCC":					 	
					DataWriter.writeInfo(testScript, className + "Test", "java", testOutputPath+"mcc/");
					break;
				default :
					DataWriter.writeInfo(testScript, className + "Test", "java", testOutputPath+"dcc/");
									
			}
//			IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
//			IProject project = root.getProject(BlackBoxHandler.CurrentProjName);
//			IFile file = project.getFile(BlackBoxHandler.CurrentEditorProjectPath+"/src/com.test");
//			IFolder f = (IFolder) file;
//			f.create(false, false, null);
//			prepare((IFolder) file.getParent());
			//IJavaProject targetProject = JavaCore.create(project);
			
			//System.out.println("Execution Succeed"+project.hasNature(JavaCore.NATURE_ID));
			
			oclParser = null;
			clgParser = null;
			pathParser = null;
			typeTable = null;
			
	//s		JOptionPane.showMessageDialog(null, "Execution Succeed", "Result", JOptionPane.INFORMATION_MESSAGE );
		} catch (Exception e1) {
			JOptionPane.showMessageDialog(null, "Execution Failed", "Result", JOptionPane.INFORMATION_MESSAGE );
			System.out.println("Execution Failed");
			e1.printStackTrace();
		}	
	}
	
	public void genBlackBoxTestScripts_VersionControl(String ProjectName, String versionsaveFilePath,String version, String compareVer, String coverageCriteria) {
		try {
			if(coverageCriteria.equals("DC")) {
				Main.criterion=Criterion.dc;
				BlackBoxHandler.coverageCriteria = "DC";
			}
			else if(coverageCriteria.equals("DCC")) {
				Main.criterion=Criterion.dcc;
				BlackBoxHandler.coverageCriteria = "DCC";
			}
			else { //以後新增用
				Main.criterion=Criterion.dc;
				BlackBoxHandler.coverageCriteria = "DC";
			}
			BlackBoxHandler.CurrentEditorName = ProjectName;
			Main.TestType = "BlackBox";
			generate_testinfo = false;
			this.version_saveFilePath = versionsaveFilePath;
			DataWriter.output_folder_path = "D:///version_control_data//"+ProjectName+"//"; //隨便設的 不會用到 但需要設定
			DataWriter.Clg_output_path = DataWriter.output_folder_path+"test model//";//
			DataWriter.testPath_output_path = DataWriter.output_folder_path+"test paths//";//
			DataWriter.testCons_output_path = DataWriter.output_folder_path+"test//";//
			DataWriter.testData_output_path = DataWriter.output_folder_path+"test data"; //
			DataWriter.initOutputPath();
			Main.boundary_analysis=true;
			Main.msort=false;
//			OCL -> AST
			this.oclParser = new OCL2AST();
			oclParser.makeAST(ocl);
			oclParser.makeSymbolTable(classUml);
		    oclParser.typeToAst();
		    this.oclAst=oclParser.getAbstractSyntaxTree();
			oclAst.toGraphViz();
			
//			AST -> CLG
			this.clgParser = new AST2CLG();
			clgParser.genCLG(oclAst);
			this.clgGraph = clgParser.getCLGGraph();
			this.invCLG = clgParser.getInvCLG();
			System.out.println(compareVer);
			this.pathParser = new CLG2Path();
			this.symbolTable = oclParser.getSymbolTable();
			pathParser.setAttribute(invCLG, symbolTable);
			pathParser.init(clgGraph);
			
			
			long startTime=System.currentTimeMillis();
			for(int number=0;number<clgGraph.size();number++) {
				pathParser.genTestData_VersionControl(clgGraph,number,version,compareVer);
				pathParser.genTestScript(classUml);		
			} 
			long endTime=System.currentTimeMillis();
			System.out.println("程式執行時間new： "+ (endTime-startTime) +"ms");
			
			
			String testScript = pathParser.getTestScript()+"}";
			InputStream is = new ByteArrayInputStream(testScript.getBytes());
			FileHandleToServer.storeFile(pathParser.getClassName()+"\\"+ version +"\\",
					                     pathParser.getClassName()+"Test.txt"
											 , is);
			oclParser = null;
			clgParser = null;
			pathParser = null;
			typeTable = null;
	//		
	//		JOptionPane.showMessageDialog(null, "Execution Succeed", "Result", JOptionPane.INFORMATION_MESSAGE );
		} catch (Exception e1) {
	//		JOptionPane.showMessageDialog(null, "Execution Failed", "Result", JOptionPane.INFORMATION_MESSAGE );
			System.out.println("Execution Failed");
			e1.printStackTrace();
		}	
	}
	
}
