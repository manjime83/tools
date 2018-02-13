package com.dubai.utils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import org.apache.commons.codec.binary.Base64InputStream;
import org.apache.commons.codec.binary.Base64OutputStream;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;

public class Base64Util {

	public static void main(String[] args) throws IOException {
		if (args[0].startsWith("-e")) {
			String name = FilenameUtils.getName(args[1]) + ".txt";
			InputStream input = new FileInputStream(args[1]);
			OutputStream output = new Base64OutputStream(new FileOutputStream(name));
			IOUtils.copy(input, output);
			output.close();
			input.close();
			System.out.println(args[1] + " encoded!");

			if (args[0].startsWith("-ez")) {
				FileOutputStream zip = new FileOutputStream(name + ".zip");
				ZipOutputStream zos = new ZipOutputStream(zip, Charset.forName("UTF-8"));
				ZipEntry zipEntry = new ZipEntry(name);
				try {
					zos.putNextEntry(zipEntry);
					IOUtils.copy(new FileInputStream(name), zos);
					zos.closeEntry();
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
				zos.close();
				zip.close();
				System.out.println(name + " compressed!");
			}
		} else if (args[0].startsWith("-d")) {
			String name;
			if (args[0].startsWith("-dz")) {
				ZipFile zip = new ZipFile(args[1], Charset.forName("UTF-8"));
				ZipEntry zipEntry = zip.entries().nextElement();
				name = zipEntry.getName();
				FileOutputStream fos = new FileOutputStream(name);
				IOUtils.copy(zip.getInputStream(zipEntry), fos);
				fos.close();
				zip.close();
				System.out.println(args[1] + " uncompressed!");
			} else {
				name = args[1];
			}

			InputStream input = new Base64InputStream(new FileInputStream(name));
			OutputStream output = new FileOutputStream(FilenameUtils.getBaseName(name));
			IOUtils.copy(input, output);
			output.close();
			input.close();
			System.out.println(name + " decoded!");
		} else {
			System.out.println("usage: java -jar base64util.jar [-e|-d] <filename>");
		}

	}

}
