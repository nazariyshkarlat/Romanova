package com.tma.romanova.data.data_source

import com.tma.romanova.domain.result.DataSourceType

interface DataSourceProvider{
    val dataSourceType: DataSourceType
}

