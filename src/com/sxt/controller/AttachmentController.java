package com.sxt.controller;

import com.sxt.bean.Attachment;
import com.sxt.dao.AttachmentDAO;
import com.sxt.dao.impl.AttachmentDAOImpl;
import com.sxt.utils.DataGridView;
import com.sxt.utils.StrUtils;
import org.apache.commons.io.FileUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class AttachmentController {
    private AttachmentDAO attachmentDAO = new AttachmentDAOImpl();

    /**
     * 文件全查询
     */
    @RequestMapping("queryAll")
    @ResponseBody
    public DataGridView queryAll() {
        List<Attachment> list = attachmentDAO.query();
        DataGridView dv = new DataGridView(list.size(), list);
        return dv;
    }

    /**
     * 文件上传
     */
    @RequestMapping("addAttachment")
    @ResponseBody
    public Map<String, Object> addAttachment(MultipartFile mf, HttpSession session) {
        Map<String, Object> map = new HashMap<String, Object>();
        String path = session.getServletContext().getRealPath("/upload");
        String oldName = mf.getOriginalFilename();
        String newName = StrUtils.createStrUseUUID(oldName);
        try {
            //将文件保存到服务器的path路径下
            mf.transferTo(new File(path, newName));
            //将文件信息保存到数据库
            attachmentDAO.add(new Attachment(oldName, newName, mf.getContentType(), "/upload/" + newName));
            map.put("status", "success");
            map.put("msg", "文件上传成功");
        } catch (IOException e) {
            e.printStackTrace();
            map.put("msg", "文件上传失败");
        }
        return map;
    }

    /**
     * 文件下载
     */
    @RequestMapping("downloadAttachment")
    public ResponseEntity downloadAttachment(Integer fid, HttpSession session) {
        //根据fid从数据库中找到这个文件
        Attachment attachment = this.attachmentDAO.queryById(new Attachment(fid));
        String path = session.getServletContext().getRealPath("");
        //构造下载文件
        File file = new File(path, attachment.getFpath());
        ResponseEntity entity = null;
        try {
            if (file.exists()) {
                //把文件转换成byte数组
                byte[] bys = FileUtils.readFileToByteArray(file);

                //创建响应头
                HttpHeaders httpHeaders = new HttpHeaders();
                //设置响应文件类型
                httpHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
                //设置响应内容的大小
                httpHeaders.setContentLength(bys.length);
                //设置文件描述
                httpHeaders.setContentDispositionFormData("attachment", attachment.getFoldname(), Charset.forName("utf-8"));
                entity = new ResponseEntity(bys, httpHeaders, HttpStatus.CREATED);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return entity;
    }

    /**
     * 删除文件
     */
    @RequestMapping("deleteAttachment")
    @ResponseBody
    public Map<String, Object> deleteAttachment(Integer fid, HttpSession session) {
        //根据fid从数据库里找到这个文件
        Attachment attachment = this.attachmentDAO.queryById(new Attachment(fid));
        Map<String, Object> map = new HashMap<>();
        String path = session.getServletContext().getRealPath("");
        //构造下载文件
        File file = new File(path, attachment.getFpath());

        try {
            if (file.exists()) {
                file.delete();
            }
            map.put("status", "success");
            map.put("msg", "文件删除成功");
            //从数据库里面将文件删除
            this.attachmentDAO.delete(attachment);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("msg", "文件删除失败");
        }
        return map;
    }
}
