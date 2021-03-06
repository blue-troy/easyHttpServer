package com.bluetroy.httpservice.mvc.service.impl;


import com.bluetroy.httpservice.http.Status;
import com.bluetroy.httpservice.http.request.Request;
import com.bluetroy.httpservice.http.response.Response;
import com.bluetroy.httpservice.mvc.annotation.Controller;
import com.bluetroy.httpservice.mvc.annotation.RequestMapping;
import com.bluetroy.httpservice.mvc.model.Student;
import com.bluetroy.httpservice.mvc.service.ServiceInterface;

@Controller()
@RequestMapping(value = "/")
public class IndexService implements ServiceInterface {

    @RequestMapping
    public Response service(Request request) {
        Response response = new Response(Status.SUCCESS_200);
        response.setContent("测试成功".getBytes());
        return response;
        // return new JsonResponse(Status.SUCCESS_200, response);
    }

    @RequestMapping(value = "hello")
    public Response hello(Request request) {
        Response response = new Response(Status.SUCCESS_200);
        response.setContent("hello".getBytes());
        return response;
    }

    @RequestMapping(value = "student")
    public Response student(Student student) {
        System.out.println(student);
        Response response = new Response(Status.SUCCESS_200);
        response.setContent(student.toString().getBytes());
        return response;
    }
}
