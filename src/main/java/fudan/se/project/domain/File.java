package fudan.se.project.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity                     //实体类的注解，必须注明
@Table(name = "file")      //指定对应的数据库表
@JsonIgnoreProperties({"handler","hibernateLazyInitializer"})

public class File implements Serializable {

    private static final long serialVersionUID = 7955954601963951487L;

    @Id
    @Column(name = "fileId")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int fileId;

    @Column(name = "fileName")
    private String filename;

    @Column(name = "path")
    private String path;

    @Column(name = "uploadTime")
    private Date uploadTime;

    public int getFileId() {
        return fileId;
    }

    public void setFileId(int fileId) {
        this.fileId = fileId;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Date getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(Date uploadTime) {
        this.uploadTime = uploadTime;
    }

    public File(String filename, String path, Date uploadTime) {
        this.filename = filename;
        this.path = path;
        this.uploadTime = uploadTime;
    }

    public File() {
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}
