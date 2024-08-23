package vn.hoidanit.jobhunter.controller;

import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vn.hoidanit.jobhunter.domain.dto.file.ResUploadFileDTO;
import vn.hoidanit.jobhunter.service.file.FileService;
import vn.hoidanit.jobhunter.util.annotation.ApiMessage;
import vn.hoidanit.jobhunter.util.error.UploadException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("api/v1")
public class FileController {
    private final FileService fileService;
    @Value("${upload-file.base-uri}")
    private String baseURI;
    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping("/files")
    @ApiMessage("Upload single file")
    public ResponseEntity<ResUploadFileDTO> uploadFile(@RequestParam(name="file", required = false)MultipartFile file, @RequestParam("folder") String folder) throws URISyntaxException, IOException, UploadException {

        // validate
        if (file==null || file.isEmpty()){
            throw  new UploadException("File is empty. Plese upload a file");
        }
        String fileName = file.getOriginalFilename();
        List<String> allowedExtensions = Arrays.asList("pdf", "jpg", "jpeg", "png", "doc", "docx");
        boolean isValid = allowedExtensions.stream().anyMatch(item -> {
            assert fileName != null;
            return fileName.toLowerCase().endsWith(item);
        });

        if(!isValid){
            throw  new UploadException("Invalid file extension, only allows " + allowedExtensions.toString());
        }

        // create directory if not exists
            fileService.handleCreateDirectory(baseURI+ folder.trim().replaceAll(",$", ""));
        // create file
           String uploadFile = fileService.handleCreateFile(file,folder.trim().replaceAll(",$", ""));
        ResUploadFileDTO res = new ResUploadFileDTO(uploadFile, Instant.now());
        return ResponseEntity.ok().body(res);
    }

    @GetMapping("/files")
    @ApiMessage("Download a file")
    public ResponseEntity<Resource> downloadFile(@RequestParam(name = "fileName", required = false) String fileName,
                                                 @RequestParam(name = "folder", required = false) String folder ) throws UploadException, URISyntaxException, FileNotFoundException {
        if(fileName == null || folder == null){
            throw  new UploadException("Missing required params :(fileName or folder)");
        }
        //check file exist and not a directory
        long fileLength = fileService.getFileLength(fileName,folder);
        if(fileLength == 0){
            throw new UploadException("File with name = "+ fileName +" not found");
        }
        //download
        InputStreamResource resource = fileService.getResource(fileName,folder);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,"attachment; fileName=\"" + fileName+ "\"")
                .contentLength(fileLength).contentType(MediaType.APPLICATION_OCTET_STREAM).body(resource);
    }
}
