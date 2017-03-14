package com.ssj.util.clazz;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

import com.ssj.util.LogUtil;


 

public class MyClassLoader
    extends URLClassLoader {

  public MyClassLoader() {
    super(new URL[0], ClassLoader.getSystemClassLoader());
  }

  public void addPath(String paths) {
    if (paths == null || paths.length() <= 0) {
      return;
    }
    String separator = System.getProperty("path.separator");
    String[] pathToAdds = paths.split(separator);
    for (int i = 0; i < pathToAdds.length; i++) {
      if (pathToAdds[i] != null && pathToAdds[i].length() > 0) {
        try {
          File pathToAdd = new File(pathToAdds[i]).getCanonicalFile();
          if(pathToAdd.isDirectory()){
        	  this.addDir(pathToAdd);
          }
          else{
        	  addURL(pathToAdd.toURL());
          }
        }
        catch (IOException e) {
          LogUtil.logError(e);
        }
      }
    }
  }
  
  private void addDir(File dir) throws MalformedURLException, IOException{
	  if(dir==null || !dir.isDirectory())return;
	  File[] fs = dir.listFiles();
	  for(int i=0;i<fs.length;i++){
		  if(fs[i].isDirectory()){
			  addDir(fs[i]);
		  }
		  else{
			  addURL(fs[i].getCanonicalFile().toURL());
		  }
	  }
  }
  
  @SuppressWarnings("unchecked")
public static void main(String[] args){
	  MyClassLoader cl = new MyClassLoader();
	  cl.addPath("C:/csmis/jar");
	  try {
		Class cla = cl.loadClass("com.csmis.util.TreeNode");
		Method[] fs = cla.getMethods();
		for(int i=0;i<fs.length;i++){
			System.out.println(fs[i].getName());
		}
	} catch (ClassNotFoundException e) {
		
		LogUtil.logError(e);
	}
  }

}

