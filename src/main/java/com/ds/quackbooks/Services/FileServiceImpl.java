package com.ds.quackbooks.services;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileServiceImpl implements FileService{

    @Override
    public String uploadImage(String path, MultipartFile image)  throws IOException{
        String originalFileName = image.getOriginalFilename();

        String randomId = UUID.randomUUID().toString();
        String fileName = randomId.concat(originalFileName.substring(originalFileName.lastIndexOf('.')));
        String filePath = path + File.pathSeparator + fileName;

        File folder = new File(path);
        if(!folder.exists())
            folder.mkdirs();

        Files.copy(image.getInputStream(), Paths.get(filePath));

        return fileName;
    }

}
