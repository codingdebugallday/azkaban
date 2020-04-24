package org.abigballpfmud.azkaban.plugin.rest.exec;

import org.abigballpfmud.azkaban.plugin.rest.model.Payload;
import org.springframework.http.ResponseEntity;

/**
 * <p>
 * http执行
 * </p>
 *
 * @author isacc 2020/4/24 14:04
 * @since 1.0
 */
public interface Exec {

    /**
     * 执行
     *
     * @param payload Payload
     * @return ResponseEntity<String>
     */
    ResponseEntity<String> doExec(Payload payload);

}
