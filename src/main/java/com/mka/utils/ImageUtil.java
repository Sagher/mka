/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mka.utils;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import javax.imageio.ImageIO;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Sagher Mehmood
 */
@Component
public class ImageUtil {

    public static Logger log = Logger.getLogger(ImageUtil.class);

    public boolean processAndStoreImage(MultipartFile img, String filePath) {
        boolean processed = false;
        try {
            if (img != null && !img.isEmpty()) {
                InputStream in = new ByteArrayInputStream(img.getBytes());
                BufferedImage image = ImageIO.read(in);
                processed = writeImage(filePath, image);
                in.close();
            }
        } catch (Exception e) {
            log.error("Exception in writing image at:" + filePath + "\n" + e);
        }
        return processed;
    }

    private boolean writeImage(String filePath, BufferedImage img) {

        try {
            String dir = filePath.substring(0, filePath.lastIndexOf(File.separator));
            if (!new File(dir).exists()) {
                new File(dir).mkdirs();
            }
            File oldFile = new File(filePath);
            if (oldFile.exists()) {
                oldFile.delete();
            }

            File outputfile = new File(filePath);
            String type = filePath.substring(filePath.lastIndexOf(".") + 1, filePath.length());
            return ImageIO.write(img, type, outputfile);

        } catch (Exception e) {
            log.error("Exception in writing image at:" + filePath + ",", e);
        }
        return false;
    }

}
