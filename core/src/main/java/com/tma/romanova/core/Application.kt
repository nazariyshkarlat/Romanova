package com.tma.romanova.core

import android.app.Application
import com.tma.romanova.domain.feature.metadata.Metadata
import org.koin.core.qualifier.named

val playerDataSourceProviderQualifier = named("player_data_source_provider")
val playerDataSourceQualifier = named("player_data_source")

val trackDataSourceQualifier = named("track_data_source")
val trackDataSourceProviderQualifier = named("track_data_source_provider")

val playlistDataSourceQualifier = named("playlist_data_source")
val playlistDataSourceProviderQualifier = named("playlist_data_source_provider")
val playlistsMemoryStorageQualifier = named("playlists_memory_storage")
val playlistDataStoreQualifier = named("playlist_data_store")
val playlistDataStoreProviderQualifier = named("playlist_data_store_provider")

val nowPlayingTrackCacheStorageQualifier = named("now_playing_track_cache_storage")
val nowPlayingTrackDataStoreQualifier = named("now_playing_track_data_store")
val nowPlayingTrackDataStoreProviderQualifier = named("now_playing_track_data_store_provider")
val nowPlayingTrackDataSourceQualifier = named("now_playing_track_data_source")
val nowPlayingTrackDataSourceProviderQualifier = named("now_playing_track_data_source_provider")

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
