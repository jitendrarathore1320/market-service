package com.advantal.utils;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;

import org.apache.commons.io.FilenameUtils;
import org.springframework.web.multipart.MultipartFile;

public class FileUploader {
	public static String uploadFile(MultipartFile mulfile, String path) {
		String filename = "";
		if (!mulfile.isEmpty()) {
			Long timestamp = System.currentTimeMillis();
			filename = mulfile.getOriginalFilename();
			String extension = FilenameUtils.getExtension(filename);
//			filename = timestamp.toString()+"_"+ filename.concat("."+extension);
//			filename = filename.concat("."+extension);
			try {
				byte[] bytes = mulfile.getBytes();
				BufferedOutputStream buffStream = new BufferedOutputStream(new FileOutputStream(path + filename, true));
				buffStream.write(bytes);
				buffStream.close();
				return path + filename;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return filename;
	}

}
