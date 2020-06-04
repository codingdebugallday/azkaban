package org.abigballpfmud.azkaban.plugin.rest.exec;

import org.abigballofmud.azkaban.common.exception.CustomerJobProcessException;
import org.abigballpfmud.azkaban.plugin.rest.model.Payload;
import org.apache.commons.lang3.tuple.MutablePair;
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
     * @return MutablePair
     */
    MutablePair<ResponseEntity<String>, ResponseEntity<String>> doExec(Payload payload) throws CustomerJobProcessException;

}
