package edu.neumont.csc180.johnashby.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

@Service
public class ImageService {

    public byte[] originalImage(String name) throws IOException {
        File file = new File("images/" + name);
        BufferedImage img = ImageIO.read(file);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        ImageIO.write(img, "png", output);
        return output.toByteArray();
    }

    public byte[] greyImage(String name) throws IOException {
        File file = new File("images/" + name);
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
        return output.toByteArray();
    }

    public byte[] rotateImage(String name) throws IOException {
        File file = new File("images/" + name);
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
        return output.toByteArray();
    }

    public void createImage(MultipartFile img) throws IOException {
        Path path = Paths.get("images/");
        if(!Files.exists(path)){
            Files.createDirectory(path);
        }
        Files.copy(img.getInputStream(),path.resolve(Objects.requireNonNull(img.getOriginalFilename())));
    }

    public void deleteImage(String name){
        File file = new File("images/" + name);
        if(file.exists()){
            file.delete();
        }
    }
}
