package org.abigballofmud.azkaban.plugin.sqoop.binder;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.Scopes;
import org.abigballofmud.azkaban.plugin.sqoop.service.ExecuteJobService;
import org.abigballofmud.azkaban.plugin.sqoop.service.impl.ExecuteJobServiceImpl;

/**
 * <p>
 * description
 * </p>
 *
 * @author isacc 2020/4/24 11:20
 * @since 1.0
 */
public class SqoopBindModule implements Module {

    @Override
    public void configure(Binder binder) {
        binder.bind(ExecuteJobService.class)
                .to(ExecuteJobServiceImpl.class)
                .in(Scopes.SINGLETON);
    }
}