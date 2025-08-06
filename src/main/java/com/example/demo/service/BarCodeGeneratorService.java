package com.example.demo.service;

import java.io.IOException;

import com.google.zxing.WriterException;

public interface BarCodeGeneratorService {
	
	public byte[] generateBarCode(String text) throws WriterException, IOException;
	
	public String readBarCode(byte[] imageBytes) throws IOException;
}
