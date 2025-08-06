package com.example.demo.service;

import java.io.IOException;

import com.google.zxing.WriterException;

public interface QrCodeGeneratorService {
	
	public byte[] generateQRCode(String text) throws WriterException, IOException;
	
	public String readQRCode(byte[] imageBytes) throws IOException;
}
