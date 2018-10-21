package com.konglk.common;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * Created by konglk on 2018/8/28.
 */
public class DataProcess {

    /**
     * 遍历列表处理数据，如果存在prop，调用consumer并设置新值
     * @param datas
     * @param props
     * @param consumers
     */
    public static void process(List<Map<String, Object>> datas, String[] props, Function<Object, Object>[] consumers) {
        if(CollectionUtils.isEmpty(datas) || ArrayUtils.isEmpty(props) || ArrayUtils.isEmpty(consumers))
            return;
        if(props.length != consumers.length)
            throw new IllegalArgumentException("props's length not equal consumers's length");
        for(Map<String, Object> data : datas) {
            for(int i=0; i<props.length; i++) {
                String prop = props[i];
                Function<Object,Object> f = consumers[i];
                if(data.containsKey(prop)) {
                    Object v = data.get(prop);
                    data.put(prop,f.apply(v));
                }
            }
        }
    }
}
