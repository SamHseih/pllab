package ccu.pllab.tcgen.srcASTVisitor;

 
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;

import org.eclipse.jdt.core.dom.CompilationUnit;
import org.xml.sax.SAXException;

import com.parctechnologies.eclipse.EclipseException;

import ccu.pllab.tcgen.AbstractCLG.*;
import ccu.pllab.tcgen.DataWriter.DataWriter;
import ccu.pllab.tcgen.TestCase.TestCaseFactory;
import ccu.pllab.tcgen.TestCase.TestScriptGenerator;
import ccu.pllab.tcgen.clg2path.CriterionFactory.Criterion;
import ccu.pllab.tcgen.clgGraph2Path.CoverageCriterionManager;
import ccu.pllab.tcgen.exe.main.Main;
import ccu.pllab.tcgen.pathCLPFinder.CLPSolver;
import ccu.pllab.tcgen.transform.CLG2Path;
import ccu.pllab.tcgen.transform.OCL2AST;
import scala.tools.nsc.symtab.SymbolTable;
import tudresden.ocl20.pivot.model.ModelAccessException;
import tudresden.ocl20.pivot.parser.ParseException;
import tudresden.ocl20.pivot.tools.template.exception.TemplateException;

public class SrcVisitProcess {

	public SrcVisitProcess(String path, File ocl, File classUml) throws Exception {
		//TestCaseFactory tcFactory = new TestCaseFactory();
		/******************************************************/

		CompilationUnit comp = SrcJdtAstUtil.getCompilationUnit(path);

		SrcVisitorUnit visitor = new SrcVisitorUnit();
		comp.accept(visitor);
		
		//用OCL2AST產生symboltable(與黑箱部份相同)
		OCL2AST useOCLMakeSymboltable = new OCL2AST(ocl, classUml);
		ccu.pllab.tcgen.AbstractSyntaxTree.SymbolTable symbolTable = useOCLMakeSymboltable.getSymbolTable();
		
		//compile method clp
		/*start  ocl2clp 可執行規格*/
		CLPSolver methodCompiler = new CLPSolver();
		for(CLGGraph graph : visitor.getCLGGraph()) {
			String methodCLP = ((CLGStartNode) graph.getStartNode()).OCL2CLP();
			System.out.println(methodCLP);
			DataWriter.writeInfo(methodCLP,  "testMehodCLP_"+ ((CLGStartNode) graph.getStartNode()).getMethodName().toString() , "ecl", DataWriter.output_folder_path+"\\test methodCLP",Main.className);
			
			if (!methodCompiler.compiling("test methodCLP", "testMehodCLP_"+ ((CLGStartNode) graph.getStartNode()).getMethodName().toString())) {
				System.out.println(methodCLP + "failed to compile.");
			}
		}
		/*end ocl2clp*/
		
		CLGCriterionTransformer clgTF = new CLGCriterionTransformer();
		for (CLGGraph graph : visitor.getCLGGraph()) {
			if (Main.criterion.equals(Criterion.dcc) || Main.criterion.equals(Criterion.dccdup)) {
				graph = clgTF.CriterionTransformer(graph, Criterion.dcc);
			} else if (Main.criterion.equals(Criterion.mcc) || Main.criterion.equals(Criterion.mccdup)) {
				graph = clgTF.CriterionTransformer(graph, Criterion.mcc);
			}else{
				
			}
			this.drawGraph(graph);

			CoverageCriterionManager criterionManger = new CoverageCriterionManager();
			criterionManger.init(graph);
			
			CLG2Path CLGpath = new CLG2Path((ArrayList<CLGGraph>) visitor.getCLGGraph(), null, symbolTable,classUml);
			//
			//TestScriptGenerator testScriptGenerator = new TestScriptGenerator();
			//testScriptGenerator.init(criterionManger.genTestSuite());
			//testScriptGenerator.genTestScript();
			//tcFactory.createTestCase(((CLGStartNode) graph.getStartNode()).getGraphName());
		}
		
		
	}

	public void drawGraph(CLGGraph graph) throws IOException {
		String content = graph.graphDraw();
		String filepath = ((CLGStartNode) graph.getStartNode()).getClassName()+"_WhiteBox_DCC";
		String fileName = ((CLGStartNode) graph.getStartNode()).getClassName() + "_" + ((CLGStartNode) graph.getStartNode()).getMethodName();
		DataWriter.writeInfo(content, fileName, "dot", DataWriter.output_folder_path+"\\test model", filepath);

		new ProcessBuilder("dot", "-Tpng", DataWriter.output_folder_path + "\\test model\\"+filepath+"\\" + fileName + ".dot", "-o", DataWriter.output_folder_path + "\\test model\\"+filepath+"\\" + fileName + ".png").start();
		/* need to find better approach */
	}
}
