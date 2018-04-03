package com.bluetroy.httpservice.http.request;

import java.util.Map;

public class RequestBody {
    private Map<String, String> formMap;
    private Map<String, MimeData> mimeMap;

    public Map<String, String> getFormMap() {
        return formMap;
    }

    public void setFormMap(Map<String, String> formMap) {
        this.formMap = formMap;
    }

    public Map<String, MimeData> getMimeMap() {
        return mimeMap;
    }

    public void setMimeMap(Map<String, MimeData> mimeMap) {
        this.mimeMap = mimeMap;
    }
}
