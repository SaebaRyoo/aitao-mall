package com.component.security.utils;

import org.json.JSONObject;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class WebUtils {

    public static void sendJson(HttpServletResponse response, JSONObject res) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");

        PrintWriter out = null;

        try {
            out = response.getWriter();
            out.append(res.toString());
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(500);
        }
    }
}
