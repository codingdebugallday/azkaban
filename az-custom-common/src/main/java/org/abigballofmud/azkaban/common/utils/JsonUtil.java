package org.abigballofmud.azkaban.common.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.springframework.util.StringUtils;

/**
 * <p>
 * JsonUtil
 * </p>
 *
 * @author isacc 2020/5/20 16:54
 * @since 1.0
 */
public class JsonUtil {

    private static final Pattern NUMBER_PATTERN = Pattern.compile("\\d");
    private static final String ARRAY_LEFT = "[";
    private static final String ARRAY_RIGHT = "]";

    private JsonUtil() {
        throw new IllegalStateException("util class");
    }

    public static String getJsonNodeValue(JsonNode node, String attrs) {
        int index = attrs.indexOf('.');
        if (index == -1) {
            if (node != null && node.get(attrs) != null) {
                return node.get(attrs).asText();
            }
            return "";
        } else {
            String s1 = attrs.substring(0, index);
            String s2 = attrs.substring(index + 1);
            // 判断是否是数组节点
            Matcher matcher = NUMBER_PATTERN.matcher(s1);
            if (s1.contains(ARRAY_LEFT) && s1.endsWith(ARRAY_RIGHT) && matcher.find()) {
                String indexNodeName = s1.substring(0, s1.indexOf(ARRAY_LEFT));
                JsonNode indexNode;
                if (StringUtils.isEmpty(indexNodeName)) {
                    // [0].status 若是集合json [{},{}]
                    String num = s1.substring(s1.indexOf(ARRAY_LEFT) + 1, s1.indexOf(ARRAY_RIGHT));
                    indexNode = node.get(Integer.parseInt(num));
                } else {
                    // content[0].status 非集合json
                    ArrayNode arrayNode = (ArrayNode) node.get(indexNodeName);
                    indexNode = arrayNode.get(Integer.parseInt(matcher.group(0)));
                }
                return getJsonNodeValue(indexNode, s2);
            } else {
                return getJsonNodeValue(node.get(s1), s2);
            }
        }
    }

}
