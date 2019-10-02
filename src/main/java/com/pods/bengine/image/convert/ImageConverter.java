package com.pods.bengine.image.convert;

import com.pods.bengine.image.ImageFormats;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

@Service
public class ImageConverter {

    public ByteBuffer convertToJpg(byte[] img) throws IOException {
        Assert.notNull(img, "Image is not specified");

        BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(img));
        BufferedImage newBufferedImage =
                new BufferedImage(bufferedImage.getWidth(), bufferedImage.getHeight(), BufferedImage.TYPE_INT_RGB);
        newBufferedImage.createGraphics().drawImage(bufferedImage, 0, 0, Color.WHITE, null);

        ByteArrayOutputStream jpgImageOutputStream = new ByteArrayOutputStream();
        ImageIO.write(newBufferedImage, ImageFormats.JPG.asFileExtension(), jpgImageOutputStream);
        return ByteBuffer.wrap(jpgImageOutputStream.toByteArray());
    }
}