package com.hl.greendao.core.query;

import com.hl.greendao.core.AbstractDao;
import com.hl.greendao.core.Property;

public class Join<SRC, DST> {

    final String sourceTablePrefix;
    final AbstractDao<DST, ?> daoDestination;
    final Property joinPropertySource;
    final Property joinPropertyDestination;
    final String tablePrefix;
    final WhereCollector<DST> whereCollector;

    public Join(String sourceTablePrefix, Property joinPrepertySource, AbstractDao<DST, ?> daoDestination, Property joinPrepertyDestination, String tablePrefix) {
        this.sourceTablePrefix = sourceTablePrefix;
        this.joinPropertySource = joinPrepertySource;
        this.daoDestination = daoDestination;
        this.joinPropertyDestination = joinPrepertyDestination;
        this.tablePrefix = tablePrefix;
        whereCollector = new WhereCollector<DST>(daoDestination, tablePrefix);
    }
}
