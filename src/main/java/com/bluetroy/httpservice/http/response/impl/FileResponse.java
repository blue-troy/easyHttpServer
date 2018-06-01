package com.bluetroy.httpservice.http.response.impl;


import com.bluetroy.httpservice.http.Status;
import com.bluetroy.httpservice.http.response.Response;
import com.bluetroy.servlet.utils.ContentTypeUtil;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.logging.Logger;

public class FileResponse extends Response {
    private static final Logger LOGGER = Logger.getLogger("fileResponse");
    public FileResponse(Status status, File file) {
        super(status);
        try {
            content = Files.readAllBytes(file.toPath());
            String contentType = ContentTypeUtil.getContentType(file.toPath().toString());
            heads.put("contentType", contentType);

        } catch (IOException e) {
            this.setStatus(Status.BAD_REQUEST_400);
            LOGGER.warning("文件"+file.getAbsolutePath() +"读取错误");
        }
    }
}
