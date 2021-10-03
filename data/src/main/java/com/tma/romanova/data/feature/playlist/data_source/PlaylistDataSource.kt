package com.tma.romanova.data.feature.playlist.data_source

import com.tma.romanova.data.feature.playlist.entity.PlaylistEntity
import com.tma.romanova.data.data_source.DataSource
import com.tma.romanova.domain.feature.playlist.entity.Playlist

class PlaylistDataSource : DataSource<PlaylistEntity, Playlist, Playlist>()