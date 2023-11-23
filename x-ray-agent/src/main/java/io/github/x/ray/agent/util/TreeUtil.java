package io.github.x.ray.agent.util;

import io.github.x.ray.agent.model.CampareInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author wuou
 */
public class TreeUtil {

    public static List<CampareInfo> buildTree(List<CampareInfo> sources) {
        Map<Long, CampareInfo> nodeMap = new HashMap<>();
        List<CampareInfo> rootNodes = new ArrayList<>();

        for (CampareInfo node : sources) {
            nodeMap.put(node.getId(), node);
            if (node.getParentId() == 0) {
                rootNodes.add(node);
            }
        }

        for (CampareInfo rootNode : rootNodes) {
            buildChildTree(nodeMap, rootNode);
        }

        return rootNodes;
    }

    public static void buildChildTree(Map<Long, CampareInfo> nodeMap, CampareInfo pNode) {
        List<CampareInfo> children = new ArrayList<>();

        for (CampareInfo source : nodeMap.values()) {
            if (source.getParentId() != null && source.getParentId().equals(pNode.getId())) {
                buildChildTree(nodeMap, source);
                children.add(source);
            }
        }

        pNode.setChildrenList(children);
    }
}