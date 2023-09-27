package tech.qiuweihong.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import tech.qiuweihong.service.FileService;
import tech.qiuweihong.utils.CommonUtils;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Service
public class FileServiceImpl implements FileService {

    @Autowired
    private S3Client s3Client;

    public static final String bucketName = "java-ecommerce";
    @Override
    public String uploadFile(MultipartFile file) {
        //Original File name
        String fileName = file.getOriginalFilename();
        //Extension
        String suffixName = fileName.substring(fileName.lastIndexOf("."));
        //NewFile name
        String newFileName = CommonUtils.generateUUID() + suffixName;

        //Folder name
        LocalDateTime ldt = LocalDateTime.now();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String folderName = dtf.format(ldt);
        String objectName = folderName + "/" + newFileName;
        //Upload
        try {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .acl(ObjectCannedACL.PUBLIC_READ)
                    .key(objectName)
                    .build();
            RequestBody requestBody = RequestBody.fromInputStream(file.getInputStream(), file.getSize());
            s3Client.putObject(putObjectRequest, requestBody);
            // get url
            return s3Client.utilities().getUrl(builder -> builder.bucket(bucketName).key(objectName)).toExternalForm();

        } catch (IOException e) {
            log.error("Upload file error:{}",e);
            throw new RuntimeException();
        }

    }
}
