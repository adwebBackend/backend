package fudan.se.project.service;

import fudan.se.project.domain.Project;
import fudan.se.project.domain.Upload;
import fudan.se.project.domain.User;
import fudan.se.project.repository.FileRepository;
import fudan.se.project.repository.ProjectRepository;
import fudan.se.project.repository.UploadRepository;
import fudan.se.project.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import javax.xml.transform.sax.SAXResult;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URLEncoder;
import java.util.Date;
import java.util.UUID;

@Service
public class FileService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private FileRepository fileRepository;
    @Autowired
    private UploadRepository uploadRepository;

    public String saveFile(MultipartFile file){
        //文件后缀名
        String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
        //上传文件名
        String filename = UUID.randomUUID() + suffix;
        //服务器端保存的文件对象
        String saveDir;
        if (suffix.equals(".pdf")){
            saveDir = "/var/www/html/files/";
//            saveDir = "D:\\Documents\\AD web\\pj\\files\\";
        }
        else {
            saveDir = "/var/www/html/images/";
//            saveDir = "D:\\Documents\\AD web\\pj\\courseImages\\";
        }
        File serverFile = new File(saveDir + filename);

        if(!serverFile.exists()) {
            //先得到文件的上级目录，并创建上级目录，在创建文件
            serverFile.getParentFile().mkdir();
            try {
                //创建文件
                serverFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //将上传的文件写入到服务器端文件内
        try {
            file.transferTo(serverFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (suffix.equals(".pdf")){
            return "files/" + filename;
        }
        return "images/" + filename;
//        return saveDir + filename;
    }

    public String uploadFile(int userId, MultipartFile file, int projectId){
        User user = userRepository.findByUserId(userId);
        if (user != null){
            Project project = projectRepository.findByProjectId(projectId);
            if (project == null){
                return "project not found";
            }
            String path = saveFile(file);
            String name = file.getOriginalFilename().substring(0,file.getOriginalFilename().lastIndexOf("."));
            Date uploadTime = new Date();
            fudan.se.project.domain.File file1 = new fudan.se.project.domain.File(name,path,uploadTime);
            fileRepository.save(file1);
            Upload upload = new Upload(projectId,userId,file1.getFileId());
            uploadRepository.save(upload);
            return "success";
        }
        return "failure";
    }

    @Transactional
    public String deleteFile(int userId, int fileId){
        User user = userRepository.findByUserId(userId);
        if (user != null){
            Upload upload = uploadRepository.findByFileIdAndUserId(fileId,userId);
            if (upload == null){
                return "you have not uploaded this file";
            }
            uploadRepository.delete(upload);
            fileRepository.deleteByFileId(fileId);
            return "success";
        }
        return "failure";
    }

    public void downloadFile(HttpServletResponse response,int userId,int fileId){
        User user = userRepository.findByUserId(userId);
        if (user != null){
            fudan.se.project.domain.File file = fileRepository.findByFileId(fileId);
            if (file == null){
                String message = "file not found";
                response.setHeader("Content-Disposition", "attachment;message=" + message);
                return;
            }

            String path = file.getPath();
            String filepath = "/var/www/html/"+path.replace("\\", "/");
            File file1 = new File(filepath);
            String suffix = path.substring(file.getPath().lastIndexOf("."));
            String fileName = file.getFilename() + suffix;
            try {
                response.setHeader("Content-Disposition", "attachment;fileName=" + URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20"));
            }catch (UnsupportedEncodingException e){
                e.printStackTrace();
            }

            byte[] buff = new byte[1024];
            BufferedInputStream bis = null;
            ServletOutputStream os;
            try {
                os = response.getOutputStream();
                bis = new BufferedInputStream(new FileInputStream(file1));
                int i = bis.read(buff);
                while (i != -1) {
                    os.write(buff, 0, buff.length);
                    os.flush();
                    i = bis.read(buff);
                }
                os.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (bis != null) {
                    try {
                        bis.close();
                    } catch (IOException e) {
//                        System.out.println(e.getMessage());
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
