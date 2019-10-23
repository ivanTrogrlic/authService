package com.example.springsocial.service;

import com.example.springsocial.exception.ImageDownloadFailedException;
import com.example.springsocial.service.impl.ImagesServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.util.ReflectionTestUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.*;
import java.net.URL;
import java.util.Arrays;
import java.util.Scanner;
import java.util.Stack;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertThrows;

class ImagesServiceTest {

    private final ImagesService imagesService = new ImagesServiceImpl();

    @Test
    void fetchImageFromValidURL() throws IOException, ExecutionException, InterruptedException {
        URL url = new ClassPathResource("data/fullsize_original.jpeg").getURL();
        BufferedImage image = ImageIO.read(url);
        BufferedImage fetchedImage = imagesService.fetchImageByUrl(url.toString()).get();
        assert Arrays.equals(getBinaryFromImage(image), getBinaryFromImage(fetchedImage));
    }

    @Test
    void throwsExceptionWhenInvalidImageURL() throws InterruptedException {
        assertThrows(ImageDownloadFailedException.class, () ->  {
           try {
               imagesService.fetchImageByUrl("malformed url").get();
           } catch (ExecutionException e) {
               if (e.getCause() instanceof ImageDownloadFailedException) {
                   throw new ImageDownloadFailedException(e.getMessage());
               }
           }
        });
    }

    // Needed to save to filesystem, otherwise results don't match
    @Test
    void compressImage() throws IOException {
        ReflectionTestUtils.setField(imagesService, "scale", 0.1f);
        BufferedImage image = imagesService.downsize(ImageIO.read(new ClassPathResource("data/fullsize_original.jpeg").getURL()));
        ImageIO.write(image, "jpeg", new ClassPathResource("data/downsized_0.1_result.jpeg").getFile());
        BufferedImage target = ImageIO.read(new ClassPathResource("data/downsized_0.1.jpeg").getURL());
        BufferedImage result = ImageIO.read(new ClassPathResource("data/downsized_0.1_result.jpeg").getURL());
        assert Arrays.equals(getBinaryFromImage(target), getBinaryFromImage(result));
    }

    @Test
    void base64ImageEncoding() throws IOException {
        BufferedImage image = ImageIO.read(new ClassPathResource("data/fullsize_original.jpeg").getURL());
        String target = new Scanner(new ClassPathResource("data/base64OriginalEncoded.dat").getFile()).useDelimiter("\\Z").next();
        String result = imagesService.base64Encode(image);
        System.out.println("Target: " + target);
        System.out.println("Result: " + result);
        assert result.equals(target);
    }

    @Test
    void makeBase64Encoded() throws IOException {
        BufferedImage image = ImageIO.read(new ClassPathResource("data/fullsize_original.jpeg").getURL());
        File targetFile = new ClassPathResource("data/base64OriginalEncoded.dat").getFile();
        BufferedWriter out = new BufferedWriter(new FileWriter(targetFile));
        String result = imagesService.base64Encode(image);
        out.write(result);
        out.close();
        System.out.println(result);
    }

    @Test
    void makeCompressedTestcase() throws IOException {
        ReflectionTestUtils.setField(imagesService, "scale", 0.1f);
        BufferedImage image = imagesService.downsize(ImageIO.read(new ClassPathResource("data/fullsize_original.jpeg").getURL()));
        ImageIO.write(image, "jpeg", new File("/home/bono/Documents/Job/Excalibur/Research/Social login/authService/spring-social/src/test/resources/data/downsized_0.1_2.jpeg"));
    }

    private byte[] getBinaryFromImage(BufferedImage image) {
        return ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
    }

}
