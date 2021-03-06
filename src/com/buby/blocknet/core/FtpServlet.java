package com.buby.blocknet.core;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

public abstract class FtpServlet {
	protected BlockNetCore bNetCore;
	public FtpServlet(BlockNetCore bNetCore) {
		this.bNetCore = bNetCore;
	}
	
	public static void downloadDirectory(FTPClient ftpClient, String parentDir,
	        String currentDir, String saveDir) throws IOException {
	    String dirToList = parentDir;
	    if (!currentDir.equals("")) {
	        dirToList += "/" + currentDir;
	    }
	 
	    FTPFile[] subFiles = ftpClient.listFiles(dirToList);
	 
	    if (subFiles != null && subFiles.length > 0) {
	        for (FTPFile aFile : subFiles) {
	            String currentFileName = aFile.getName();
	            if (currentFileName.equals(".") || currentFileName.equals("..")) {
	                continue;
	            }
	            String filePath = parentDir + "/" + currentDir + "/"
	                    + currentFileName;
	            if (currentDir.equals("")) {
	                filePath = parentDir + "/" + currentFileName;
	            }
	 
	            String newDirPath = saveDir + parentDir + File.separator
	                    + currentDir + File.separator + currentFileName;
	            if (currentDir.equals("")) {
	                newDirPath = saveDir + parentDir + File.separator
	                          + currentFileName;
	            }
	 
	            if (aFile.isDirectory()) {
	                File newDir = new File(newDirPath);
	                newDir.mkdirs();
	                downloadDirectory(ftpClient, dirToList, currentFileName,
	                        saveDir);
	            } else {
	                downloadSingleFile(ftpClient, filePath,
	                        newDirPath);
	            }
	        }
	    }
	}

	
	public static boolean downloadSingleFile(FTPClient ftpClient,
	        String remoteFilePath, String savePath) throws IOException {
	    File downloadFile = new File(savePath);
	     
	    File parentDir = downloadFile.getParentFile();
	    if (!parentDir.exists()) {
	        parentDir.mkdir();
	    }
	         
	    OutputStream outputStream = new BufferedOutputStream(
	            new FileOutputStream(downloadFile));
	    try {
	        ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
	        return ftpClient.retrieveFile(remoteFilePath, outputStream);
	    } catch (IOException ex) {
	        throw ex;
	    } finally {
	        if (outputStream != null) {
	            outputStream.close();
	        }
	    }
	}
}
