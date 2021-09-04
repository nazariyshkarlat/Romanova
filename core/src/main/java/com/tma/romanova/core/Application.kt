package com.tma.romanova.core

import android.app.Application
import com.tma.romanova.domain.feature.metadata.Metadata
import org.koin.core.qualifier.named

val playerDataSourceProviderQualifier = named("player_data_source_provider")
val playerDataSourceQualifier = named("player_data_source")

val tracksMemoryStorageQualifier = named("tracks_memory_storage")
val trackDataStoreQualifier = named("track_data_store")
val trackDataStoreProviderQualifier = named("track_data_store_provider")
val trackDataSourceQualifier = named("track_data_source")
val trackDataSourceProviderQualifier = named("track_data_source_provider")
val playlistDataSourceQualifier = named("playlist_data_source")
val playlistDataSourceProviderQualifier = named("playlist_data_source_provider")

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
