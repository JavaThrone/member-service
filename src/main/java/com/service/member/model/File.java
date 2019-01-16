package com.service.member.model;

import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Document(collection = "files")
public class File implements Serializable {

    private static final long serialVersionUID = 79522632352152196L;

    private String name;
    private String contentType;
    @Transient
    private byte[] stream;

    public File() {
    }

    public File(String name, String contentType, byte[] stream) {
        this.name = name;
        this.contentType = contentType;
        this.stream = stream;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public byte[] getStream() {
        return stream;
    }

    public void setStream(byte[] stream) {
        this.stream = stream;
    }

    @Override
    public String toString() {
        return "File{" +
                "name='" + name + '\'' +
                ", contentType='" + contentType + '\'' +
                '}';
    }
}
