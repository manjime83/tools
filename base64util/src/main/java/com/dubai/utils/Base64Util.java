package com.dubai.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import org.apache.commons.codec.binary.Base64InputStream;
import org.apache.commons.codec.binary.Base64OutputStream;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;

public class Base64Util {

	public static void main(String[] args) throws IOException {
		if (args[0].equals("-e")) {
			FileOutputStream fos = new FileOutputStream(FilenameUtils.getBaseName(args[1]) + ".b64.zip");
			ZipOutputStream zos = new ZipOutputStream(fos, Charset.forName("UTF-8"));
			ZipFile zip = new ZipFile(args[1], Charset.forName("UTF-8"));

			Enumeration<? extends ZipEntry> entries = zip.entries();
			while (entries.hasMoreElements()) {
				ZipEntry inEntry = entries.nextElement();
				ZipEntry outEntry = new ZipEntry(inEntry.getName());
				zos.putNextEntry(outEntry);
				if (!inEntry.isDirectory()) {
					ByteArrayOutputStream b64 = new ByteArrayOutputStream();
					IOUtils.copy(zip.getInputStream(inEntry), new Base64OutputStream(b64));
					IOUtils.copy(new ByteArrayInputStream(b64.toByteArray()), zos);
					b64.close();
				}
				zos.closeEntry();
			}

			zip.close();
			zos.close();
			fos.close();
			System.out.println(args[1] + " encoded!");
		} else if (args[0].startsWith("-d")) {
			FileOutputStream fos = new FileOutputStream(
					FilenameUtils.getBaseName(FilenameUtils.getBaseName(args[1])) + ".zip");
			ZipOutputStream zos = new ZipOutputStream(fos, Charset.forName("UTF-8"));
			ZipFile zip = new ZipFile(args[1], Charset.forName("UTF-8"));

			Enumeration<? extends ZipEntry> entries = zip.entries();
			while (entries.hasMoreElements()) {
				ZipEntry inEntry = entries.nextElement();
				ZipEntry outEntry = new ZipEntry(inEntry.getName());
				zos.putNextEntry(outEntry);
				if (!inEntry.isDirectory()) {
					ByteArrayOutputStream b64 = new ByteArrayOutputStream();
					IOUtils.copy(new Base64InputStream(zip.getInputStream(inEntry)), b64);
					IOUtils.copy(new ByteArrayInputStream(b64.toByteArray()), zos);
					b64.close();
				}
				zos.closeEntry();
			}

			zip.close();
			zos.close();
			fos.close();
			System.out.println(args[1] + " decoded!");
		} else {
			System.out.println("usage: java -jar base64util.jar [-e|-d] <filename>");
		}

	}

}
