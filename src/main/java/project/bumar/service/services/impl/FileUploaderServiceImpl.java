package project.bumar.service.services.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import project.bumar.constant.ErrorConstants;
import project.bumar.exeption.DestroyFailException;
import project.bumar.exeption.UploadFailException;
import project.bumar.service.services.FileUploaderService;

import java.io.IOException;
import java.util.Map;

@Service
public class FileUploaderServiceImpl implements FileUploaderService {

    private final Cloudinary cloudinary;

    @Autowired
    public FileUploaderServiceImpl(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    @Override
    public String getUploadedFileUrl(String folderName, long fileName, MultipartFile file) {

        Map params = ObjectUtils.asMap(
                "public_id", folderName + "/" + fileName,
                "overwrite", true
        );

        Map uploadResult = null;
        try {
            uploadResult = cloudinary.uploader().upload(file.getBytes(), params);
        } catch (IOException e) {
            throw new UploadFailException(String.format(ErrorConstants.FILE_UPLOAD_FAILED, fileName, folderName));
        }

        return (String) uploadResult.get("url");
    }

    @Override
    public void getDestroyedFileUrl(String folderName, long fileName) {

        try {
            cloudinary.uploader().destroy(folderName + "/" + fileName, ObjectUtils.emptyMap());
        } catch (IOException e) {
            throw new DestroyFailException(String.format(ErrorConstants.FILE_DESTROY_FAILED, fileName, folderName));
        }
    }
}
