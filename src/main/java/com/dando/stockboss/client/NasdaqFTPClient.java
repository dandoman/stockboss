package com.dando.stockboss.client;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import lombok.Cleanup;

public class NasdaqFTPClient {

	private FTPClient ftp = new FTPClient();
	
	public List<String> getAllCompanyTickers() {
		try {
			ftp.connect("ftp.nasdaqtrader.com");
			ftp.enterLocalPassiveMode();
			ftp.login("anonymous", "");
			ftp.changeWorkingDirectory("/symboldirectory");
			FTPFile[] listFiles = ftp.listFiles("nasdaqlisted.txt");
			long size = 0;
			for(FTPFile f : listFiles) {
				long remoteSize = f.getSize();
				if(remoteSize > size) {
					size = remoteSize;
				}
			}
			
			OutputStream output = new ByteArrayOutputStream((int)size); //dangerrrrr
			ftp.retrieveFile("nasdaqlisted.txt", output);
			return getTickerList(output);
			
		} catch (IOException e) {
			e.printStackTrace();
			return new ArrayList<>();
		} finally {
			try {
				ftp.logout();
				ftp.disconnect();
			} catch (IOException e) {
				//log it or wtv
			}
		}
	}

	private List<String> getTickerList(OutputStream output) {
		byte[] byteArray = ((ByteArrayOutputStream) output).toByteArray();
		List<String> tickers = new ArrayList<>();
		@Cleanup Scanner scanner = new Scanner(new ByteArrayInputStream(byteArray));
		while(scanner.hasNextLine()) {
			String line = scanner.nextLine();
			String [] parts = line.split("\\|");
			tickers.add(parts[0].trim());
		}
		return tickers;
	}
	
}
