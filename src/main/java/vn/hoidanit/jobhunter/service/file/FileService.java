package vn.hoidanit.jobhunter.service.file;

import org.springframework.core.io.InputStreamResource;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;

public interface FileService {
     void handleCreateDirectory(String folder) throws URISyntaxException;
     String handleCreateFile(MultipartFile file, String folder) throws IOException, URISyntaxException;

     long getFileLength(String fileName, String folder) throws URISyntaxException;
     InputStreamResource getResource(String fileName, String folder) throws FileNotFoundException, URISyntaxException;

}
