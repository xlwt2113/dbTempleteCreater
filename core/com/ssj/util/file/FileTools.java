package com.ssj.util.file;

import java.io.File;
import java.io.FileFilter;
import java.text.DecimalFormat;

public class FileTools
{
    public static String getAutoFileSize(int fileSize){
        
        DecimalFormat formater = new DecimalFormat();
        formater.applyPattern("###.##");
        if(fileSize < 1024){
            return fileSize + " B";
        }
        else if(fileSize < 1024*1024){
            return formater.format(fileSize/1024f) + " KB";
        }
        else if(fileSize < 1024*1024*1024){
            return formater.format(fileSize/(1024*1024f)) + " MB";
        }
        else{
            return formater.format(fileSize/(1024*1024*1024f)) + " GB";
        }
    }

    public static void travelFolder(File folder,FileCallback fcb){
    	travelFolder(folder,fcb,new FileFilter(){
			public boolean accept(File pathname) {
				return true;
			}});
    }
    
    public static void travelFolder(File folder,FileCallback fcb,FileFilter filter){
    	if(folder==null||!folder.exists())
    		return;
    	File[] fs = folder.listFiles(filter);
    	for(int i=0;i<fs.length;i++){
    		fcb.dealFile(fs[i]);
    		if(fs[i].isDirectory()){
    			travelFolder(fs[i],fcb,filter);
    		}
    	}
    }
    
}
