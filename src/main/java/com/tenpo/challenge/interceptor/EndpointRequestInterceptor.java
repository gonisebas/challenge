package com.tenpo.challenge.interceptor;

import com.tenpo.challenge.SensibleFields;
import com.tenpo.challenge.model.EndpointEntryEntiy;
import com.tenpo.challenge.service.EndpointService;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@Component
public class EndpointRequestInterceptor extends HandlerInterceptorAdapter {

    private static final String SENSIBLE_MASK = "***";

    @Autowired
    private EndpointService endpointService;

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) {
        persistEndpointRequest(request);
        return true;
    }

    private void persistEndpointRequest(HttpServletRequest request) {
        EndpointEntryEntiy endpoint = new EndpointEntryEntiy();
        endpoint.setMethod(request.getMethod());
        endpoint.setUri(request.getRequestURI());
        endpoint.setUsername(getUserDetails(request.getSession()));
        endpoint.setParameters(convert(request.getParameterMap()));
        endpointService.save(endpoint);
    }

    private String convert(final Map<String, String[]> parameterMap) {
        var map = new HashMap<String, String[]>(parameterMap);
        maskSensibleData(map);
        JSONObject jObject = new JSONObject(map);
        return jObject.toJSONString();
    }

    private void maskSensibleData(HashMap<String, String[]> map) {
        SensibleFields.stream().forEach(f ->
             map.replace(f.getFieldName(), new String[]{SENSIBLE_MASK})
        );
    }

    public static boolean isUserLogged() {
        try {
            return !SecurityContextHolder.getContext().getAuthentication()
                    .getName().equals("anonymousUser");
        } catch (Exception e) {
            return false;
        }
    }

    private String getUserDetails(HttpSession session) {
        if (isUserLogged()) {
            return SecurityContextHolder.getContext().getAuthentication().getName();
        }else{
            return null;
        }

    }

}