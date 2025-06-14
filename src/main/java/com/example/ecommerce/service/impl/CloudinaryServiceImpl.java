package com.example.ecommerce.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.ecommerce.service.CloudinaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

@Service
public class CloudinaryServiceImpl implements CloudinaryService {

    @Autowired
    private Cloudinary cloudinary;

    @Override
    public Map<String, Object> uploadFile(MultipartFile file, String folderName) throws IOException {
        File convertedFile = convertToFile(file);
        Map<String, Object> result = cloudinary.uploader().upload(convertedFile, ObjectUtils.asMap(
                "folder", folderName
        ));
        convertedFile.delete();
        return result;
    }

    @Override
    public Map<String, Object> deleteFile(String publicId) throws IOException {
        return cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
    }

    @Override
    public Map<String, Object> updateFile(MultipartFile file, String oldPublicId, String folderName) throws IOException {
        deleteFile(oldPublicId);
        return uploadFile(file, folderName);
    }

    // Utility method to convert MutlipartFile to File
    private File convertToFile(MultipartFile multipartFile) throws IOException{
        File file = File.createTempFile("temp", multipartFile.getOriginalFilename());
        try (FileOutputStream fos = new FileOutputStream(file)){
            fos.write(multipartFile.getBytes());
        }
        return file;
    }
}
