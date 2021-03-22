package com.buby.blocknet.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.stream.Stream;

public class CommonUtils {
	public static void log(String str, Object... args) {
		System.out.println(String.format(str, args));
	}
	
	public static class FileUtil{	
		
		public static File copyFolder(File src, File dest){
		    try (Stream<Path> stream = Files.walk(src.toPath())) {
		        stream.forEach(source -> copy(source, dest.toPath().resolve(src.toPath().relativize(source))));
		    }catch(Exception e) {
		    	e.printStackTrace();
		    }
		    return dest;
		}

		private static void copy(Path source, Path dest) {
		    try {
		        Files.copy(source, dest, StandardCopyOption.REPLACE_EXISTING);
		    } catch (Exception e) {
		    	e.printStackTrace();
		    }
		}

		public static void deleteFile(String path) {
			File file = new File(path);
			if(!file.exists()) return;
			deleteDirectory(file);
		}
		
		private static boolean deleteDirectory(File directoryToBeDeleted) {
		    File[] allContents = directoryToBeDeleted.listFiles();
		    if (allContents != null) {
		        for (File file : allContents) {
		            deleteDirectory(file);
		        }
		    }
		    return directoryToBeDeleted.delete();
		}
		
		public static boolean unlockFiles(File dir) {
		    File[] allContents = dir.listFiles();
		    if (allContents != null) {
		        for (File file : allContents) {
		        	unlockFiles(file);
		        }
		    }
		    dir.setExecutable(true);
		    dir.setReadable(true);
		    dir.setWritable(true);
		    return true;
		
		}
		
		public static File getOrMkdirs(URI path) {
			return getOrMkdirs(path.getPath());
		}
		
		public static File getOrMkdirs(String path) {
			File file = new File(path);
			try {
				if(!file.exists()) {
					if(path.contains(".") && path.contains("/") ) {
						new File(path.substring(0, path.lastIndexOf("/"))).mkdirs();
						file.createNewFile();
					}else {
						file.mkdirs();
					}
					file.setReadable(true);
				}
			}catch(Exception e) {
				e.printStackTrace();
			}
			return file;
		}
		
		public static File getOrMkdirs(String path, boolean deleteOnExit) {
			File file =  getOrMkdirs(path);
			if(deleteOnExit)
				file.deleteOnExit();
			return file;
		}
		
		
		public static boolean fileExists(String path) {
			return new File(path).exists();
		}
		
	    public static String readFileAsString(File file){
	    	try {
	    		return new String(Files.readAllBytes(file.toPath()));
	    	}catch(Exception e) {
	    		return null;
	    	}
	    }
	    
	    public static void saveJSONtoFile(String jsonString, File file) {
	    	try {
	    		FileWriter writer = new FileWriter(file.getAbsolutePath());
	    		writer.write(jsonString);
	    		writer.flush();
	    		writer.close();
	    	}catch(Exception e) {
	    		e.printStackTrace();
	    	}
	    }
	    
	    public static File getResourceAsFile(String resourcePath) {
		    try {
		        InputStream in = ClassLoader.getSystemClassLoader().getResourceAsStream(resourcePath);
		        if (in == null) {
		            return null;
		        }

		        File tempFile = File.createTempFile(String.valueOf(in.hashCode()), ".tmp");
		        tempFile.deleteOnExit();

		        try (FileOutputStream out = new FileOutputStream(tempFile)) {
		            //copy stream
		            byte[] buffer = new byte[1024];
		            int bytesRead;
		            while ((bytesRead = in.read(buffer)) != -1) {
		                out.write(buffer, 0, bytesRead);
		            }
		        }
		        return tempFile;
		    } catch (IOException e) {
		        e.printStackTrace();
		        return null;
		    }
		}
	}
	
	public static class MathUtil{
		public static int random(int min, int max){
			return (int) ((Math.random() * ((max - min) + 1)) + min);
		}
		
		public static float random(float min, float max){
			return (float) ((Math.random() * ((max - min) + 1)) + min);
		}
		
	}
}
