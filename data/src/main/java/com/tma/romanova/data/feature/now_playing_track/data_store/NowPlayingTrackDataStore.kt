package com.tma.romanova.data.feature.now_playing_track.data_store

import com.tma.romanova.data.data_source.DataStore
import com.tma.romanova.data.feature.playlist.entity.PlaylistEntity
import com.tma.romanova.data.feature.playlist.entity.TrackEntity
import com.tma.romanova.domain.feature.playlist.entity.Playlist
import com.tma.romanova.domain.feature.playlist.entity.Track

class NowPlayingTrackDataStore : DataStore<TrackEntity, Track, Track>()