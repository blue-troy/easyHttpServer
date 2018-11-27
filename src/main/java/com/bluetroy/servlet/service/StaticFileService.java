package com.bluetroy.servlet.service;


import com.bluetroy.httpservice.http.Status;
import com.bluetroy.httpservice.http.request.Request;
import com.bluetroy.httpservice.http.response.Response;
import com.bluetroy.httpservice.http.response.impl.FileResponse;
import com.bluetroy.servlet.annotation.Controller;
import com.bluetroy.servlet.annotation.RequestMapping;

import java.io.File;

@Controller()
@RequestMapping(value = "\\S*\\.\\S*")
public class StaticFileService {
    private static String staticPath;
    private static String root;

    static {
        root = StaticFileService.class.getResource("/").getPath();
    }

    public Response service(Request request) {
        String filePath = root + request.getHeader().getURI().substring(1);
        return new FileResponse(Status.SUCCESS_200, new File(filePath));
    }


}
