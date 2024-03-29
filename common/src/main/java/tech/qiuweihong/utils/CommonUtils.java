package tech.qiuweihong.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Objects;
import java.util.UUID;

@Slf4j
public class CommonUtils {
    /**
     * 获取ip
     * @param request
     * @return
     */
    public static String getIpAddr(HttpServletRequest request) {
        String ipAddress = null;
        try {
            ipAddress = request.getHeader("x-forwarded-for");
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader("Proxy-Client-IP");
            }
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader("WL-Proxy-Client-IP");
            }
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getRemoteAddr();
                if (ipAddress.equals("127.0.0.1")) {
                    // 根据网卡取本机配置的IP
                    InetAddress inet = null;
                    try {
                        inet = InetAddress.getLocalHost();
                    } catch (UnknownHostException e) {
                        e.printStackTrace();
                    }
                    ipAddress = inet.getHostAddress();
                }
            }
            // 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
            if (ipAddress != null && ipAddress.length() > 15) {
                // "***.***.***.***".length()
                // = 15
                if (ipAddress.indexOf(",") > 0) {
                    ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
                }
            }
        } catch (Exception e) {
            ipAddress="";
        }
        return ipAddress;
    }



    public static String MD5(String data)  {
        try {
            java.security.MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] array = md.digest(data.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte item : array) {
                sb.append(Integer.toHexString((item & 0xFF) | 0x100), 1, 3);
            }

            return sb.toString().toUpperCase();
        } catch (Exception exception) {
        }
        return null;

    }
    public static String getRandomCode(int length){
        String source = "0123456789";
        String code = "";
        for (int i = 0; i < length; i++) {
            code += (int)(Math.random()*10);
        }
        return code;
    }

    public static String generateUUID(){
        return UUID.randomUUID().toString().replaceAll("-","").subSequence(0,32).toString();
    }

    /**
     * Generate random string
     * @param length
     * @return
     */
    public static String getStringNumRandom(int length){
        String source = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        String code = "";
        for (int i = 0; i < length; i++) {
            code += source.charAt((int)(Math.random()*source.length()));
        }
        return code;
    }

    public static void sendJsonMessage(HttpServletResponse response, Object obj){
        ObjectMapper objectMapper = new ObjectMapper();
        response.setContentType("application/json; charset=utf-8");
        try(PrintWriter writer = response.getWriter()){
            writer.print(objectMapper.writeValueAsString(obj));
            response.flushBuffer();

        }catch (Exception e){
            log.warn("Returning Exception {}",e);

        }
    }
}
