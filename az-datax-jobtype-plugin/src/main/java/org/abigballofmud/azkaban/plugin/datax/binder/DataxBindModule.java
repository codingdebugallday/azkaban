package org.abigballofmud.azkaban.plugin.datax.binder;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.Scopes;
import org.abigballofmud.azkaban.plugin.datax.service.ExecuteJobService;
import org.abigballofmud.azkaban.plugin.datax.service.impl.ExecuteJobServiceImpl;

/**
 * <p>
 * description
 * </p>
 *
 * @author isacc 2020/4/24 11:20
 * @since 1.0
 */
public class DataxBindModule implements Module {

    @Override
    public void configure(Binder binder) {
        binder.bind(ExecuteJobService.class)
                .to(ExecuteJobServiceImpl.class)
                .in(Scopes.SINGLETON);
    }
}