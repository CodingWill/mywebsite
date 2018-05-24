package com.eCommerce.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * Created by chao on 5/24/18.
 */
public interface IFileService {

    String upload(MultipartFile file, String path);
}
