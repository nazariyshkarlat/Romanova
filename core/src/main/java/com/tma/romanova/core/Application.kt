package com.tma.romanova.core

import android.app.Application
import com.tma.romanova.domain.feature.metadata.Metadata

val application: Application
get() = _application!!

private var _application: Application? = null

val isApplicationInitialized
get() = _application != null

fun putApplication(application: Application){
    if(!isApplicationInitialized){
        _application = application
    }
}


val metadata: Metadata
    get() = _metadata!!

private var _metadata: Metadata? = null

val isMetadataInitialized
    get() = _metadata != null

fun putMetadata(metadata: Metadata){
    if(!isMetadataInitialized){
        _metadata = metadata
    }
}
