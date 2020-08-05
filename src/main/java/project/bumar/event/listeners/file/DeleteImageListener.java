package project.bumar.event.listeners.file;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import project.bumar.event.events.file.DeleteImageEvent;
import project.bumar.pattern.FolderFactory;
import project.bumar.service.services.FileUploaderService;

@Component
public class DeleteImageListener {

    private final FileUploaderService fileUploaderService;
    private final FolderFactory folderFactory;

    @Autowired
    public DeleteImageListener(FileUploaderService fileUploaderService, FolderFactory folderFactory) {
        this.fileUploaderService = fileUploaderService;
        this.folderFactory = folderFactory;
    }

    @EventListener(DeleteImageEvent.class)
    public void deleteImage(DeleteImageEvent deleteImageEvent) {
        String FOLDER_NAME = this.folderFactory.getFolder(deleteImageEvent.getSource().getClass().getSimpleName());
        this.fileUploaderService.getDestroyedFileUrl(FOLDER_NAME, deleteImageEvent.getId());
    }
}
