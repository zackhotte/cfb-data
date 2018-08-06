package com.github.zackhotte.cfbdata.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public abstract class CFBParser<T> {

    @Getter
    protected URL url;
    @Getter
    protected JsonNode root;

    public CFBParser(String url, Object... args) {
        try {
            this.url = new URL(String.format(url, args));
            ObjectMapper mapper = new ObjectMapper();
            this.root = mapper.readTree(this.url);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public abstract List<T> getData();

}