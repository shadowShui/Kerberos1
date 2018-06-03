package System;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class FileHandle {
	
	
	// �ж��ı��ļ����Ƿ����ĳ������, ��"\n"��Ϊ�ָ�
	public static boolean contains(String path, String word) {
		
		return contains(path, word, " ");
	}
	
	// �ж��ı��ļ����Ƿ����ĳ������
	public static boolean contains(String path, String word, String cons) {
		
		if(!isExist(path))
			return false;
		
		Set<String> wordsSet = new HashSet<String>();
		// ��ȡ�����ļ�
        Path ppath = Paths.get(path);
        byte[] readBytes = null;
        
		try {
			readBytes = Files.readAllBytes(ppath);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// ���зָ�
        String wordListContents = new String(readBytes);      
        String[] words = wordListContents.split(cons);
        // System.out.println(wordListContents);
        // System.out.println(words.length);
        
        // ���뼯��
        Collections.addAll(wordsSet, words);
        // System.out.println(wordsSet);
        return wordsSet.contains(word);
	}
	
	// �ж��ļ��Ƿ����
	public static boolean isExist(String path) {
		File f = new File(path);
		if (f.exists())
			return true;
		else
			return false;
	}
	
	// �����ļ�
	public static void creat(String path, String data) 
			throws FileNotFoundException {
		try {  
            File file = new File(path);  
            // if file doesnt exists, then create it  
            if (!file.exists()) {  
                file.createNewFile();  
            }  
  
            // true = append file  
            //FileWriter fileWritter = new FileWriter(file.getName(), true);  
            FileWriter fileWritter = new FileWriter(path);  
            fileWritter.write(data);  
            fileWritter.close();  
  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
	}
	
	
	// ���ļ�����׷������
	public static void append(String path, String data) {
		FileWriter fw = null;
		try {
			//����ļ����ڣ���׷�����ݣ�����ļ������ڣ��򴴽��ļ�
			File f = new File(path);
			fw = new FileWriter(f, true);
		} catch (IOException e) {
			e.printStackTrace();
		}
		PrintWriter pw = new PrintWriter(fw);
		pw.print(data + " ");
		pw.flush();
		try {
			fw.flush();
			pw.close();
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	// ��ȡ�ļ�����
	public static String readToString(String fileName) {  

        File file = new File(fileName);  
        Long filelength = file.length();  
        byte[] filecontent = new byte[filelength.intValue()];  
        try {  
            FileInputStream in = new FileInputStream(file);  
            in.read(filecontent);  
            in.close();  
        } catch (FileNotFoundException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
     
        return new String(filecontent);  
        
    }
	
	public static void main(String[] args) 
			throws FileNotFoundException {
		
		
		//a.method2("E:\\dd.txt", "222222222222222");
		//a.method3("E:\\dd.txt", "33333333333");
	}
}
