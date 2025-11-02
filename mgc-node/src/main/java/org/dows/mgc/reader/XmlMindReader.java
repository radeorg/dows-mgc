package org.dows.mgc.reader;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dows.mgc.entity.MindNode;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Slf4j
@Component
public class XmlMindReader implements MindReader {
    @Override
    public Map<String, List<MindNode>> loadMindProjects(String ... projectUri) {
        return Map.of();
    }

    @Override
    public List<MindNode> loadProjectMind(String projectUri) {
        return List.of();
    }

   /* public MindNodes read(InputStream inputStream) throws IOException, DocumentException {
        InputStream stream = null;
        //获取文件输入流
        //FileInputStream input = new FileInputStream(file);
        //获取ZIP输入流(一定要指定字符集Charset.forName("GBK")否则会报java.lang.IllegalArgumentException: MALFORMED)
        ZipInputStream zipInputStream = new ZipInputStream(new BufferedInputStream(inputStream), Charset.forName("GBK"));
        //定义ZipEntry置为null,避免由于重复调用zipInputStream.getNextEntry造成的不必要的问题
        ZipEntry ze;
        List<List<Object>> list;
        //循环遍历
        while ((ze = zipInputStream.getNextEntry()) != null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            if (!ze.isDirectory() && ze.getName().equals("page/page.xml")) {
                //读取
                byte[] buffer = new byte[1024];
                int len;
                while ((len = zipInputStream.read(buffer)) > -1) {
                    baos.write(buffer, 0, len);
                }
                baos.flush();
                stream = new ByteArrayInputStream(baos.toByteArray()); //excel 流
                break;
            }
        }
        //一定记得关闭流
        zipInputStream.closeEntry();
        inputStream.close();
        return doRead(stream);
    }

    *//**
     * 读取sheet
     *
     * @param inputStream
     * @return
     * @throws IOException
     *//*
    private MindNodes doRead(InputStream inputStream) throws DocumentException {
        Document document = new SAXReader().read(inputStream);
        List<Node> nodes = new ArrayList<>();
        nodes.add(document.selectSingleNode("/Page/Shape[@Type='MainIdea']"));
        nodes.addAll(document.selectNodes("/Page/Shape[@Type='MainTopic']"));
        nodes.addAll(document.selectNodes("/Page/Shape[@Type='SubTopic']"));
        log.info("nodes.size={}", nodes.size());

        List<NodeSchema> nodeSchemas = nodes.stream().map(node -> {
            Element element = (Element) node;
            String id = element.attributeValue("ID");
            String type = element.attributeValue("Type");
            String text = node.selectSingleNode("Text/TextBlock/Text/pp/tp").getText();
            Node spuerNode = node.selectSingleNode("LevelData/Super");
            Node toSuperNode = node.selectSingleNode("LevelData/ToSuper");
            Node subLevelNode = node.selectSingleNode("LevelData/SubLevel");
            NodeSchema mindNode = new NodeSchema(id, text);
            if (spuerNode != null) {
                element = (Element) spuerNode;
                mindNode.setSuperId(element.attributeValue("V"));
            }
            if (subLevelNode != null) {
                element = (Element) subLevelNode;
                mindNode.setChildIds(element.attributeValue("V"));
            }
            return mindNode;
        }).collect(Collectors.toList());
        return new MindNodes(nodeSchemas);
    }*/
}