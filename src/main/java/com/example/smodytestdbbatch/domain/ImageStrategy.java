package com.example.smodytestdbbatch.domain;

import org.springframework.web.multipart.MultipartFile;

public interface ImageStrategy {

    String extractUrl(MultipartFile image);
}
