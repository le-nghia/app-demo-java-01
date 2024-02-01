package com.ln.training.app.common.util;

import com.ln.training.app.common.constant.Constants;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.util.Date;

public class FileHelper {
    /**
     * Save new file in server storage
     * @param parentFolderPath
     * @param files
     * @return
     * @throws IOException
     */
    public static String addNewFile(String parentFolderPath, MultipartFile[] files) throws IOException{
        return editFile(parentFolderPath,files,null);
    }

    /**
     *
     * @param parentFolderPath
     * @param files
     * @param oldFilepath
     * @return
     * @throws IOException
     */
    public static String editFile(String parentFolderPath, MultipartFile[] files, String oldFilepath) throws IOException{

        String randomStr = new BigInteger(130, new SecureRandom()).toString(32).substring(0,6);
        String newFileName = CommonUtil.cvDateToString(new Date(), Constants.DATE_FORMAT_FOR_FILE_NAME) + Constants.COMMON_HYPHEN + randomStr;
        String rootFolderPath = System.getProperty(Constants.PROP_KEY_FOLDER);

        String originalFileName = files[0].getOriginalFilename();
        String fileExtension = originalFileName.substring(originalFileName.lastIndexOf('.'), originalFileName.length());
        String assetPath = parentFolderPath + newFileName + fileExtension;
        String fullAssetPath = rootFolderPath + assetPath;

        // create Folder to save video
        File parentFolder = new File(rootFolderPath + parentFolderPath);
        if (!parentFolder.exists()){
            parentFolder.mkdirs();
        }

        if (StringUtils.isNotEmpty(oldFilepath)){
            // Remove old file if it existed
            deleteFile(rootFolderPath, oldFilepath);
        }
        byte[] fileInByte = files[0].getBytes();
        Path path = Paths.get(fullAssetPath);
        Files.write(path,fileInByte);
        return assetPath;
    }

    /**
     * Delete file
     * @param fileName
     * @throws IOException
     */
    public static void deleteFile(String fileName) throws IOException{
        String rootFolderPath = System.getProperty(Constants.PROP_KEY_FOLDER);
        File deleteFile = new File(rootFolderPath + fileName);
        if (deleteFile.exists()){
            deleteFile.delete();
        }
    }

    /**
     * Delete file
     * @param parentFolder
     * @param fileName
     * @throws IOException
     */
    public static void deleteFile(String parentFolder, String fileName) throws IOException {
        File deleteFile = new File(parentFolder + fileName);
        if (deleteFile.exists()){
            deleteFile.delete();
        }
    }

}
