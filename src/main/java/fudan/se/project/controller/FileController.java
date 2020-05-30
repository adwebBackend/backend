package fudan.se.project.controller;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.Table;

@RestController
@Table(name = "file")
@JsonIgnoreProperties({"handler","hibernateLazyInitializer"})
@Validated
public class FileController {

}
