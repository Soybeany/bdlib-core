package com.soybeany.bdlib.log.extract.part;

import com.soybeany.bdlib.log.extract.model.Range;

/**
 * 行内容提供者
 * <br>Created by Soybeany on 2019/6/6.
 */
public interface ILineProvider {

    /**
     * 是否有下一行文本
     */
    boolean hasNext();

    /**
     * 返回下一行的文本
     */
    String next();

    /**
     * 状态信息，在{@link #hasNext()}为false时可提供具体原因
     */
    String stateMsg();

    /**
     * 设置内容提供的范围
     */
    void setRange(Range range);

}
