package com.bluetroy.httpservice.mvc;

import com.bluetroy.httpservice.http.Http;
import com.bluetroy.httpservice.http.request.Request;
import com.bluetroy.httpservice.mvc.service.impl.StaticFileService;
import com.bluetroy.httpservice.utils.RequestUtil;

public class DispatcherServlet {
    public static Service dispatch(Http http) throws NoSuchMethodException {
        Request request = http.getRequest();
        //todo 修改成spring bean方式实现 此处应该发现这部分是静态文件服务器，应该是属于http服务部分
        if (RequestUtil.isStaticFileRequest(request))
            return new Service(new StaticFileService(), StaticFileService.class.getMethod("service"));
        return ServiceRegistry.findService(request.getHeader().getURI());
    }
}
