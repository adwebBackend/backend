package fudan.se.project.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
public class FileService {
    public String saveFile(MultipartFile file){
        //文件后缀名
        String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
        //上传文件名
        String filename = UUID.randomUUID() + suffix;
        //服务器端保存的文件对象
        String saveDir = "/usr/local/images/";
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
        return serverFile.getAbsolutePath();
    }

//    public byte[] fileToByte(File img) throws Exception {
//        byte[] bytes = null;
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        try {
//            BufferedImage bi;
//            bi = ImageIO.read(img);
//            ImageIO.write(bi, "png", baos);
//            bytes = baos.toByteArray();
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            baos.close();
//        }
//        return bytes;
//    }
}
