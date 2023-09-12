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
			// �q���A���WŪ�����w���ɮ�
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
			// �D�ʩI�s�@��getReply()�Ⱶ�U�Ӫ�226���O��. �o�˰��O�i�H�ѨM�o�Ӫ�^null���D
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
			// �s�u�ܦ��A���A��w�]��21�ɡA�i�����q�LURL�s�u 
			ftp.connect("140.123.102.101",21); 
			//ftp.connect("127.0.0.1",21);  
			// �n�J���A�� 
			ftp.login("wayger","fupjo3"); 
			// �P�_��^�X�O�_�X�k 
			if (!FTPReply.isPositiveCompletion(ftp.getReplyCode())) { 
				// ���X�k���_�}�s�u 
				ftp.disconnect(); 
				// �����{�� 	
				return result; 
			} 
		// �P�_ftp�ؿ��O�_�s�b�A�p�G���s�b�h�إߥؿ��A�]�A�إߦh�ťؿ� 
		String s = "/"+LoginWindow.account+"\\"+ storePath; 
		String[] dirs = s.split("/"); 
		ftp.changeWorkingDirectory("/");       
		//�������ˬd�ؿ��O�_�s�b�A���s�b�h�إߥؿ�  
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
		// �]�w�ɮ׾ާ@�ؿ� 
		ftp.changeWorkingDirectory(storePath); 
		// �]�w�ɮ׫��O�A�G�i��� 
		ftp.setFileType(FTPClient.BINARY_FILE_TYPE); 
		// �]�w�w�İϤj�p 
		ftp.setBufferSize(3072); 
		// �W���ɮ� 
		result = ftp.storeFile(fileName, is); 
		// ������J�y 
		is.close(); 
		// �n�X���A�� 
		ftp.logout(); 
		} catch (IOException e) { 
		e.printStackTrace(); 
		} finally { 
		try { 
		// �P�_��J�y�O�_�s�b 
		if (null != is) { 
		// ������J�y 
		is.close(); 
		} 
		// �P�_�s�u�O�_�s�b 
		if (ftp.isConnected()) { 
		// �_�}�s�u 
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
			//�U�� .java
			try {
				String refundList = "";
				// �q���A���WŪ�����w���ɮ�
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
					// �D�ʩI�s�@��getReply()�Ⱶ�U�Ӫ�226���O��. �o�˰��O�i�H�ѨM�o�Ӫ�^null���D
					ftp.getReply();
				} 
				else {
					System.out.println("...");
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			//�U�� .ocl
			try {
				String refundList = "";
				// �q���A���WŪ�����w���ɮ�
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
					// �D�ʩI�s�@��getReply()�Ⱶ�U�Ӫ�226���O��. �o�˰��O�i�H�ѨM�o�Ӫ�^null���D
					ftp.getReply();
				} 
				else {
					System.out.println("...");
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			//�U�� .uml
			try {
				String refundList = "";
				// �q���A���WŪ�����w���ɮ�
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
					// �D�ʩI�s�@��getReply()�Ⱶ�U�Ӫ�226���O��. �o�˰��O�i�H�ѨM�o�Ӫ�^null���D
					ftp.getReply();
				} 
				else {
					System.out.println("...");
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			//�U�� testscript
			try {
				String refundList = "";
				// �q���A���WŪ�����w���ɮ�
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
					// �D�ʩI�s�@��getReply()�Ⱶ�U�Ӫ�226���O��. �o�˰��O�i�H�ѨM�o�Ӫ�^null���D
					ftp.getReply();
				} 
				else {
					System.out.println("...");
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			//�U�� test data
			
			try {
				String refundList = "";
				// �q���A���WŪ�����w���ɮ�
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
						}																//��.txt�R��
						DataWriter.writeInfo(refundList, testdataList[i].substring(0, testdataList[i].length()-4), "txt",
								download_path + "\\" + projectname + " " + version + "\\test data\\");
						reader.close();
					}
					else {
						System.out.println("...");
					}
					ins.close();
					// �D�ʩI�s�@��getReply()�Ⱶ�U�Ӫ�226���O��. �o�˰��O�i�H�ѨM�o�Ӫ�^null���D
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
