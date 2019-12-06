package com.mmall.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * Created by Administrator on 2019/11/18 0018.
 */
public interface IFileService {

    String upload(MultipartFile file, String path);
}
