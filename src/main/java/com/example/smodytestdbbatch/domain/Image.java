package com.example.smodytestdbbatch.domain;

import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Image {

    private final MultipartFile multipartFile;

    private final ImageStrategy imageStrategy;

    private String url;

    public String getUrl() {
        if (url == null) {
            this.url = imageStrategy.extractUrl(multipartFile);
        }
        return this.url;
    }
}
