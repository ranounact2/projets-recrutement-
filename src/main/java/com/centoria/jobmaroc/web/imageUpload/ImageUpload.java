package com.centoria.jobmaroc.web.imageUpload;

import jakarta.servlet.http.Part;
import lombok.extern.slf4j.Slf4j;

import javax.mail.MessagingException;
import java.io.*;
import java.nio.file.Files;
import java.util.Date;

@Slf4j
public class ImageUpload implements Serializable {

    private static final long serialVersionUID = 1L;
    public static final int BUFFER_SIZE = 2048;

    private String message;
    private String imageName;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


	public String uploadFile(Part file, String path) throws IOException, MessagingException {
		InputStream inputStream = null;
		OutputStream outputStream = null;

		boolean fileSuccess = false;

		ImageValidator img_valider = new ImageValidator();
		Date date = new Date();

		if (file.getSize() > 0) {
			String fileName = Utils.getFileNameFromPart((jakarta.servlet.http.Part) file);

			if (img_valider.validate(fileName)) {
				try {
					String ext = fileName.toString();
					String[] get_ext = ext.split("\\.");
					imageName = "IMG-" + date.getTime() + "." + get_ext[1];
					File outputFile = new File(path + File.separator + imageName);
					inputStream = file.getInputStream();
					outputStream = Files.newOutputStream(outputFile.toPath());

					byte[] buffer = new byte[BUFFER_SIZE];
					int bytesRead = 0;
					while ((bytesRead = inputStream.read(buffer)) != -1) {
						outputStream.write(buffer, 0, bytesRead);

					}
					if (outputStream != null) {
						outputStream.close();
					}
					inputStream.close();
					fileSuccess = true;
				}
				catch(Exception e) {
					log.error("Error uploading file", e);
				}
			}
		}

		if (fileSuccess) {
			log.info("File uploaded to : {}", path);
		} else {
			log.info("Please, select an image!");
		}
		return null;
	}

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

}