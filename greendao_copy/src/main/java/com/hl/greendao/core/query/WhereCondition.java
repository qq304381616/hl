package com.hl.greendao.core.query;

import java.util.List;

public interface WhereCondition {
    void appendTo(StringBuilder builder, String tablePrefixOrNull);

    void appendValuesTo(List<Object> values);
}
