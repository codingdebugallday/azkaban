package org.abigballpfmud.azkaban.plugin.rest.binder;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.Scopes;
import org.abigballpfmud.azkaban.plugin.rest.service.ExecuteJobService;
import org.abigballpfmud.azkaban.plugin.rest.service.impl.ExecuteJobServiceImpl;

/**
 * <p>
 * description
 * </p>
 *
 * @author isacc 2020/4/23 16:12
 * @since 1.0
 */
public class RestApiBindModule implements Module {

    @Override
    public void configure(Binder binder) {
        binder.bind(ExecuteJobService.class)
                .to(ExecuteJobServiceImpl.class)
                .in(Scopes.SINGLETON);
    }
}
