package com.hl.dotime.utils

import java.util.*

class UUIDUtils {

    companion object {
        val uuid: String
            get() = UUID.randomUUID().toString().replace("-", "").toLowerCase();
    }

}