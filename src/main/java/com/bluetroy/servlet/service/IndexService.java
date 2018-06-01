package com.bluetroy.servlet.service;


import com.bluetroy.httpservice.http.Status;
import com.bluetroy.httpservice.http.request.Request;
import com.bluetroy.httpservice.http.response.Response;
import com.bluetroy.mvc.model.Student;
import com.bluetroy.servlet.annotation.Controller;
import com.bluetroy.servlet.annotation.RequestMapping;

@Controller()
@RequestMapping(value = "/")
public class IndexService {

    @RequestMapping
    public Response service(Request request) {
        Response response = new Response(Status.SUCCESS_200);
        response.setContent("测试成功".getBytes());
        return response;
        // return new JsonResponse(Status.SUCCESS_200, response);
    }

    @RequestMapping(value = "hello")
    public Response hello(String name) {
        System.out.println(name);
        Response response = new Response(Status.SUCCESS_200);
        response.setContent(new String("你好" + name).getBytes());
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
