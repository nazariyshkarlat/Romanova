package com.tma.romanova.data.feature.now_playing_track.data_source

import com.tma.romanova.data.feature.playlist.entity.PlaylistEntity
import com.tma.romanova.data.data_source.DataSource
import com.tma.romanova.data.feature.playlist.entity.TrackEntity
import com.tma.romanova.domain.feature.playlist.entity.Playlist
import com.tma.romanova.domain.feature.playlist.entity.Track

class NowPlayingTrackDataSource : DataSource<TrackEntity, Track, Track>()