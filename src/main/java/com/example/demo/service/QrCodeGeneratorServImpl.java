package com.example.demo.service;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.Result;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeWriter;

@Service("qrcodeserv")
public class QrCodeGeneratorServImpl implements QrCodeGeneratorService {

	@Value("${qrcode-output-location}")
	private String qr_location;
	
	@Override
	public byte[] generateQRCode(String text) throws WriterException, IOException {
		 
		QRCodeWriter qrCodeWriter = new QRCodeWriter();
		BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, 200, 200);
		BufferedImage bufferedImage = new BufferedImage(200, 200, BufferedImage.TYPE_INT_RGB);
		
		bufferedImage.createGraphics().fillRect(0, 0, 200, 200);
		
		Graphics2D graphics = (Graphics2D) bufferedImage.getGraphics();
		graphics.setColor(Color.BLACK);
		
		for(int i=0;i<200;i++) {
			for(int j=0;j<200;j++) {
				if(bitMatrix.get(i, j)) {
					graphics.fillRect(i, j, 1, 1);
				}
			}
		}
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageIO.write((RenderedImage) bufferedImage, "png", baos);
		
		byte[] byteArray = baos.toByteArray();
		
		  // Get resources path (works during development)
	    Path resourceDir = Paths.get("src", "main", "resources", "qrcodes");
	    Files.createDirectories(resourceDir); // Ensure the directory exists

	    // Save file with timestamp to avoid overwrite
	    String fileName = text.replaceAll("\\W+", "_") + "_" + System.currentTimeMillis() + ".png";
	    Path outputPath = resourceDir.resolve(fileName);
	    	
		Files.write(outputPath,byteArray);
		
		return byteArray;
		
		/* SECOND WAY  */
//		QRCodeWriter qrCodeWriter = new QRCodeWriter();
//        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, 200, 200);
//
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", baos);
//        return baos.toByteArray();
	}

	@Override
	public String readQRCode(byte[] imageBytes) throws IOException {
		ByteArrayInputStream bais = new ByteArrayInputStream(imageBytes);
		
		BufferedImage bufferedImage = ImageIO.read(bais);
		BufferedImageLuminanceSource source = new BufferedImageLuminanceSource(bufferedImage);
		
		HybridBinarizer binizer = new HybridBinarizer(source);
		
		BinaryBitmap bitmap = new BinaryBitmap(binizer);
		
		MultiFormatReader reader = new MultiFormatReader();
		
		try {
			Result result = reader.decode(bitmap);
			return result.getText();
		}
		catch(Exception e) {
			return "Error reading QR code";
		}
		 
	}

}
