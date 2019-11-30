/*
 * Copyright 2019-2029 geekidea(https://github.com/geekidea)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.geekidea.updaterecord.core.cache;

import io.geekidea.updaterecord.core.vo.UpdateRecordItemVo;
import io.geekidea.updaterecord.core.vo.UpdateRecordVo;

/**
 * 修改记录线程池缓存工具
 *
 * @author geekidea
 * @date 2019-11-09
 **/
public class UpdateRecordThreadLocalCache {

    private static ThreadLocal<UpdateRecordVo> threadLocal = new ThreadLocal<>();

    /**
     * 获取线程池中缓存的修改记录对象
     *
     * @return
     */
    public static UpdateRecordVo get() {
        return threadLocal.get();
    }

    /**
     * 缓存修改记录对象到线程池中
     *
     * @param updateRecordVo
     */
    public static void set(UpdateRecordVo updateRecordVo) {
        threadLocal.set(updateRecordVo);
    }

    /**
     * 移除当前线程池变量
     */
    public static void remove() {
        threadLocal.remove();
    }

    /**
     * 添加修改记录项
     *
     * @param updateRecordItemVo
     */
    public static void add(UpdateRecordItemVo updateRecordItemVo) {
        get().getUpdateRecordItemVos().add(updateRecordItemVo);
    }

    /**
     * 获取请求提交ID
     * 同一个请求，commitId一致
     *
     * @return
     */
    public static String getCommitId() {
        return get().getCommitId();
    }

    /**
     * 线程池中的变量是否为空
     *
     * @return
     */
    public static boolean empty() {
        return get() == null;
    }

}
