package com.example.scenchive.domain.board.service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.example.scenchive.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor    // final 멤버변수가 있으면 생성자 항목에 포함시킴
@Component
@Service
public class S3Uploader {

    private final AmazonS3Client amazonS3Client; //AmazonS3Client: S3 전송객체를 만들 때 필요한 클래스

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    // MultipartFile을 전달받아 File로 전환한 후 S3에 업로드
    public String upload(MultipartFile multipartFile, String dirName) throws IOException {
        File uploadFile = convert(multipartFile)
                .orElseThrow(() -> new IllegalArgumentException("MultipartFile -> File 전환 실패"));
        return upload(uploadFile, dirName);
    }

    private String upload(File uploadFile, String dirName) {
        String fileName = dirName + "/" + uploadFile.getName();
        String uploadImageUrl = putS3(uploadFile, fileName);

        removeNewFile(uploadFile);  // 로컬에 생성된 File 삭제 (MultipartFile -> File 전환 하며 로컬에 파일 생성됨)

        return uploadImageUrl;      // 업로드된 파일의 S3 URL 주소 반환
    }

    public String upload(MultipartFile multipartFile, String dirName, String name) throws IOException { //name 안에 향수, 브랜드 이름 입력
        String imgName = name.replaceAll("[^\\w]", "");
        String fileName = dirName + "/" + imgName;
        File uploadFile = convert(multipartFile)
                .orElseThrow(() -> new IllegalArgumentException("MultipartFile -> File 전환 실패"));
        String uploadImageUrl = putS3(uploadFile, fileName);
        removeNewFile(uploadFile);
        return uploadImageUrl;
    }

    //S3 버킷에 이미지 업로드
    private String putS3(File uploadFile, String fileName) {
        amazonS3Client.putObject(
                new PutObjectRequest(bucket, fileName, uploadFile)
                        .withCannedAcl(CannedAccessControlList.PublicRead)	// PublicRead 권한으로 업로드 됨
        );
        //return amazonS3Client.getUrl(bucket, fileName).toString();
        return "https://s3." + amazonS3Client.getRegionName() + ".amazonaws.com/" + bucket + "/" + fileName;
    }

    //로컬에 있는 이미지 삭제
    private void removeNewFile(File targetFile) {
        if(targetFile.delete()) {
            log.info("파일이 삭제되었습니다.");
        }else {
            log.info("파일이 삭제되지 못했습니다.");
        }
    }

    //변환
    private Optional<File> convert(MultipartFile file) throws  IOException {
        String now=LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        File convertFile = new File(now + "." + file.getOriginalFilename()); //파일 변환
        if(convertFile.createNewFile()) {
            try (FileOutputStream fos = new FileOutputStream(convertFile)) {
                fos.write(file.getBytes());
            }
            return Optional.of(convertFile);
        }
        return Optional.empty();
    }

    public void fileDelete(String fileUrl){
        try{
            // fileUrl에서 S3의 파일 경로만 추출
            String fileName = extractFileNameFromUrl(fileUrl);

            amazonS3Client.deleteObject(this.bucket, fileName);
        }
        catch(AmazonServiceException e){
            System.err.println(e.getErrorMessage());
        }
    }

    // URL에서 파일명만 추출하는 메서드
    private String extractFileNameFromUrl(String fileUrl) {
        // 업로드한 URL 형식: https://s3.[region].amazonaws.com/[bucket]/[fileName]
        try {
            URI uri = new URI(fileUrl);
            String path = uri.getPath(); // "/[bucket]/[fileName]" 형태

            // 버킷 이름 제거하고 파일명만 반환
            return path.substring(path.indexOf(this.bucket) + this.bucket.length() + 1);
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("Invalid file URL format: " + fileUrl);
        }
    }

}