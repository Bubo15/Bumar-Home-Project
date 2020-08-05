package project.bumar.service.services;

import org.springframework.web.multipart.MultipartFile;

public interface FileUploaderService {

    String getUploadedFileUrl(String folderName, long fileName, MultipartFile file);

    void getDestroyedFileUrl(String folderName, long fileName);
}
