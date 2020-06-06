package fudan.se.project.controller;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import fudan.se.project.service.FileService;
import fudan.se.project.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Table;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;

@SuppressWarnings("Duplicates")
@RestController
@Table(name = "file")
@JsonIgnoreProperties({"handler","hibernateLazyInitializer"})
@Validated
public class FileController {
    @Autowired
    private FileService fileService;

    @PostMapping("/upload_file")
    @ResponseBody
    public ResponseEntity<?> uploadFile(@Validated @RequestParam(value = "file") MultipartFile file, @Validated @RequestParam(value = "project_id") int projectId){
        int userId = Integer.parseInt((((org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername()));
        JSONObject message = new JSONObject();
        if (file.isEmpty() || !file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1).equals("pdf")){
            message.put("message","A PDF file is required");
            return new ResponseEntity<>(message.toJSONString(), HttpStatus.BAD_REQUEST);
        }
        float size = Float.parseFloat(String.valueOf(file.getSize())) / 1024;
        BigDecimal b = new BigDecimal(size);
        // 2表示2位 ROUND_HALF_UP表明四舍五入，
        size = b.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
        if (size > 1024){
            message.put("message","file is too large");
            return new ResponseEntity<>(message.toJSONString(),HttpStatus.BAD_REQUEST);
        }
        String result = fileService.uploadFile(userId,file,projectId);
        message.put("message",result);
        if (result.equals("success")){
            return new ResponseEntity<>(message.toJSONString(),HttpStatus.OK);
        }
        return new ResponseEntity<>(message.toJSONString(), HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/delete_file")
    @ResponseBody
    public ResponseEntity<?> deleteFile(@Validated @RequestParam(value = "file_id")int fileId){
        JSONObject message = new JSONObject();
        int userId = Integer.parseInt((((org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername()));
        String result = fileService.deleteFile(userId,fileId);
        message.put("message",result);
        if (result.equals("success")){
            return new ResponseEntity<>(message.toJSONString(),HttpStatus.OK);
        }
        return new ResponseEntity<>(message.toJSONString(), HttpStatus.BAD_REQUEST);

    }

    @GetMapping("/download_file")
    @ResponseBody
    public void downloadFile(HttpServletResponse response, @Validated @RequestParam(value = "file_id")int fileId){
        int userId = Integer.parseInt((((org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername()));
        fileService.downloadFile(response,userId,fileId);
    }
}
