package org.dows.mgc;

import org.dows.mgc.entity.MindNode;
import org.dows.mgc.reader.MindReader;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootApplication
@ExtendWith(SpringExtension.class)
@SpringBootTest
//@ContextConfiguration(classes = { TestConfig.class}) // SpringBootApplication 二选一
public class ReadTxtMindTest {

    @Autowired
    private MindReader mindReader;
    @Autowired
    private MindService mindService;

    @Test
    public void test() {

        String appId = "100000_rest";
        mindService.init(appId);
        mindService.generate(appId);
    }


    @Test
    public void testGetMindNodes() {
        // 调用被测试方法
        List<MindNode> nodes = mindReader.loadProjectMind("100000_rest");

        // 验证结果不为空
        assertNotNull(nodes, "解析结果不应为空");

        // 验证解析出的节点数量（根据rest.txt文件实际内容调整预期值）
        assertTrue(nodes.size() > 0, "至少应解析出一个节点");
        System.out.println("解析出的节点总数: " + nodes.size());

        // 验证根节点（没有父节点的节点）存在
        long rootNodeCount = nodes.stream().filter(node -> node.getParentId() == null).count();
        assertTrue(rootNodeCount > 0, "应该存在根节点");
        System.out.println("根节点数量: " + rootNodeCount);

        // 验证节点ID是按行号生成的
        boolean allNodeIdsAreNumbers = nodes.stream()
                .allMatch(node -> {
                    try {
                        Integer.parseInt(node.getNodeId());
                        return true;
                    } catch (NumberFormatException e) {
                        return false;
                    }
                });
        assertTrue(allNodeIdsAreNumbers, "所有节点ID应该是数字（行号）");

        // 验证节点的父子关系是否正确
        // 检查每个非根节点的父节点是否存在
        boolean allParentNodesExist = nodes.stream()
                .filter(node -> node.getParentId() != null)
                .allMatch(node -> nodes.stream()
                        .anyMatch(parentNode -> parentNode.getNodeId().equals(node.getParentId())));
        assertTrue(allParentNodesExist, "所有非根节点的父节点应该存在于结果集中");

        // 打印部分节点信息，方便调试
        System.out.println("前5个节点信息:");
        int printCount = Math.min(nodes.size(), 5);
        for (int i = 0; i < printCount; i++) {
            MindNode node = nodes.get(i);
            System.out.printf("节点ID: %s, 父节点ID: %s, 内容: %s\n",
                    node.getNodeId(), node.getParentId(), node.getContent());
        }
    }

    @Test
    public void testMindNodeParsing() {
        // 更详细的解析测试
        List<MindNode> nodes = mindReader.loadProjectMind("test-app-id");

        // 检查特定内容的节点是否存在
        boolean hasPkgNode = nodes.stream()
                .anyMatch(node -> node.getContent().contains("pkg/metric/度量"));
        assertTrue(hasPkgNode, "应该包含pkg/metric/度量节点");

        // 检查特定层级的关系
        MindNode pkgNode = nodes.stream()
                .filter(node -> node.getContent().contains("pkg/metric/度量"))
                .findFirst().orElse(null);
        assertNotNull(pkgNode, "pkg节点应该存在");

        // 检查pkg节点是否有子节点
        long childCount = nodes.stream()
                .filter(node -> pkgNode.getNodeId().equals(node.getParentId()))
                .count();
        assertTrue(childCount > 0, "pkg节点应该有子节点");
        System.out.printf("pkg节点(ID: %s)的子节点数量: %d\n", pkgNode.getNodeId(), childCount);
    }
}
