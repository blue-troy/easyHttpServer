package com.bluetroy.httpservice;

import com.bluetroy.httpservice.http.Http;
import com.bluetroy.httpservice.http.request.Request;
import com.bluetroy.servlet.Service;
import com.bluetroy.servlet.ServiceRegistry;
import com.bluetroy.servlet.service.StaticFileService;
import com.bluetroy.servlet.utils.RequestUtil;

public class DispatcherServlet {
    public static Service dispatch(Http http) {
        Request request = http.getRequest();
        //todo 修改成spring bean方式实现 此处应该发现这部分是静态文件服务器，应该是属于http服务部分
        if (RequestUtil.isStaticFileRequest(request)) {
            try {
                return new Service(new StaticFileService(), StaticFileService.class.getMethod("service"));
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
        return ServiceRegistry.findService(request.getHeader().getURI());
    }
}
