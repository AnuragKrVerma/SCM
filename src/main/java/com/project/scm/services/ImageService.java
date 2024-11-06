package com.project.scm.services;

import org.springframework.web.multipart.MultipartFile;

public interface ImageService {
    String uploadImage(MultipartFile profileImage, String fileName);

    String getUrlFromPublicId(String publicId);
}
