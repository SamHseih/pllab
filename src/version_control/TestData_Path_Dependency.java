package version_control;

import java.util.ArrayList;

public class TestData_Path_Dependency {
	private ArrayList<String> testdata;
	private ArrayList<String> pathConstriant;
	private ArrayList<String> newTestData;
	
	public TestData_Path_Dependency() {
		this.testdata = new ArrayList<String>();
		this.pathConstriant = new ArrayList<String>();
		this.newTestData = new ArrayList<String>();
	}
	
	public void Build_Dependency(String TD_Path_Dependency_File) {
		String[] content = TD_Path_Dependency_File.split("\n");
		for(int i = 0; i < content.length; i+=2) {
			pathConstriant.add(content[i]);
			testdata.add(content[i+1]);
		}
	}
	
	//版本0 給一個值 避免陣列沒值出錯
	public void setpathConstriant_zeroVersion() {
		this.pathConstriant.add("nothing");
	}
	
	public ArrayList<String> getTestData() {
		return this.testdata;
	}
	
	public String getTestData(int i) {
		return this.testdata.get(i);
	}
	
	public ArrayList<String> getNewTestData() {
		return this.newTestData;
	}
	/*
	public void addNewTestData(String testdata) {
		String formalTestData = "";
		String[] spliteTestData = testdata.split(",");
		
		formalTestData += "OBJ_PRE = " + spliteTestData[0]
						+ ", ARG_PRE = " + spliteTestData[1]
						+ ",OBJ_POST = " + spliteTestData[2]
						+ ",ARG_POST = " + spliteTestData[3]
						+ ", RETVAL = " + spliteTestData[4]
						+ ", EXCEPTION =" + spliteTestData[5];
		
		this.newTestData.add(testdata);
	}
	*/
	public String getPathConstriant(int i) {
		return this.pathConstriant.get(i);
	}
	public String hasSamePath(String path) {
		for(int i = 0; i < pathConstriant.size(); i++) {
			if(pathConstriant.get(i).equals(path)) {
				String samePathNum= ""+i;
				i++;
				while(i != pathConstriant.size()) 
				{
					if(pathConstriant.get(i).equals(path))
						i++;
					else
						return samePathNum+","+ i;
				}
				return samePathNum+","+ i;
			}
		}
		return "false";
	}
}
