package com.demo.controller;

import cn.hutool.core.date.DateUtil;
import com.demo.common.R;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 文件上传控制器
 */
@Tag(name = "文件上传")
@RestController
@RequestMapping("/api/upload")
public class UploadController {

    @Value("${file.upload.path}")
    private String uploadPath;

    @Value("${file.upload.url}")
    private String uploadUrl;

    @Operation(summary = "上传图片")
    @PostMapping("/image")
    public R<?> uploadImage(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return R.fail("请选择要上传的文件");
        }

        // 获取文件名
        String originalFilename = file.getOriginalFilename();
        // 获取文件后缀
        String suffix = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        // 生成新文件名
        String newFileName = UUID.randomUUID().toString().replace("-", "") + suffix;
        // 按日期生成目录
        String dateDir = DateUtil.format(new Date(), "yyyyMMdd");
        // 确保uploadPath以/结尾
        String basePath = uploadPath.endsWith("/") ? uploadPath : uploadPath + "/";
        // 完整路径
        String filePath = basePath + dateDir + "/" + newFileName;

        try {
            // 创建目录
            File dest = new File(filePath);
            if (!dest.getParentFile().exists()) {
                dest.getParentFile().mkdirs();
            }
            // 保存文件
            file.transferTo(dest);
            // 返回访问URL
            String url = uploadUrl + dateDir + "/" + newFileName;
            Map<String, String> result = new HashMap<>();
            result.put("url", url);
            result.put("name", originalFilename);
            return R.ok(result);
        } catch (IOException e) {
            e.printStackTrace();
            return R.fail("文件上传失败：" + e.getMessage());
        }
    }
}
