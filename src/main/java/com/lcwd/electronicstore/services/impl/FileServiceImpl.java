package com.lcwd.electronicstore.services.impl;

import com.lcwd.electronicstore.exception.BadApiRequestException;
import com.lcwd.electronicstore.services.FileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {

    private Logger logger= LoggerFactory.getLogger(FileServiceImpl.class);

    @Override
    public String uploadFile(MultipartFile file, String path) throws IOException {

        logger.info("Initiating service layer to upload image with file: {} & path : {}",file, path);
        String originalFilename = file.getOriginalFilename();
        logger.info("FileName : {}", originalFilename);

        String fileName = UUID.randomUUID().toString();
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String fileNameWithExtension = fileName + extension;
        String fullPathWithFileName = path + fileNameWithExtension;

        logger.info("Full image path : {}", fullPathWithFileName);
        if(extension.equalsIgnoreCase(".jpg") || extension.equalsIgnoreCase(".jpeg") || extension.equalsIgnoreCase(".png")){

            logger.info("Full extension : {}", extension);
            File folder = new File(path);
            if (!folder.exists()) {
                folder.mkdirs();
            }

            Files.copy(file.getInputStream(), Paths.get(fullPathWithFileName));
            logger.info("Completed service layer to upload image with file: {} & path : {}",file, path);
            return fileNameWithExtension;
        }else{
            throw new BadApiRequestException("File with this "+extension+" not allowed");
        }


    }

    @Override
    public InputStream getResource(String path, String filename) throws FileNotFoundException {
        logger.info("Initiating service layer to download image with filename: {} & path : {}",filename, path);
        String fullpath = path + File.separator + filename;
        InputStream inputStream=new FileInputStream(fullpath);
        logger.info("Completed service layer to download image with filename: {} & path : {}",filename, path);
        return inputStream;
    }
}
