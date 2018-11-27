package com.bluetroy.httpservice.http;

public enum Status {
    SUCCESS_200(200, "OK"),
    TEMPORARILY_MOVED(302, "TEMPORARILY_MOVED"),
    NOT_MODIFIED_304(304, "NOT MODIFIED"),
    BAD_REQUEST_400(400, "BAD REQUEST"),
    NOT_FOUND_404(404, "NOT FOUND"),
    METHOD_NOT_ALLOWED_405(405, "METHOD NOT ALLOWED"),
    INTERNAL_SERVER_ERROR_500(500, "INTERNAL SERVER ERROR");

    private int code;
    private String message;

    Status(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
