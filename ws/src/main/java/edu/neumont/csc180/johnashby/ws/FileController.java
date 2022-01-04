package edu.neumont.csc180.johnashby.ws;

import edu.neumont.csc180.johnashby.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

@RestController
public class FileController {
    @Autowired
    ImageService service;

    @GetMapping("/image/{name}")
    public ResponseEntity<byte[]> getImage(@RequestParam(required = false) String transform, @PathVariable("name") String name) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "image/png");
        File file = new File("images/" + name);
        if(!file.exists()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        byte [] img;
        if(!transform.isEmpty()){
            if(transform.equals("grayscale")){
                img = service.greyImage(name);
            }
            else if(transform.equals("rotate clockwise")){
                img = service.rotateImage(name);
            }
            else{
                img = service.originalImage(name);
            }
        }
        else{
            img = service.originalImage(name);
        }
        return new ResponseEntity<>(img, headers, HttpStatus.OK);
    }

    @PostMapping("/image")
    public ResponseEntity<String> createImage(@RequestParam("file") MultipartFile multipartFile) throws IOException {
        File file = new File("images/" + multipartFile.getOriginalFilename());
        if (file.exists() || multipartFile.isEmpty() || !Objects.equals(multipartFile.getContentType(), "image/png")) {
            return new ResponseEntity<>("Error: the name already exists",HttpStatus.BAD_REQUEST);
        }
        service.createImage(multipartFile);
        return new ResponseEntity<>(multipartFile.getOriginalFilename(),HttpStatus.OK);
    }

    @DeleteMapping("/image/{name}")
    public ResponseEntity<String> deleteImage(@PathVariable("name") String name){
        File file = new File("images/" + name);
        if(!file.exists()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        service.deleteImage(name);
        return new ResponseEntity<>("Successfully Deleted: " + name,HttpStatus.OK);
    }
}
