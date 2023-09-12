package version_control;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import ccu.pllab.tcgen.DataWriter.DataWriter;

public class FileHandleToServer {
/*	public static void main(String[] args) throws IOException {
		//File f = new File("D:\\runtime-New_configuration\\Grade\\spec\\Grade.java");
		//FileInputStream test = new FileInputStream(f);
		//System.out.println(storeFile("grade\\version_1","grade.java",test));
	}*/
	
	public static String readFile(String filepath, String fileName) throws SocketException, IOException {
		String refundList = new String();
		try {
		FTPClient ftp = new FTPClient(); 
		ftp.connect("140.123.102.101",21); 
		//ftp.connect("127.0.0.1",21); 
		ftp.login("wayger","fupjo3");
		ftp.changeWorkingDirectory(LoginWindow.account + "\\" + filepath);
		
		InputStream ins = null;
		
		try {
			// 從伺服器上讀取指定的檔案
			ins = ftp.retrieveFileStream(fileName);
			if(ins != null){
			BufferedReader reader = new BufferedReader(new InputStreamReader(ins));
			String line = "";
			while ((line = reader.readLine()) != null) {
				line+="\n";
				refundList+=line;
			}
			reader.close();
			ins.close();
			// 主動呼叫一次getReply()把接下來的226消費掉. 這樣做是可以解決這個返回null問題
			ftp.getReply();
			}else {
				System.out.println("...");
			}
			} catch (IOException e) {
			e.printStackTrace();
			}
		
		} catch(Exception ex) {
			System.out.println(ex);
		}
		return refundList;
	}
	
	public static boolean storeFile (String storePath, String fileName, InputStream is) throws IOException { 
		boolean result = false; 
		FTPClient ftp = new FTPClient(); 
		try { 
			// 連線至伺服器，埠預設為21時，可直接通過URL連線 
			ftp.connect("140.123.102.101",21); 
			//ftp.connect("127.0.0.1",21);  
			// 登入伺服器 
			ftp.login("wayger","fupjo3"); 
			// 判斷返回碼是否合法 
			if (!FTPReply.isPositiveCompletion(ftp.getReplyCode())) { 
				// 不合法時斷開連線 
				ftp.disconnect(); 
				// 結束程式 	
				return result; 
			} 
		// 判斷ftp目錄是否存在，如果不存在則建立目錄，包括建立多級目錄 
		String s = "/"+LoginWindow.account+"\\"+ storePath; 
		String[] dirs = s.split("/"); 
		ftp.changeWorkingDirectory("/");       
		//按順序檢查目錄是否存在，不存在則建立目錄  
		for(int i=1; dirs!=null&&i<dirs.length; i++) {  
		if(!ftp.changeWorkingDirectory(dirs[i])) {  
		if(ftp.makeDirectory(dirs[i])) {  
		if(!ftp.changeWorkingDirectory(dirs[i])) {  
		return false;  
		}  
		}else {  
		return false;              
		}  
		}  
		}  
		// 設定檔案操作目錄 
		ftp.changeWorkingDirectory(storePath); 
		// 設定檔案型別，二進位制 
		ftp.setFileType(FTPClient.BINARY_FILE_TYPE); 
		// 設定緩衝區大小 
		ftp.setBufferSize(3072); 
		// 上傳檔案 
		result = ftp.storeFile(fileName, is); 
		// 關閉輸入流 
		is.close(); 
		// 登出伺服器 
		ftp.logout(); 
		} catch (IOException e) { 
		e.printStackTrace(); 
		} finally { 
		try { 
		// 判斷輸入流是否存在 
		if (null != is) { 
		// 關閉輸入流 
		is.close(); 
		} 
		// 判斷連線是否存在 
		if (ftp.isConnected()) { 
		// 斷開連線 
		ftp.disconnect(); 
		} 
		} catch (IOException e) { 
		e.printStackTrace(); 
		} 
		} 
		return result; 
	} 
	
	public static void DownloadFile(String projectname, String version, String download_path) {
		try {
			FTPClient ftp = new FTPClient(); 
			ftp.connect("140.123.102.101",21); 
			//ftp.connect("127.0.0.1",21); 
			ftp.login("wayger","fupjo3");
			ftp.changeWorkingDirectory(LoginWindow.account + "\\" + projectname + "\\" + version);
			
			InputStream ins = null;
			//下載 .java
			try {
				String refundList = "";
				// 從伺服器上讀取指定的檔案
				ins = ftp.retrieveFileStream(projectname + ".java");
				if(ins != null){
					BufferedReader reader = new BufferedReader(new InputStreamReader(ins));
					String line = "";
					while ((line = reader.readLine()) != null) {
						line+="\n";
						refundList+=line;
					}
					DataWriter.writeInfo(refundList, projectname, "java",
							download_path + "\\" + projectname + " " + version + "\\src\\");
					reader.close();
					ins.close();
					// 主動呼叫一次getReply()把接下來的226消費掉. 這樣做是可以解決這個返回null問題
					ftp.getReply();
				} 
				else {
					System.out.println("...");
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			//下載 .ocl
			try {
				String refundList = "";
				// 從伺服器上讀取指定的檔案
				ins = ftp.retrieveFileStream(projectname + ".ocl");
				if(ins != null){
					BufferedReader reader = new BufferedReader(new InputStreamReader(ins));
					String line = "";
					while ((line = reader.readLine()) != null) {
						line+="\n";
						refundList+=line;
					}
					DataWriter.writeInfo(refundList, projectname, "ocl",
							download_path + "\\" + projectname + " " + version + "\\spec\\");
					reader.close();
					ins.close();
					// 主動呼叫一次getReply()把接下來的226消費掉. 這樣做是可以解決這個返回null問題
					ftp.getReply();
				} 
				else {
					System.out.println("...");
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			//下載 .uml
			try {
				String refundList = "";
				// 從伺服器上讀取指定的檔案
				ins = ftp.retrieveFileStream(projectname + ".uml");
				if(ins != null){
					BufferedReader reader = new BufferedReader(new InputStreamReader(ins));
					String line = "";
					while ((line = reader.readLine()) != null) {
						line+="\n";
						refundList+=line;
					}
					DataWriter.writeInfo(refundList, projectname, "uml",
							download_path + "\\" + projectname + " " + version + "\\spec\\");
					reader.close();
					ins.close();
					// 主動呼叫一次getReply()把接下來的226消費掉. 這樣做是可以解決這個返回null問題
					ftp.getReply();
				} 
				else {
					System.out.println("...");
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			//下載 testscript
			try {
				String refundList = "";
				// 從伺服器上讀取指定的檔案
				ins = ftp.retrieveFileStream(projectname + "Test.txt");
				if(ins != null){
					BufferedReader reader = new BufferedReader(new InputStreamReader(ins));
					String line = "";
					while ((line = reader.readLine()) != null) {
						line+="\n";
						refundList+=line;
					}
					DataWriter.writeInfo(refundList, projectname+"Test", "txt",
							download_path + "\\" + projectname + " " + version + "\\test script\\");
					reader.close();
					ins.close();
					// 主動呼叫一次getReply()把接下來的226消費掉. 這樣做是可以解決這個返回null問題
					ftp.getReply();
				} 
				else {
					System.out.println("...");
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			//下載 test data
			
			try {
				String refundList = "";
				// 從伺服器上讀取指定的檔案
				String[] testdataList = getFileList(projectname+"\\"+version+ "\\test data");
				ftp.changeWorkingDirectory("test data");
				for(int i = 0; i < testdataList.length; i++) {
					ins = ftp.retrieveFileStream(testdataList[i].toString());
					if(ins != null){
						BufferedReader reader = new BufferedReader(new InputStreamReader(ins));
						String line = "";
						while ((line = reader.readLine()) != null) {
							line+="\n";
							refundList+=line;
						}																//把.txt刪掉
						DataWriter.writeInfo(refundList, testdataList[i].substring(0, testdataList[i].length()-4), "txt",
								download_path + "\\" + projectname + " " + version + "\\test data\\");
						reader.close();
					}
					else {
						System.out.println("...");
					}
					ins.close();
					// 主動呼叫一次getReply()把接下來的226消費掉. 這樣做是可以解決這個返回null問題
					ftp.getReply();
				} 		
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			
		} catch(Exception ex) {
			System.out.println(ex);
		  }
				
	}
	
	public static String[] getFileList(String program) {
		String[] fileList = null;
		
		try {
			FTPClient ftp = new FTPClient(); 
			ftp.connect("140.123.102.101",21); 
			//ftp.connect("127.0.0.1",21); 
			ftp.login("wayger","fupjo3");
			ftp.changeWorkingDirectory(LoginWindow.account + "\\" + program);
			fileList = ftp.listNames();

			
		} catch(Exception ex) {
			System.out.println(ex);
		}
		
		return fileList;
	}
}
