package com.negongal.hummingbird.infra.awsS3;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import java.io.IOException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class S3Uploader {

    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public String saveFile(MultipartFile multipartFile) throws IOException {
        String filename = createFilename(multipartFile.getOriginalFilename());

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(multipartFile.getSize());
        metadata.setContentType(multipartFile.getContentType());

        try {
            amazonS3.putObject(bucket, filename, multipartFile.getInputStream(), metadata);
        } catch (IOException e) {
            throw new RuntimeException("이미지 업로드에 실패했습니다.");
        }

        return amazonS3.getUrl(bucket, filename).toString();
    }

    public String saveFileInFolder(MultipartFile multipartFile, String folder) throws IOException{
        String filename = folder + createFilename(multipartFile.getOriginalFilename());

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(multipartFile.getSize());
        metadata.setContentType(multipartFile.getContentType());

        try {
            amazonS3.putObject(bucket, filename, multipartFile.getInputStream(), metadata);
        } catch (IOException e) {
            throw new RuntimeException("이미지 업로드에 실패했습니다.");
        }

        return amazonS3.getUrl(bucket, filename).toString();
    }

    public void deleteFile(String filename) throws IOException {
        try {
            amazonS3.deleteObject(bucket, filename);
        } catch (SdkClientException e) {
            throw new RuntimeException("이미지 삭제에 실패했습니다");
        }
    }

    public String createFilename(String originalFilename) {
        return UUID.randomUUID() + "_" + originalFilename;
    }

}
