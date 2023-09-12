/*
 * 20180331 黎怡伶
 * 取得用Papyrus繪製成的類別圖資訊
 */


package ccu.pllab.tcgen.PapyrusCDParser;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.parsers.DocumentBuilder;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import ccu.pllab.tcgen.AbstractType.*;
import ccu.pllab.tcgen.DataWriter.DataWriter;
import ccu.pllab.tcgen.launcher.BlackBoxLauncher;
import ccu.pllab.tcgen.transform.Splitter;
import ccu.pllab.tcgen.transform.UmlTransformer;
import tcgenplugin_2.handlers.BlackBoxHandler;

import org.w3c.dom.Node;
import org.w3c.dom.Element;


public class SingleCDParser {

	private ArrayList<Document> ref = null;  // 用來放其自訂型别需參考的其他類別圖
	private ArrayList<ClassInfo> classList ; // 用來放主要
	private ArrayList<UserDefinedType> ref_typelist; // 用來記錄其他自定義類別
	private String packageName=null;
	private Document doc ;
	
	// constructor ===========================================================================
	// 單一圖
	public SingleCDParser() {
		// this.cd = cd ;
		// init(cd);
	} 
	// constructor ===========================================================================
	
	
	
	// 外部拿取資料 method =======================================================================
	// Get package name
	
	public String getPkgName() {
		return this.packageName;   // XML根節點
	}
	
	
	// Get Class List
	public ArrayList<ClassInfo> getClassList() {
		return classList;
	}
	// 外部拿取資料 method =======================================================================
	
	
	
	/*
	 * 以下為Parse主要及所需method
	 * 
	 * 1. 將類別圖轉成dom樹狀結構 : init(), initRef()
	 * 2. 分析並取得類別圖裡的資訊 : Parse()
	 */ 
	
	// NO.1 =====================================================================================
	// 把主要要測試的類別的類別圖轉成dom
	public Document init( File uml ) {
		// TODO Auto-generated method stub
		try {
			Splitter split = new Splitter(uml);
			File cd = split.split2CDuml();
			
			// 定義XML DOM parser解析器
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			// 建立DOM document
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			// 引入xml
			Document doc = dBuilder.parse(cd);
			
			// 針對xml文檔的元素做normalize
		    doc.getDocumentElement().normalize();
		    this.packageName=doc.getDocumentElement().getAttribute("name");
		    return doc;
		    
		} catch (Exception e) {
			 e.printStackTrace();
	    }
		return null;
	} // init()
	
	/*
	// 把參考用的uml圖list 轉成dom
	private void initRef( ArrayList<File> rList ) {
		try {
			this.ref = new ArrayList<Document>();
			
			// 定義XML DOM parser解析器
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			// 建立DOM document
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			
			for(int i = 0 ; i < rList.size() ; i++ ) {
			    doc = dBuilder.parse(rList.get(i));       // 引入xml
			    doc.getDocumentElement().normalize();   // 針對xml文檔的元素做normalize
			    this.ref.add(doc) ;
			} // for
		    
		} catch (Exception e) {
			 e.printStackTrace();
	    }
	} // initRef()
	*/
	// NO.1 =====================================================================================
	
	
	
	// NO.2 =====================================================================================
	// 取得類別圖的所有class的資訊
	public void Parse(Document doc) throws ParserConfigurationException, SAXException, IOException, TransformerException {
		classList = new ArrayList<ClassInfo>() ;
		
		// 取得所有class
		NodeList nList = doc.getElementsByTagName("packagedElement");
		
		for (int i = 0; i < nList.getLength(); i++) {
			ArrayList<VariableInfo> property_List = null ;
			ArrayList<OperationInfo> operation_List = null ;
			ClassInfo c = new ClassInfo();
			UserDefinedType this_class_type = null;
			
            Node node = nList.item(i);                // 第i個class
            if (node.getNodeType() == Node.ELEMENT_NODE) {  
                Element e = (Element) node;
                c.setName(e.getAttribute("name"));    // class name
                c.setID(e.getAttribute("xmi:id"));    // class id
    			this_class_type = new UserDefinedType(c.getName(), c.getID());
    			BlackBoxLauncher.typeTable.add(this_class_type);
    			
    		    System.out.println(BlackBoxLauncher.typeTable.printTypeTableInfo());
                // ---------- Property ----------
                NodeList pList = e.getElementsByTagName("ownedAttribute");   // 取得此class的所有property
                if( pList.getLength() != 0 ) property_List = new ArrayList<VariableInfo>() ;
                
                // 取得此class裡的各個property的type & name
                for( int j = 0 ; j < pList.getLength() ; j++ ) {
                	Element child = (Element) pList.item(j);
                	VariableInfo p = new VariableInfo( getVarType(child, c.getID(), c.getName()), child.getAttribute("name"), 
                			                           child.getAttribute("xmi:id"), getAttrVisibility(child), 
                			                           c.getName() );
                	if (child.getElementsByTagName("lowerValue").item(0) != null) {
                		Element temp = (Element) child.getElementsByTagName("lowerValue").item(0);
                		if( temp.hasAttribute("value") ) p.setLowerValue(temp.getAttribute("value"));
                		else p.setLowerValue("0");
                	} // if
                	
                	if (child.getElementsByTagName("upperValue").item(0) != null) {
                		Element temp = (Element) child.getElementsByTagName("upperValue").item(0);
                		p.setUpperValue(temp.getAttribute("value"));
                	} // if
                	
                	// ArrayType
                	if(!p.getLowerValue().equals("1") && !p.getUpperValue().equals("1")) {
                		p.setType(new ArrayType(p.getType(), p.getUpperValue()));
                	}
                	
                	property_List.add( p ) ;    // 把property加進list
                } // for
                // ---------- Property ----------
                
                
                // ---------- Operation ----------
                NodeList oList = e.getElementsByTagName("ownedOperation");   // 取得此class的所有operation
                if( oList.getLength() != 0 ) operation_List = new ArrayList<OperationInfo>() ;
                
                // 取得此class裡的各個operation的name, parameter, return type 
                for( int k = 0 ; k < oList.getLength() ; k++ ) {
                	OperationInfo method = new OperationInfo();
                	Element child = (Element) oList.item(k);
                	method.setName(child.getAttribute("name"));     // 取得method name
                	method.setID(child.getAttribute("xmi:id"));     // 取得method id
                	method.setVisibility(getAttrVisibility(child)); // 取得method visibility
                	method.setClassName(c.getName());               // 設定所屬類別
                	
                	NodeList parameter = child.getElementsByTagName("ownedParameter");  // 取得所有參數(包括return的變數)
                	System.out.println("CD Parser / Class Name: " + method.getName()+", ParaNum: " + parameter.getLength());
                	
                	// method 沒有參數 , return type為void
                	if(parameter.getLength()== 0 ) {
                		System.out.println("CD Parser: No Arg.");
                		VariableInfo rt = new VariableInfo(new VoidType(), "", "", "", c.getName());
                		method.setReturnType(rt);
                	} // if
                	
                	// 取得method所有參數的name, type 以及 return type
                	else {
                	  ArrayList<VariableInfo> parameter_List = new ArrayList<VariableInfo>();
                      for( int p_i = 0 ; p_i < parameter.getLength() ; p_i++ ) {
                    	  Element eParameter = (Element) parameter.item(p_i);
                    	  
                          VariableInfo p = new VariableInfo( getVarType(eParameter,c.getID(),c.getName()), eParameter.getAttribute("name"), 
                          			                         eParameter.getAttribute("xmi:id"), "", c.getName() );
                          	
                          if (eParameter.getElementsByTagName("lowerValue").item(0) != null) {
                         	  Element temp = (Element) child.getElementsByTagName("lowerValue").item(0);
                       		  if( temp.hasAttribute("value") ) p.setLowerValue(temp.getAttribute("value"));
                       		  else p.setLowerValue("0");
                       	  } // if
                        	
                          if (eParameter.getElementsByTagName("upperValue").item(0) != null) {
                              Element temp = (Element) child.getElementsByTagName("upperValue").item(0);
                        	  p.setUpperValue(temp.getAttribute("value"));
                          } // if
                    	  // System.out.println(p.GetType()+" " + p.GetName());
                          
                      	// ArrayType
                      	if(!p.getLowerValue().equals("1") && !p.getUpperValue().equals("1")) {
                      		p.setType(new ArrayType(p.getType(), p.getUpperValue()));
                      	}
                        	
                      	  // 參數
                      	  if ( ! eParameter.getAttribute("direction").equals("return"))
                      		  parameter_List.add( p ) ;    // 把parameter加進list
                    	  
                    	  // return variable
                    	  else method.setReturnType( p );
                      	  
                      } // for
                      
                      
                      if( parameter_List.size()!=0 )    // 表示method有參數
                    	  method.setParameter(parameter_List);
                      
                      // 回傳型別為void
                      if(method.getReturnType()==null) {
                    	  System.out.println("VOID");
                    	  VariableInfo rt = new VariableInfo(new VoidType(), "", "", "", c.getName());
                  		  method.setReturnType(rt);
                      }
                	} // else
                	
                	operation_List.add( method ) ;    // 把operation加進list
                } // for
                // ---------- Operation ----------
                
                c.setProperties(property_List);     // class property
                c.setOperations(operation_List);    // class operation
            } // if
        	
        	this_class_type.setClassInfo(c);
            classList.add(c);   // 把一個取得資訊後的classInfo加進class list
		} // for
		
	} // Parse()
	// NO.2 =====================================================================================
	
	
	
	// 於Parse()中所用到的method =================================================================
	
	// 取得property的type: 自建立的型別, 基本型別, 陣列
	private String getVarType_Str(Element e) {
    	if(e.hasAttribute("type")) return e.getAttribute("type");
    	
    	else {
    		Element et = (Element)e.getElementsByTagName("type").item(0);
        	String type_id = et.getAttribute("href");
        	type_id = type_id.substring(type_id.indexOf("#")+1, type_id.length());
    		return type_id;
    	}
	}
	
	private String getVarType(Element e) {
    	if(e.hasAttribute("type")) return e.getAttribute("type");
    	else {
    		Element et = (Element)e.getElementsByTagName("type").item(0);
    		String s = et.getAttribute("href");
    		s = s.substring(s.indexOf("#")+1, s.length());
    		return s;
    	}
    	
	}
	// 取得property的type: 自建立的型別, 基本型別, 陣列
	private VariableType getVarType(Element e, String classID, String className ) throws ParserConfigurationException, SAXException, IOException, TransformerException {
		/*
		// 2020/05 可能都只用下面的程式碼了
    	if(e.hasAttribute("type")) return e.getAttribute("type");
    	*/
		if(e.hasAttribute("type") ) {//同類別圖的   待修
			if(BlackBoxLauncher.typeTable.containsType(e.getAttribute("type"), e.getAttribute("type")))
				return BlackBoxLauncher.typeTable.get(e.getAttribute("type"), e.getAttribute("type"));
			//else return new UnDefinedType(e.getAttribute("type"));// 還未讀取便先用到的 待修改
		}
		
		
    	// 如果使用外部的type，會另外有個tag為type的子節點，由裏頭的xmi:type判別為基本型別或是自定義型別
    	Element et = (Element)e.getElementsByTagName("type").item(0);


    	// 基本型別(int, float, boolean, char, ..)
    	if(et.getAttribute("xmi:type").equals("uml:PrimitiveType")) {
        	String s = et.getAttribute("href");
        	s = s.substring(s.indexOf("#")+1, s.length());
        	
        	/*判斷是否是array*/        	
        	String low = "1", up = "1";
        	
        	if (e.getElementsByTagName("lowerValue").item(0) != null) {
        		Element temp = (Element) e.getElementsByTagName("lowerValue").item(0);
        		if( temp.hasAttribute("value") )low = temp.getAttribute("value");
        	} // if
        	
        	if (e.getElementsByTagName("upperValue").item(0) != null) {
        		Element temp = (Element) e.getElementsByTagName("upperValue").item(0);
        		up = temp.getAttribute("value");
        	} // if
        	
        	// ArrayType
        	if(!low.equals("1") && !up.equals("1")) {
        		ArrayType new_arrType = new ArrayType(BlackBoxLauncher.typeTable.get(s, s), up);
        		BlackBoxLauncher.typeTable.add(new_arrType);
        		return new_arrType;
        	}
        	/*判斷是否是array*/
        	
        	else return BlackBoxLauncher.typeTable.get(s, s);
    	} // if
    	
    	// 自定義型別 (ArrayList, String, UserDefinedType)
    	else {
        	String s = et.getAttribute("href");
        	s = s.substring(0, s.indexOf("."));
        	
        	VariableType tempType = null;
        	
        	if(s.equals("ArrayList")) { // ArrayList的話要另外抓裡面的element type
        		String comment_type = e.getElementsByTagName("ownedComment").item(0).getTextContent().trim();
        		System.out.println("CD Parser Test Type [ArrayList]: "+ comment_type);
        		// comment_type = comment_type.substring( comment_type.indexOf("<")+1,  comment_type.length()) ;
        		tempType = createArrayListType(comment_type);
        		BlackBoxLauncher.typeTable.add(tempType);
        	} // if
        	
        	else if ( s.equals("String") )tempType = BlackBoxLauncher.typeTable.get("string", "string") ;
        	
        	else {
        		tempType = createUserDefinedType( s ); 
        	} // else

        	
        	/*判斷是否是array*/        	
        	String low="1", up = "1";
        	
        	if (e.getElementsByTagName("lowerValue").item(0) != null) {
        		Element temp = (Element) e.getElementsByTagName("lowerValue").item(0);
        		if( temp.hasAttribute("value") )low = temp.getAttribute("value");
        	} // if
        	
        	if (e.getElementsByTagName("upperValue").item(0) != null) {
        		Element temp = (Element) e.getElementsByTagName("upperValue").item(0);
        		up = temp.getAttribute("value");
        	} // if
        	
        	// ArrayType
        	if(!low.equals("1") && !up.equals("1")) {
        		ArrayType new_arrType = new ArrayType(tempType, up);
        		BlackBoxLauncher.typeTable.add(new_arrType);
        		return new_arrType;
        	}
        	/*判斷是否是array*/
        	
        	else return tempType;

    	} // else
	} // getVarType()
	
	
	
	
	// 創建自定義的ArrayListType結構
	private ArrayListType createArrayListType( String s) throws ParserConfigurationException, SAXException, IOException, TransformerException {	
		System.out.println("ArrayList String: " + s);
		String type_str = null;
		String element = s.substring(s.indexOf("<")+1, s.length()-1);
		System.out.println(s+"::"+element);
		if( element.contains("<") && element.contains(">") )
			type_str = s.substring(0, s.indexOf("<") );
		
		System.out.println(s+"::"+type_str+"::"+element);
		if( BlackBoxLauncher.typeTable.containsType(element, element)) {
			return new ArrayListType( BlackBoxLauncher.typeTable.get(element, element) );
		}
		else if( type_str != null && type_str.equals("ArrayList") )  // 多維 muti-D
			return new ArrayListType( createArrayListType(element) );
		else
			return new ArrayListType( createUserDefinedType(element) );
	} // createArrayListType()
	
	
	// 創建使用者自定義的UserDefinedType結構
	private UserDefinedType createUserDefinedType( String s ) throws ParserConfigurationException, SAXException, IOException, TransformerException {
		//去spec資料夾找同名的類別圖，並傳入PapyrusCDParser分析
		//從ClassInfo拿資料
		//創建結構並回傳
		Path CDumlPath = Paths.get(DataWriter.output_folder_path+"/spec/"+s+".uml");
		File uml = CDumlPath.toFile();
		Splitter split = new Splitter(uml);
		File cdUml = split.split2CDuml();
		
		SingleCDParser parser = new SingleCDParser();
		Document ref_doc = parser.init(cdUml);
		NodeList nList = ref_doc.getElementsByTagName("packagedElement");
		Node node = nList.item(0);                // 只有一個class (如果那張圖有多個class，之後這邊要改
        if (node.getNodeType() == Node.ELEMENT_NODE) {  
        	Element e = (Element) node;
        	String type_name = e.getAttribute("name");    // class name
        	String type_id = e.getAttribute("xmi:id");    // class id
        	
        	if( ! BlackBoxLauncher.typeTable.containsType( type_name, type_id) ) {
            	parser.Parse(ref_doc);
        	} // if
        	
        	return ((UserDefinedType)BlackBoxLauncher.typeTable.get( type_name, type_id ));

        } // if
        
        return null;
	} // createUserDefinedType()
	
	
	// 取得visibility, 若無設定此屬性, 則設為預設的"public"
	private String getAttrVisibility(Element e) {
    	if(e.hasAttribute("visibility")) return e.getAttribute("visibility");
    	else return "public" ;
	}
	

	// 判斷是否有return值
	private boolean hasReturn(NodeList list) {
		boolean result = false ;
		for(int i = 0 ; i < list.getLength() ; i++) {
			Element e = (Element) list.item(i);
			if ( e.hasAttribute("direction") && e.getAttribute("direction").equals("return") )
				return true;
		} // for
		
		return result;
	}
	
	
	// 取得type為array的property的size(lowerValue & upperValue)
	private String getSize(Element e) {
    	if(e.hasAttribute("type")) return e.getAttribute("type");
    	else {
    		Element et = (Element)e.getElementsByTagName("type").item(0);
    		String s = et.getAttribute("href");
    		s = s.substring(s.indexOf("#")+1, s.length());
    		return s;
    	}
    	
	}
	
	
	/*
	private void changeRef() {
		for(int i =0; i < this.getClassList().size();i++) {
			 now = this.getClassList().get(i);
			if( searchRefUserDefinedType(now.getID(), now.getName()) == null );
			else 
				
				
		for( int i = 0 ; i < classList.size() ; i++ ) {
			ClassInfo c =classList.get(i);
			for(int j= 0; c.getProperties()!= null && j < c.getProperties().size();j++ ) {
			    VariableInfo p = c.getProperties().get(j);
			    if(p.getType() instanceof UserDefinedType) {
					if( searchRefUserDefinedType(now.getID(), now.getName()) == null );
			    }
			    	
			    }
			    	
			   for(int k= 0; c.getOperations()!= null && k < c.getOperations().size();k++ ) {
				   OperationInfo o = c.getOperations().get(k);
			    	s=s+o.getReturnType().getType().toString() +" " + o.getName() + " " + o.getID() + " " + o.getVisibility()+ " " + o.getClassName();
			    	s=s+"Parameter: ";
			    	for(int index = 0 ;o.getParameter()!= null && index < o.getParameter().size();index++) {
				    	VariableInfo p = o.getParameter().get(index);
				    	s=s+p.getType().toString() + " " + p.getName() + " " + p.getID() + " " + p.getVisibility()+ " " + p.getClassName();
			    	 }
			    	   s=s+"\n";
			    	}
			    	s=s+"\n================================";
			    }
	}*/
	/*
	// 2020/05 以下函式可能不會用到 -----------------------------------------------------------------------------------------------------------------------------------------
	
	// 將變數的型別與自己建立的class的id做對應, 把id改成class的name
	// 如果型別是陣列要把在name後面加上[]                                   ****待改
	public void changeTypeStr() {
		
		for (int  i= 0 ; i < this.classList.size();i++){
		    // class local variable
            for(int j = 0 ; this.classList.get(i).getProperties() != null && 
            		j < this.classList.get(i).getProperties().size();j++) {
            	VariableInfo p = this.classList.get(i).getProperties().get(j);
            	p.setType(typeIDtoName(p.getType()));
            	
            	// if(p.getSize() != null) p.setType(p.getType()+"[]"); // size非空即為陣列
		    } // for
            

		    // class method 
            for(int j = 0 ; this.classList.get(i).getOperations() != null &&
            		j < this.classList.get(i).getOperations().size();j++){
            	OperationInfo o=this.classList.get(i).getOperations().get(j);
            	// class method parameter
            	for(int para_i = 0 ; o.getParameter() != null && 
            			para_i < o.getParameter().size();para_i++) {
            		VariableInfo parameter = o.getParameter().get(para_i);
            		parameter.setType(typeIDtoName(parameter.getType()));
            		
            		// if(parameter.getSize() != null) parameter.setType(parameter.getType()+"[]"); // size非空即為陣列
                } // for
            	
            	// return type
            	VariableInfo rt = o.getReturnType() ;
            	if(o.getReturnType()!=null)
            	rt.setType(typeIDtoName(rt.getType()));
            	
            	// *** if(o.getReturnType().getSize() != null) o.setType(p.getType()+"[]"); // size非空即為陣列
            }//for operation
		}
	} // changeTypeStr()
	
	
	// 如果是自定義的型別, 在類別圖裡type是以class ID的形式記錄
	// 所以要去將id轉換成相對應的型別名稱
	// 被changeTypeStr()呼叫
	private String typeIDtoName( String type ) {
		// System.out.println(classList.size());
		String s = type ;
		for(int k = 0 ; k < this.classList.size();k++ ) {
			// System.out.println((k+1) + this.classList.get(k).getID());
			
			if (type.equals(this.classList.get(k).getID())) {
				// System.out.println(type + "**"+this.classList.get(k).getID() + " "+ this.classList.get(k).getName());
				s = this.classList.get(k).getName();
			}
		} //for
		
		return s;
	} // typeIDtoName()
	
	
	public void parseRef() {
		NodeList list = doc.getElementsByTagName("packageImport");
		Node node = list.item(4);                // 第i個class
         if (node.getNodeType() == Node.ELEMENT_NODE) {  
             Element e = (Element) node;
             NodeList child = e.getElementsByTagName("importedPackage");
             Element ch = (Element) child.item(0);
             System.out.println(ch.getAttribute("href"));
		File f = new File(ch.getAttribute("href"));
		if( f.exists() ) System.out.println("O");
		else System.out.println("X");
         }
		
	}
	*/
	// 於Parse()中所用到的method =================================================================

	
	
	
	//    拿取資料範例
	public String printParseClassInfo( ClassInfo c ) {
		// TODO Auto-generated method stub
		try {
			String s = "";
  
			   s= "Class Name: " /*+(i+1)*/ + " "+c.getName() +/* " " + c.getID()+*/ "\n"+"Attributes: ";
		    	for(int j= 0; c.getProperties()!= null && j < c.getProperties().size();j++ ) {
		    		VariableInfo p = c.getProperties().get(j);
		    		s=s+p.getType().toString() + " " + p.getName() + " " + p.getID() + " " + p.getVisibility() + " " + p.getClassName();
		    	}
		    	
		    	s=s+"\nOperations: ";
		    	for(int k= 0; c.getOperations()!= null && k < c.getOperations().size();k++ ) {
		    		OperationInfo o = c.getOperations().get(k);
		    		s=s+o.getReturnType().getType().toString() +" " + o.getName() + " " + o.getID() + " " + o.getVisibility()+ " " + o.getClassName();
		    		s=s+"Parameter: ";
		    	    for(int index = 0 ;o.getParameter()!= null && index < o.getParameter().size();index++) {
			    		VariableInfo p = o.getParameter().get(index);
			    		s=s+p.getType().toString() + " " + p.getName() + " " + p.getID() + " " + p.getVisibility()+ " " + p.getClassName();
		    	    }
		    	    s=s+"\n";
		    	}
		    	s=s+"\n================================";
		    	
		    return s;

		} catch (Exception e) {
			 e.printStackTrace();
	    }
		return "Nothing";
	}
	
	

}
