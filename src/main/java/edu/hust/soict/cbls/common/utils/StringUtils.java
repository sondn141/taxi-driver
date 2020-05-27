package edu.hust.soict.cbls.common.utils;

import java.util.LinkedList;
import java.util.List;

public class StringUtils {

    public static boolean isNullOrEmpty(String content){
        return content == null || content.isEmpty();
    }

    public static String toPath(String... components){
        StringBuilder strBuilder = new StringBuilder();
        List<String> std = new LinkedList<>();
        for(String component : components){
            if(!component.endsWith("/"))
                std.add(component.substring(component.lastIndexOf("/")));
            else std.add(component);
        }

        return String.join("/", std);
    }

    public static boolean validPath(String content){
        String[] comps = content.split("/");
        for(int i = 0 ; i < comps.length ; i ++){
            if(comps[i].contains("\\."))
                if(i == comps.length - 1)
                    return true;
        }

        return false;
    }

    public static String getDir(String content){
        if(validPath(content))
            return content;
        else
            return content.substring(content.lastIndexOf("/"));
    }
}
