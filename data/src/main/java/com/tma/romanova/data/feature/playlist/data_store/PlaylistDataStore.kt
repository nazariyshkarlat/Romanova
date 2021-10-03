package com.tma.romanova.data.feature.playlist.data_store

import com.tma.romanova.data.data_source.DataStore
import com.tma.romanova.data.feature.playlist.entity.PlaylistEntity
import com.tma.romanova.domain.feature.playlist.entity.Playlist

class PlaylistDataStore : DataStore<PlaylistEntity, Playlist, Playlist>()