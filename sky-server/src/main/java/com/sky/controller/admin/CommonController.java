package com.sky.controller.admin;

import com.alibaba.druid.wall.violation.ErrorCode;
import com.sky.constant.MessageConstant;
import com.sky.result.Result;
import com.sky.utils.AliOssUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/admin/common")
@Api(tags = "通用接口")
@Slf4j
public class CommonController {

    @Autowired
    AliOssUtil aliOssUtil;

    /**
     * 文件上传
     * @param file
     * @return
     */
    @PostMapping("upload")
    @ApiOperation(value = "文件上传接口")
    public Result<String> uploadFile(MultipartFile file) {
        log.info("文件上传：{}", file.getOriginalFilename());

        try {
            String fileName = file.getOriginalFilename();
            String suffixName = fileName.substring(fileName.lastIndexOf("."));

            String filePath = aliOssUtil.upload(file.getBytes(), UUID.randomUUID().toString() + suffixName);
            return Result.success(fileName);
        } catch (IOException e) {
            log.error("文件上传失败:{}", e.getMessage());
        }

        return Result.error(MessageConstant.UPLOAD_FAILED);
    }
}
