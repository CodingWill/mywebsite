package com.eCommerce.service.impl;

import com.eCommerce.service.IFileService;
import com.eCommerce.util.FTPUtil;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;


@Service("iFileService")
public class FileServiceImpl implements IFileService {

    private Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);

    public String upload(MultipartFile file, String path) {
        String fileName = file.getOriginalFilename();
        // get file extension
        // eg: ***.jpg -> jpg
        String fileExtensionName = fileName.substring((fileName.lastIndexOf(".") + 1));
        // generate different file name
        String uploadFileName = UUID.randomUUID().toString() + "." + fileExtensionName;

        logger.info("Uploading, file name: {}, path: {}, upload file name: {}",
                    fileName, path, uploadFileName);

        File fileDir = new File(path);
        if (!fileDir.exists()) {
            fileDir.setWritable(true);
            fileDir.mkdirs();
        }
        File targetFile = new File(path, uploadFileName);

        try {
            file.transferTo(targetFile);
            // upload to FTP server
            FTPUtil.uploadFile(Lists.newArrayList(targetFile));

            // delete file in dir: upload
            targetFile.delete();
        } catch (IOException e) {
            logger.error("Error when uploading", e);
            return null;
        }
        return targetFile.getName();
    }

}
