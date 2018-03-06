package com.watent.soa.remote.servlet;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.watent.soa.bean.Service;
import org.springframework.context.ApplicationContext;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * servlet server
 *
 * @author Atom
 */
public class DispatcherServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try {
            JSONObject requestParam = htpProcess(req, resp);
            //远程spring容器拿到对应serviceId实例
            String serviceId = requestParam.getString("serviceId");
            String methodName = requestParam.getString("methodName");

            JSONArray paramTypes = requestParam.getJSONArray("paramTypes");
            JSONArray methodParamsJa = requestParam.getJSONArray("methodParams");

            System.out.println("methodName:"+methodName);

            //反射参数
            Object[] objs = null;
            if (null != methodParamsJa) {
                objs = new Object[methodParamsJa.size()];
                int i = 0;
                for (Object jobj : methodParamsJa) {
                    objs[i++] = jobj;
                }
            }

            //拿到spring的上下文
            ApplicationContext applicationContext = Service.getApplicationContext();
            //服务层的实例
            Object serviceBean = applicationContext.getBean(serviceId);
            System.out.println("serviceBean:"+serviceBean);
            //这个方法的获取，要考虑到这个方法的重载
            Method method = getMethod(serviceBean, methodName, paramTypes);
            if (null != method) {
                Object result = null;
                result = method.invoke(serviceBean, objs);
                PrintWriter printWriter = resp.getWriter();
                printWriter.write(result.toString());
            } else {
                PrintWriter printWriter = resp.getWriter();
                printWriter.write("------ No such method ------");
            }
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }

    }

    public static JSONObject htpProcess(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        StringBuffer buffer = new StringBuffer();
        InputStream is = req.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
        String s = "";
        while ((s = br.readLine()) != null) {
            buffer.append(s);
        }
        if (buffer.length() <= 0) {
            return null;
        } else {
            return JSONObject.parseObject(buffer.toString());
        }
    }

    private Method getMethod(Object bean, String methodName, JSONArray paramTypes) {

        Method[] methods = bean.getClass().getMethods();
        List<Method> retMethods = new ArrayList<>();

        for (Method method : methods) {
            //放入同名的
            if (method.getName().trim().equals(methodName)) {
                retMethods.add(method);
            }
        }

        if (retMethods.size() == 1) {
            return retMethods.get(0);
        }

        boolean isSameSize = false;
        boolean isSameType = false;
        tag:
        for (Method method : retMethods) {
            Class<?>[] types = method.getParameterTypes();
            if (types.length == paramTypes.size()) {
                isSameSize = true;
            }
            if (!isSameSize) {
                continue;
            }

            for (int i = 0; i < types.length; i++) {
                if (types[i].toString().contains(paramTypes.getString(i))) {
                    isSameType = true;
                } else {
                    isSameType = false;
                }
                if (!isSameType) {
                    continue tag;
                }
            }
            if (isSameType) {
                return method;
            }
        }
        return null;
    }

}
