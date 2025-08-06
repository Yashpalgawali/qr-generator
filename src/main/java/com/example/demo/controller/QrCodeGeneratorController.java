package com.example.demo.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.service.QrCodeGeneratorService;
import com.google.zxing.WriterException;

@RestController
public class QrCodeGeneratorController {


	private final QrCodeGeneratorService qrcodeserv;

	public QrCodeGeneratorController(  QrCodeGeneratorService qrcodeserv) {
		super();
	 
		this.qrcodeserv = qrcodeserv;
	}

	@GetMapping("/generateqr/{message}")
	public ResponseEntity<byte[]> generateQR(@PathVariable String message) throws WriterException, IOException {
		
		byte[] qrCode = qrcodeserv.generateQRCode(message);
		
		HttpHeaders headers = new HttpHeaders();
		
		headers.setContentType(MediaType.IMAGE_PNG);
		return ResponseEntity.ok().headers(headers).body(qrCode);
	}
}
