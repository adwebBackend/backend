package fudan.se.project.tool;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.*;
import java.util.logging.Logger;

public class Tool {
    /**
     * 全部结果List转Page工具
     * @param list  全部查询结果
     * @param pageable 分页信息
     * @return Page
     */

    private static Logger logger = Logger.getLogger("LoggingDemo");

    private Random rand = SecureRandom.getInstanceStrong();

    public Tool() throws NoSuchAlgorithmException {
    }

    public static <T> Page<T> listToPage(List<T> list, Pageable pageable){
        //查询结果为空
        if(list.isEmpty())
            return new PageImpl<T>(list, pageable,list.size());
        //要返回的页面大于最后一页
        if(list.size() < pageable.getPageNumber() * pageable.getPageSize())
            return new PageImpl<T>(new ArrayList<>(),pageable,list.size());
        //当前页第一条数据在List中的位置
        int start;
        //Pageable.getOffset(): 当total不足pageSize时，返回的是pageSize
        if(list.size() < pageable.getPageSize())
            start = 0;
        else
            start = (int)pageable.getOffset();
        //当前页最后一条数据在List中的位置
        int end = Math.min((start + pageable.getPageSize()), list.size());
        return new PageImpl<T>(list.subList(start,end),pageable, list.size());
    }

    /**
     * 通过当前页查询结果返回jsonObject
     * @param resultPage 当前页查询结果
     * @return json格式：if resultPage is not empty : {number:num,conference:[{},{}]}
     *  else : {number:num}
     */
    public static <T> JSONObject pageToJsonResponse(Page<T> resultPage, String title){
        JSONObject jsonObject = new JSONObject();
        if(resultPage.isEmpty()) {
            jsonObject.put("number", 0);
            jsonObject.put("record", resultPage.getTotalElements());
            return jsonObject;
        }
        //该页不为空
        List<T> resultList = resultPage.getContent();
        jsonObject.put("number",resultPage.getNumberOfElements());  //当前页元素个数
        jsonObject.put("record",resultPage.getTotalElements());     //总记录数
        //查询内容以JSONArray存放
        JSONArray jsonarray = JSONArray.parseArray(JSON.toJSONString(resultList));
        jsonObject.put(title,jsonarray);

        return jsonObject;
    }

    //检验参数是否异常
    public static JSONObject DealParamError(BindingResult bindingResult) throws JSONException {
        if (bindingResult.hasErrors()) {
            //有校验没通过
            JSONObject result = new JSONObject();
            List<ObjectError> errorList = bindingResult.getAllErrors();
            for (ObjectError error : errorList) {
                System.out.println(error.getDefaultMessage());  //输出具体的错误信息
            }
            result.put("message", "parameter error");
            return result;
        }
        //通过了参数校验
        return null;
    }

    public static ResponseEntity<?> getResponseEntity(String message) throws JSONException {
        JSONObject result = new JSONObject();
        if(message.equals("success")) {
            result.put("message", message);
            return new ResponseEntity<>(result.toJSONString(), HttpStatus.OK);
        }
        else {
            result.put("message", message);
            return new ResponseEntity<>(result.toJSONString(), HttpStatus.BAD_REQUEST);
        }
    }

    public static ResponseEntity<?> getResponseEntity(JSONObject result) throws JSONException {
        if (result.getInteger("number") != null && result.getInteger("number") == 0){
            return new ResponseEntity<>(result.toJSONString(), HttpStatus.BAD_REQUEST);
        }
        if (result.getString("message") != null && !"success".equals(result.getString("message"))){
            return new ResponseEntity<>(result.toJSONString(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(result.toJSONString(), HttpStatus.OK);
    }

    public static String TransferStringArrayToString(String[] stringArray){
        StringBuffer tmp = new StringBuffer();
        int len = stringArray.length;
        tmp.append("[");
        for (int i = 0; i < len - 1; i++){
            tmp.append("\"" + stringArray[i] + "\",");
        }
        tmp.append("\"" + stringArray[len - 1] + "\"]");
        return tmp.toString();
    }

    public static String[] TransferStringToStringArray(String string){
        String[] res = string.split(",");
        int len = res.length;
        res[0] = res[0].substring(2, res[0].length() - 1);
        for (int i = 1; i < len - 1; i++){
            res[i] = res[i].substring(1, res[i].length() - 1);
        }
        res[len - 1] = res[len - 1].substring(1, res[len - 1].length() - 2);
        return res;
    }

    //随机抽取3个人
    public HashSet<String> RandomThree(HashSet<String> origin_members){
        //刚好3个人就不需要再抽取了
        if (origin_members.size() == 3)
            return origin_members;
        List<String> allList = new ArrayList<>(origin_members);
        int size = origin_members.size();
        HashSet<String> reviewers = new HashSet<>();
        while (reviewers.size() < 3) {//获取3个
            //随机在list里取出元素，添加到新哈希集合
            reviewers.add(allList.get(this.rand.nextInt(size)));
        }
        return reviewers;
    }

    public static String SubmitFile(MultipartFile file){
        String baseUrl = "/usr/papers";
        File dir = new File(baseUrl);
        if (!dir.exists())
            dir.mkdirs();
        String fileName = file.getOriginalFilename();
        String name = fileName.substring(0,fileName.indexOf('.'));
        String suffix = fileName.substring(fileName.lastIndexOf('.'));
        // 写文件到服务器
        String filepath = dir.getAbsolutePath() + File.separator + file.getOriginalFilename();
        File serverFile = new File(filepath);
        //若文件名重复
        int i = 1;
        while (serverFile.exists()){
            String newFileName = name + "(" + i + ")" + suffix;
            filepath = serverFile.getParent() + File.separator + newFileName;
            serverFile = new File(filepath);
            i++;
        }
        try {
            file.transferTo(serverFile);
        } catch (Exception e) {
            logger.info(e.getMessage());
            return "Failed to upload " +  fileName + " => " + e.getMessage();
        }
        return "success " + filepath;
    }
}
