/**
 * Copyright (c) 2011-2014, hubin (jobob@qq.com).
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.baomidou.mybatisplus.mapper;

/**
 * <p>
 * SQL 比较条件枚举类
 * </p>
 *
 * @author ghy
 * @Date 2018-01-05
 */
public class SqlCondition {

    /**
     * 等于
     */
    public static final String EQUAL = "%s=#{%s}";
    /**
     * 不等于
     */
    public static final String NOT_EQUAL = "%s&lt;&gt;#{%s}";
    /**
     * % 两边 %
     */
    public static final String LIKE = "%s LIKE '%%'||#{%s}||'%%'";
    /**
     * % 左
     */
    public static final String LIKE_LEFT = "%s LIKE '%%'||#{%s}";
    /**
     * 右 %
     */
    public static final String LIKE_RIGHT = "%s LIKE #{%s}||'%%'";


}
