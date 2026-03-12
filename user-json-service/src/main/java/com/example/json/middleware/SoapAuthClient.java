package com.example.json.middleware;

import com.example.json.model.AuthInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

@Component
public class SoapAuthClient {

    @Value("${soap.service.url}")
    private String soapUrl;

    public AuthInfo validateToken(String token) {
        String requestXml =
            "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" " +
            "xmlns:auth=\"http://num.edu.mn/users/auth\">" +
            "<soapenv:Header/><soapenv:Body>" +
            "<auth:ValidateTokenRequest>" +
            "<auth:token>" + token + "</auth:token>" +
            "</auth:ValidateTokenRequest>" +
            "</soapenv:Body></soapenv:Envelope>";

        try {
            URL url = new URL(soapUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
            conn.setRequestProperty("SOAPAction", "");
            conn.setDoOutput(true);

            try (OutputStream os = conn.getOutputStream()) {
                os.write(requestXml.getBytes("UTF-8"));
            }

            BufferedReader br = new BufferedReader(
                new InputStreamReader(conn.getInputStream(), "UTF-8"));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) sb.append(line);
            br.close();

            System.out.println("=== SOAP Response: " + sb.toString());
            return parseResponse(sb.toString());

        } catch (Exception e) {
            System.out.println("=== SOAP Error: " + e.getMessage());
            AuthInfo info = new AuthInfo();
            info.setValid(false);
            info.setMessage("SOAP холбогдохгүй: " + e.getMessage());
            return info;
        }
    }

    private AuthInfo parseResponse(String xml) {
        AuthInfo info = new AuthInfo();
        try {
            info.setValid(xml.contains("<valid>true</valid>") ||
                          xml.contains("<ns2:valid>true</ns2:valid>") ||
                          xml.contains("<ns3:valid>true</ns3:valid>"));
            String userId = extract(xml, "userId");
            if (userId != null && !userId.trim().isEmpty()) {
                try { info.setUserId(Integer.parseInt(userId.trim())); }
                catch (Exception ignored) {}
            }
            info.setUsername(extract(xml, "username"));
            info.setRole(extract(xml, "role"));
            info.setMessage(extract(xml, "message"));
        } catch (Exception e) {
            info.setValid(false);
            info.setMessage("Parse алдаа");
        }
        return info;
    }

    private String extract(String xml, String tag) {
        String[] prefixes = {"ns2:", "ns3:", "ns4:", ""};
        for (String p : prefixes) {
            String open  = "<"  + p + tag + ">";
            String close = "</" + p + tag + ">";
            int start = xml.indexOf(open);
            if (start != -1) {
                int end = xml.indexOf(close, start);
                if (end != -1) return xml.substring(start + open.length(), end);
            }
        }
        return null;
    }
}