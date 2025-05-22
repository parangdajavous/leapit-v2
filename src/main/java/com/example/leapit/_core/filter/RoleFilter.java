package com.example.leapit._core.filter;

import com.example.leapit._core.util.Resp;
import com.example.leapit.common.enums.Role;
import com.example.leapit.user.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;

public class RoleFilter implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("sessionUser") == null) {
            exResponse(response, "로그인 필요합니다.");
            return;
        }

        User user = (User) session.getAttribute("sessionUser");
        String uri = request.getRequestURI();

        if (uri.startsWith("/s/api/personal") && user.getRole() != Role.PERSONAL) {
            exResponse(response, "개인 회원을 위한 서비스입니다.");
            return;
        }

        if (uri.startsWith("/s/api/company") && user.getRole() != Role.COMPANY) {
            exResponse(response, "기업 회원을 위한 서비스입니다.");
            return;
        }

        chain.doFilter(request, response);
    }

    private void exResponse(HttpServletResponse response, String msg) throws IOException {
        response.setContentType("application/json;charset=utf-8");
        response.setStatus(403);
        PrintWriter out = response.getWriter();

        Resp<?> resp = Resp.fail(403, msg);
        String responseBody = new ObjectMapper().writeValueAsString(resp);
        out.println(responseBody);
    }
}
