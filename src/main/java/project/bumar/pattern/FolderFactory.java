package project.bumar.pattern;

import project.bumar.constant.FileUploaderConstants;

public class FolderFactory {

    public String getFolder(String typeOfService) {
        if (typeOfService == null) {
            return null;
        }
        if (typeOfService.contains("Business")) {
            return FileUploaderConstants.BUSINESS_FOLDER_NAME;
        }
        else if (typeOfService.contains("Service")) {
            return FileUploaderConstants.SERVICE_FOLDER_NAME;
        }
        else if (typeOfService.contains("News")) {
            return FileUploaderConstants.NEWS_FOLDER_NAME;
        }
        else if (typeOfService.contains("Product")) {
            return FileUploaderConstants.PRODUCT_FOLDER_NAME;
        }

        return null;
    }
}
