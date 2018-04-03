package com.bluetroy.httpservice.mvc.service.impl;


import com.bluetroy.httpservice.http.Status;
import com.bluetroy.httpservice.http.request.Request;
import com.bluetroy.httpservice.http.response.Response;
import com.bluetroy.httpservice.mvc.annotation.Service;
import com.bluetroy.httpservice.mvc.service.ServiceInterface;

@Service(urlPattern = "^/$")
public class IndexService implements ServiceInterface {
    @Override
    public Response service(Request request) {
        Response response = new Response(Status.SUCCESS_200);
        response.setContent("测试成功".getBytes());
        return response;
        // return new JsonResponse(Status.SUCCESS_200, response);
    }
}
