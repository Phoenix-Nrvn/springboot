package com.demo.springboot.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.demo.springboot.common.Result;
import com.demo.springboot.entity.Files;
import com.demo.springboot.mapper.FilesMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

/**
 * @author LISHANSHAN
 * @ClassName FileController
 * @Description 文件上传相关接口
 * @date 2022/07/2022/7/6 16:34
 */
@RestController
@RequestMapping("/file")
public class FileController {

    /** 将配置文件中的上传文件路径的配置拿到这里，以供使用 */
    @Value("${files.upload.path}")
    private String fileUploadPath;

    @Value("${server.ip}")
    private String serverIp;

    @Value("${server.port}")
    private String serverPort;

    @Resource
    private FilesMapper filesMapper;

    /**
     * Desc: 文件上传接口
     * @param file 前端传递过来的文件
     * @return {@link String} 直接返回一个String即可，前端拿去直接可用，包装一下反而不方便用
     * @author LISHANSHAN
     * @date 2022/7/6 17:02
     */
    @PostMapping("/upload")
    public String upload(@RequestParam MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();
        // 获取文件的类型
        String type = FileUtil.extName(originalFilename);
        long size = file.getSize();
        // 存储到磁盘，可以将这部分和下边的uploadFile合并，即uploadFile.getParentFile()
        File uploadParentFile = new File(fileUploadPath);
        // 判断配置的文件目录是否存在，如果不存在，创建一个新的
        if (!uploadParentFile.exists()) {
            uploadParentFile.mkdirs();
        }

        // 获取文件后缀，即文件类型
        // String fileType = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
        // 定义一个文件唯一的标识码
        String uuid = IdUtil.fastSimpleUUID();
        String fileUUID = uuid + StrUtil.DOT + type;
        // 生成图片将存入的位置
        File uploadFile = new File(fileUploadPath + fileUUID);
        System.out.println(fileUploadPath + fileUUID);
        // 为了避免文件过多，占用磁盘空间，根据文件的MD5区分文件，相同的文件，直接使用原来的链接
        // 此处获取文件的md5，并将其保存到数据库中
        // 将其存入磁盘
        file.transferTo(uploadFile);
        String url;
        // 获取文件md5码
        String md5 = SecureUtil.md5(uploadFile);
        // 在数据库中查找是否已存在相同的md5
        Files fileByMD5 = getFileByMD5(md5);
        System.out.println(fileByMD5);
        // 如果获取到对象，就将上传的对象从磁盘删除
        if (fileByMD5 != null) {
            uploadFile.delete();
            url = fileByMD5.getUrl();
        } else {
            url = "http://" + serverIp + ":" + serverPort + "/file/" + fileUUID;
        }

        // 存储数据库
        Files saveFiles = new Files();
        saveFiles.setName(originalFilename);
        saveFiles.setType(type);
        saveFiles.setSize(size / 1024);
        saveFiles.setUrl(url);
        saveFiles.setMd5(md5);
        filesMapper.insert(saveFiles);

        return url;
    }

    /**
     * Desc: 文件下载接口："http://localhost:9090/file/" + fileUUID
     * @param fileUUID
     * @param response
     * @return
     * @author LISHANSHAN
     * @date 2022/7/6 18:14
     */
    @GetMapping("/{fileUUID}")
    public void download(@PathVariable String fileUUID, HttpServletResponse response) throws IOException {
        // 根据文件的唯一标识符获取文件
        File uploadFile = new File(fileUploadPath + fileUUID);
        // 设置输出流的格式
        ServletOutputStream os = response.getOutputStream();
        response.addHeader("Content-Disposition", "attachment;filename" + URLEncoder.encode(fileUUID, "UTF-8"));
        response.setContentType("application/octet-stream");

        // 读取文件的字节流
        os.write(FileUtil.readBytes(uploadFile));
        os.flush();
        os.close();
    }

    /**
     * Desc: 将获取md5的代码进行封装
     * @param md5
     * @return {@link String}
     * @author LISHANSHAN
     * @date 2022/7/6 21:30
     */
    private Files getFileByMD5(String md5) {
        // 首先根据MD5去数据库中搜索，获取到即意味着磁盘中已经保存过文件，则直接将获取到的链接放入即可
        // 即无需重复存入磁盘获取链接
        // 将获取到的文件存储到磁盘目录去
        QueryWrapper<Files> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("md5", md5);
        // 数据库中可以存在重复的md5，故而查询出的可能是列表
        List<Files> list = filesMapper.selectList(queryWrapper);
        // 由于要获取的实际是url，故而只需返回一个查询出的文件即可

        return list.size() == 0 ? null : list.get(0);
    }

    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {
        Files file = filesMapper.selectById(id);
        file.setIsDeleted(true);
        int num = filesMapper.updateById(file);
        return Result.success(num);
    }

    @PostMapping("/batchDelete")
    public Result batchDelete(@RequestBody List<Integer> ids) {

        List<Files> list = filesMapper.selectBatchIds(ids);
        list.forEach(file -> {
            file.setIsDeleted(true);
            filesMapper.updateById(file);
        });

        return Result.success();
    }


    @PostMapping("/update")
    public Result update(@RequestBody Files file) {
        return Result.success(filesMapper.updateById(file));
    }

    /**
     * Desc: 分页查询
     * @param pageNo
     * @param pageSize
     * @param name
     * @return {@link Result}
     * @author LISHANSHAN
     * @date 2022/7/14 23:19
     */
    @GetMapping("/pageSelect")
    public Result pageSelect(@RequestParam("pageNo") Integer pageNo,
                             @RequestParam("pageSize") Integer pageSize,
                             @RequestParam String name) {
        QueryWrapper<Files> queryWrapper = new QueryWrapper<>();
        // 查询未删除的字段，与上边的删除将isDeleted字段设置为true相对应
        queryWrapper.eq("is_deleted", false);
        if (!StringUtils.isEmpty(name)) {
            queryWrapper.like("name", name);
        }
        queryWrapper.orderByDesc("id");
        Page<Files> page = filesMapper.selectPage(new Page<>(pageNo, pageSize), queryWrapper);
        return Result.success(page);
    }
}
