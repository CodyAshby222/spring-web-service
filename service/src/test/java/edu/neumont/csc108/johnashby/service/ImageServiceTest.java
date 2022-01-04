package edu.neumont.csc108.johnashby.service;

import org.junit.jupiter.api.Test;

import javax.imageio.IIOException;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class ImageServiceTest {

    @Test
    void testOriginalImage_happyPath() throws IOException {
        String basePath = "./src/test/java/edu/neumont/csc108/johnashby/service/results/";

        //Expected Img
        String expectedImg = "expected/logo.png";
        File expFile = new File(basePath + expectedImg);
        BufferedImage expImg = ImageIO.read(expFile);
        ByteArrayOutputStream expOutput = new ByteArrayOutputStream();
        ImageIO.write(expImg, "png", expOutput);
        byte[] expectedResult = expOutput.toByteArray();

        //Actual Img
        String actualImg = "actual/mask_logo.png";
        File file = new File(basePath + actualImg);
        BufferedImage img = ImageIO.read(file);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        ImageIO.write(img, "png", output);
        byte[] actualResult = output.toByteArray();

        assertArrayEquals(expectedResult, actualResult);
    }

    @Test
    void testOriginalImage_fileNotFound_Success(){
        assertThrows(IIOException.class, () -> {
            //Non existing img
            String basePath = "./src/test/java/edu/neumont/csc108/johnashby/service/results/";
            String actualImg = "actual/not_existing.png";
            File file = new File(basePath + actualImg);
            BufferedImage img = ImageIO.read(file);
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            ImageIO.write(img, "png", output);
            output.toByteArray();
        });
    }

    @Test
    void testGreyImage_happyPath() throws IOException {
        String basePath = "./src/test/java/edu/neumont/csc108/johnashby/service/results/";

        //Actual Grey Result
        String actualImg = "actual/mask_logo.png";
        File file = new File(basePath + actualImg);
        BufferedImage img = ImageIO.read(file);

        for(int x = 0; x < img.getWidth(); x++){
            for(int y = 0; y < img.getHeight(); y++){
                Color color = new Color(img.getRGB(x,y));
                int red = (int)(color.getRed() * 0.3);
                int green =  (int)(color.getGreen() * 0.6);
                int blue =  (int)(color.getBlue() * 0.1);
                int total = red + green + blue;
                Color gray = new Color(total, total, total);
                img.setRGB(x,y, gray.getRGB());
            }
        }

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        ImageIO.write(img, "png", output);
        byte[] actualResult = output.toByteArray();

        //Expected Grey Result
        String expectedImg = "expected/logo.png";
        File expFile = new File(basePath + expectedImg);
        BufferedImage expImg = ImageIO.read(expFile);

        for(int x = 0; x < expImg.getWidth(); x++){
            for(int y = 0; y < expImg.getHeight(); y++){
                Color color = new Color(expImg.getRGB(x,y));
                int red = (int)(color.getRed() * 0.3);
                int green =  (int)(color.getGreen() * 0.6);
                int blue =  (int)(color.getBlue() * 0.1);
                int total = red + green + blue;
                Color gray = new Color(total, total, total);
                expImg.setRGB(x,y, gray.getRGB());
            }
        }

        ByteArrayOutputStream expOutput = new ByteArrayOutputStream();
        ImageIO.write(expImg, "png", expOutput);
        byte[] expectedResult = expOutput.toByteArray();

        assertArrayEquals(expectedResult, actualResult);
    }

    @Test
    void testOriginalImage_ThrowsIIOException_Success(){
        assertThrows(IIOException.class, () -> {
            //Non existing img
            String basePath = "./src/test/java/edu/neumont/csc108/johnashby/service/results/";
            String actualImg = "actual/not_existing.png";
            File file = new File(basePath + actualImg);
            BufferedImage img = ImageIO.read(file);

            for(int x = 0; x < img.getWidth(); x++){
                for(int y = 0; y < img.getHeight(); y++){
                    Color color = new Color(img.getRGB(x,y));
                    int red = (int)(color.getRed() * 0.3);
                    int green =  (int)(color.getGreen() * 0.6);
                    int blue =  (int)(color.getBlue() * 0.1);
                    int total = red + green + blue;
                    Color gray = new Color(total, total, total);
                    img.setRGB(x,y, gray.getRGB());
                }
            }

            ByteArrayOutputStream output = new ByteArrayOutputStream();
            ImageIO.write(img, "png", output);
            output.toByteArray();
        });
    }

    @Test
    void testGreyImage_sameGreyImage_Failure() throws IOException {
        String basePath = "./src/test/java/edu/neumont/csc108/johnashby/service/results/";

        //Actual - Normal Image
        String actualImg = "actual/mask_logo.png";
        File file = new File(basePath + actualImg);
        BufferedImage img = ImageIO.read(file);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        ImageIO.write(img, "png", output);
        byte[] actualResult = output.toByteArray();

        //Expected - Grey Image
        String expectedImg = "expected/logo.png";
        File expFile = new File(basePath + expectedImg);
        BufferedImage expImg = ImageIO.read(expFile);

        for(int x = 0; x < expImg.getWidth(); x++){
            for(int y = 0; y < expImg.getHeight(); y++){
                Color color = new Color(expImg.getRGB(x,y));
                int red = (int)(color.getRed() * 0.3);
                int green =  (int)(color.getGreen() * 0.6);
                int blue =  (int)(color.getBlue() * 0.1);
                int total = red + green + blue;
                Color gray = new Color(total, total, total);
                expImg.setRGB(x,y, gray.getRGB());
            }
        }

        ByteArrayOutputStream expOutput = new ByteArrayOutputStream();
        ImageIO.write(expImg, "png", expOutput);
        byte[] expectedResult = expOutput.toByteArray();

        assertFalse(Arrays.equals(expectedResult, actualResult));
    }

    @Test
    void testRotateImage_happyPath() throws IOException {
        String basePath = "./src/test/java/edu/neumont/csc108/johnashby/service/results/";

        //Actual Rotated Image
        String actualImg = "actual/mask_logo.png";
        File file = new File(basePath + actualImg);
        BufferedImage img = ImageIO.read(file);

        double rads = Math.toRadians(90);
        double sin = Math.abs(Math.sin(rads));
        double cos = Math.abs(Math.cos(rads));
        int w = (int) Math.floor(img.getWidth() * cos + img.getHeight() * sin);
        int h = (int) Math.floor(img.getHeight() * cos + img.getWidth() * sin);
        BufferedImage rotatedImage = new BufferedImage(w, h, img.getType());
        AffineTransform at = new AffineTransform();
        at.translate(w / 2, h / 2);
        at.rotate(rads,0, 0);
        at.translate(-img.getWidth() / 2, -img.getHeight() / 2);
        AffineTransformOp rotateOp = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
        rotateOp.filter(img,rotatedImage);

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        ImageIO.write(rotatedImage, "png", output);
        byte[] actualResult = output.toByteArray();

        //Expected Rotated Image
        String expectedImg = "expected/logo.png";
        File expFile = new File(basePath + expectedImg);
        BufferedImage expImg = ImageIO.read(expFile);

        int wid = (int) Math.floor(expImg.getWidth() * cos + expImg.getHeight() * sin);
        int ht = (int) Math.floor(expImg.getHeight() * cos + expImg.getWidth() * sin);
        BufferedImage rotatedExpImage = new BufferedImage(wid, ht, expImg.getType());
        AffineTransform aTran = new AffineTransform();
        aTran.translate(wid / 2, ht / 2);
        aTran.rotate(rads,0, 0);
        aTran.translate(-expImg.getWidth() / 2, -expImg.getHeight() / 2);
        AffineTransformOp rotateExpOp = new AffineTransformOp(aTran, AffineTransformOp.TYPE_BILINEAR);
        rotateExpOp.filter(expImg,rotatedExpImage);

        ByteArrayOutputStream expOutput = new ByteArrayOutputStream();
        ImageIO.write(rotatedExpImage, "png", expOutput);
        byte[] expectedResult = expOutput.toByteArray();

        assertArrayEquals(expectedResult, actualResult);
    }

    @Test
    void testRotateImage_fileNotFound_Success(){
        assertThrows(IIOException.class, () -> {
            //Non existing img
            String basePath = "./src/test/java/edu/neumont/csc108/johnashby/service/results/";
            String actualImg = "actual/not_existing.png";
            File file = new File(basePath + actualImg);
            BufferedImage img = ImageIO.read(file);

            double rads = Math.toRadians(90);
            double sin = Math.abs(Math.sin(rads));
            double cos = Math.abs(Math.cos(rads));
            int w = (int) Math.floor(img.getWidth() * cos + img.getHeight() * sin);
            int h = (int) Math.floor(img.getHeight() * cos + img.getWidth() * sin);
            BufferedImage rotatedImage = new BufferedImage(w, h, img.getType());
            AffineTransform at = new AffineTransform();
            at.translate(w / 2, h / 2);
            at.rotate(rads,0, 0);
            at.translate(-img.getWidth() / 2, -img.getHeight() / 2);
            AffineTransformOp rotateOp = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
            rotateOp.filter(img,rotatedImage);

            ByteArrayOutputStream output = new ByteArrayOutputStream();
            ImageIO.write(rotatedImage, "png", output);
            output.toByteArray();
        });
    }

    @Test
    void testRotateImage_sameRotatedImage_Failure() throws IOException {
        String basePath = "./src/test/java/edu/neumont/csc108/johnashby/service/results/";

        //Actual - Normal Image
        String actualImg = "actual/mask_logo.png";
        File file = new File(basePath + actualImg);
        BufferedImage img = ImageIO.read(file);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        ImageIO.write(img, "png", output);
        byte[] actualResult = output.toByteArray();

        //Expected - Rotated Image
        String expectedImg = "expected/logo.png";
        File expFile = new File(basePath + expectedImg);
        BufferedImage expImg = ImageIO.read(expFile);

        double rads = Math.toRadians(90);
        double sin = Math.abs(Math.sin(rads));
        double cos = Math.abs(Math.cos(rads));
        int wid = (int) Math.floor(expImg.getWidth() * cos + expImg.getHeight() * sin);
        int ht = (int) Math.floor(expImg.getHeight() * cos + expImg.getWidth() * sin);
        BufferedImage rotatedExpImage = new BufferedImage(wid, ht, expImg.getType());
        AffineTransform aTran = new AffineTransform();
        aTran.translate(wid / 2, ht / 2);
        aTran.rotate(rads,0, 0);
        aTran.translate(-expImg.getWidth() / 2, -expImg.getHeight() / 2);
        AffineTransformOp rotateExpOp = new AffineTransformOp(aTran, AffineTransformOp.TYPE_BILINEAR);
        rotateExpOp.filter(expImg,rotatedExpImage);

        ByteArrayOutputStream expOutput = new ByteArrayOutputStream();
        ImageIO.write(rotatedExpImage, "png", expOutput);
        byte[] expectedResult = expOutput.toByteArray();

        assertFalse(Arrays.equals(expectedResult, actualResult));
    }

    @Test
    void testCreateImage_happyPath() {
        String basePath = "./src/test/java/edu/neumont/csc108/johnashby/service/results/";
        String newImg = "actual/new_img.png";
        File file = new File(basePath + newImg);

        //If file does not exist create new file
        assertFalse(file.exists());
    }

    @Test
    void testCreateImage_fileAlreadyExists_Success(){
        String basePath = "./src/test/java/edu/neumont/csc108/johnashby/service/results/";
        String existingImg = "actual/mask_logo.png";
        File file = new File(basePath + existingImg);

        //If file does not exist create new file
        assertTrue(file.exists());

    }

    @Test
    void testDeleteImage_happyPath() {
        String basePath = "./src/test/java/edu/neumont/csc108/johnashby/service/results/";
        String existingImg = "actual/mask_logo.png";
        File file = new File(basePath + existingImg);

        //If file does exist then delete file
        assertTrue(file.exists());
    }

    @Test
    void testDeleteImage_fileDoesNotExist_Success() {
        String basePath = "./src/test/java/edu/neumont/csc108/johnashby/service/results/";
        String existingImg = "actual/fake_img.png";
        File file = new File(basePath + existingImg);

        //If file does exist then delete file
        assertFalse(file.exists());
    }
}