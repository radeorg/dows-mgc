package org.dows.mgc.reader;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import jakarta.ws.rs.HttpMethod;
import jakarta.ws.rs.core.MediaType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dows.mgc.entity.MindNode;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description: </br>
 * @author: lait.zhang@gmail.com
 * @date: 4/29/2024 7:24 PM
 * @history: </br>
 * <author>      <time>      <version>    <desc>
 * 修改人姓名      修改时间        版本号       描述
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class GitmindReader {

    private final GitmindProperties gitmindProperties;
    ObjectMapper objectMapper = new ObjectMapper();
    RestTemplate restTemplate = new RestTemplate();

    FileSystemResource fileSystemResource =
            new FileSystemResource(System.getProperty("user.dir") + File.separator + "gitmind.token");


    public List<MindNode> getGitMindNode(BuildRequest buildRequest) {
        //Map<String, MindNode> mindNodeMap = new HashMap<>();
        List<MindNode> mindNodeList = new ArrayList<>();
        try {
            GitMind gitMind = buildRequest.getGitMind();
            String token;
            if (gitMind != null) {
                log.info(JSONUtil.toJsonStr(gitMind));
                token = login(gitMind.getUsername(), gitMind.getPassword());
            } else {
                token = login(null, null);
            }
            String guid = getGuid(token);
            List<MindXpath> mindXpaths = gitMind.getMindXpaths();
            for (MindXpath mindXpath : mindXpaths) {
                String mindFileName = mindXpath.getMindFileName();
                Map mindFileNames = getMindFileNames(token, guid, mindFileName);
                if (mindFileNames != null) {
                    String mindGuid = (String) mindFileNames.get("guid");
                    String mindUrl = getMindFileUrl(token, mindGuid);
                    String mindInfo = getMindInfo(mindUrl);
                    JSONObject entries = JSONUtil.parseObj(mindInfo);
                    MindNode mindNode = entries.get("root", MindNode.class);
                    mindNode.setName(mindFileName);
                    mindNodeList.add(mindNode);
                    //mindNodeMap.put(mindFileName, mindNode);
                }
            }
            return mindNodeList;
            /*Map mindFileNames = getMindFileNames(token, guid, buildRequest.getGitmind());
            if (mindFileNames != null) {
                String mindGuid = (String) mindFileNames.get("guid");
                String mindUrl = getMindFileUrl(token, mindGuid);
                String mindInfo = getMindInfo(mindUrl);
                JSONObject entries = JSONUtil.parseObj(mindInfo);
                MindNode mindNode = entries.get("root", MindNode.class);
                return mindNode;
            }*/
        } catch (Exception e) {
            //refreshToken();
            log.info("refresh token: {}", e.getMessage());
        }
        return null;
    }

    public void refreshToken() {
        if (fileSystemResource.exists()) {
            fileSystemResource.getFile().delete();
        }
    }

    public Map getMindFileNames(String token, String guid, String mindFile) {
        String mindFiles = this.listMindFile(token, guid);
        List<Map<?, ?>> data = JsonPath.parse(mindFiles).read("data", List.class);
        Map mindFileNames = data.stream()
                .filter(m -> mindFile.equals(m.get("file_name")))
                .findAny()
                .orElse(null);
        return mindFileNames;
    }

    public String login(String username, String password) throws IOException {
        boolean exists = fileSystemResource.exists();
        String token = null;
        if (exists) {
            File file = fileSystemResource.getFile();
            token = Files.readString(file.toPath());
        } else {
            try {
                String response = loginAndGetToken(username, password);
                //System.out.println(response);
                token = JsonPath.parse(response).read("data.api_token", String.class);
                Files.createFile(Path.of(fileSystemResource.getPath()));
                Files.writeString(Path.of(fileSystemResource.getPath()), token);
                //System.out.println(token);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return token;
    }


    public String getMindInfo(String mindUrl) {
        try {
            // 创建URL对象
            URL url = new URL(mindUrl);
            // 打开连接
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // 设置请求方法，默认是GET
            connection.setRequestMethod("GET");

            // 设置请求头，例如内容类型
            connection.setRequestProperty("Content-Type", "application/json");

            // 发送请求并连接到URL
            connection.connect();

            // 获取响应码
            int responseCode = connection.getResponseCode();
            //System.out.println("Response Code: " + responseCode);

            // 读取响应内容
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();

            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }

            // 打印响应内容
            //System.out.println("Response Content: " + response.toString());

            // 关闭连接和流
            reader.close();
            connection.disconnect();

            return response.toString();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }


    public String getMindFileUrl(String token, String guid) {
        // 创建HttpHeaders对象并设置请求头
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.set("Authorization", "Bearer " + token);

        // 创建HttpEntity对象，将请求头添加到其中
        HttpEntity<String> entity = new HttpEntity<>(headers);

        // 发送GET请求并获取响应
        ResponseEntity<String> response = restTemplate
                .exchange(String.format(gitmindProperties.getMindUrl(), guid), HttpMethod.GET, entity, String.class);

        String responseBody = "";
        // 处理响应
        if (response.getStatusCode().is2xxSuccessful()) {
            responseBody = response.getBody();
            //System.out.println("Response: " + responseBody);
        } else {
            //System.out.println("Error: " + response.getStatusCode());
            throw new RuntimeException("Error: " + response.getStatusCode());
        }
        //&rnd=1714379507999
        String url = JsonPath.parse(responseBody).read("data.project_url", String.class);
        url += "&rnd=" + System.currentTimeMillis();
        return url;
    }


    public String listMindFile(String token, String guid) {
        // 创建HttpHeaders对象并设置请求头
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.set("Authorization", "Bearer " + token);

        // 创建HttpEntity对象，将请求头添加到其中
        HttpEntity<String> entity = new HttpEntity<>(headers);

        // 发送GET请求并获取响应
        ResponseEntity<String> response = restTemplate
                .exchange(String.format(gitmindProperties.getMindFileUrl(), guid), HttpMethod.GET, entity, String.class);

        String responseBody = "";
        // 处理响应
        if (response.getStatusCode().is2xxSuccessful()) {
            responseBody = response.getBody();
            //System.out.println("Response: " + responseBody);
        } else {
            System.out.println("Error: " + response.getStatusCode());
        }
        return responseBody;
    }

    public String getGuid(String token) {
        // 创建HttpHeaders对象并设置请求头
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.set("Authorization", "Bearer " + token);

        // 创建HttpEntity对象，将请求头添加到其中
        HttpEntity<String> entity = new HttpEntity<>(headers);

        // 发送GET请求并获取响应
        ResponseEntity<String> response = restTemplate
                .exchange(gitmindProperties.getMindFiles(), HttpMethod.GET, entity, String.class);

        String responseBody = "";
        // 处理响应
        if (response.getStatusCode().is2xxSuccessful()) {
            responseBody = response.getBody();
            //System.out.println("Response: " + responseBody);
        } else {
            System.out.println("Error: " + response.getStatusCode());
        }
        return JsonPath.parse(responseBody).read("data.guid", String.class);
    }

    public String loginAndGetToken(String username, String password) throws IOException {
        Map loginParams = objectMapper.readValue(new ClassPathResource("login.json").getInputStream(), Map.class);
        if (StrUtil.isNotBlank(username)) {
            loginParams.put("telephone", username);
        }
        if (StrUtil.isNotBlank(password)) {
            loginParams.put("password", password);
        }
        // 设置请求头信息
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // 创建请求体
        Map<String, String> requestBody = new HashMap<>(loginParams);
        // 创建HttpEntity对象
        HttpEntity<Map<String, String>> entity = new HttpEntity<>(requestBody, headers);

        try {
            // 发送POST请求
            ResponseEntity<String> response = restTemplate
                    .exchange(gitmindProperties.getLoginUrl(), HttpMethod.POST, entity, String.class);

            // 检查响应状态码
            if (response.getStatusCode() == HttpStatus.OK) {
                // 假设token在响应体中，你可能需要解析JSON来提取token
                // 这里我们直接返回响应体作为示例
                return response.getBody();
            } else {
                throw new RuntimeException("Login failed: " + response.getStatusCode());
            }

        } catch (Exception e) {
            throw new RuntimeException("Error during login", e);
        }
    }
}

