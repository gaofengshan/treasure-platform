package com.company.treasury.common.result;

import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class PageResult<T> extends Result<List<T>> {
    private long total;
    private int pages;
    private int pageNum;
    private int pageSize;

    public static <T> PageResult<T> success(List<T> data, long total, int pageNum, int pageSize) {
        PageResult<T> r = new PageResult<>();
        r.setCode(200);
        r.setMessage("success");
        r.setData(data);
        r.total = total;
        r.pageNum = pageNum;
        r.pageSize = pageSize;
        r.pages = (int) (total + pageSize - 1) / pageSize;
        return r;
    }
}
