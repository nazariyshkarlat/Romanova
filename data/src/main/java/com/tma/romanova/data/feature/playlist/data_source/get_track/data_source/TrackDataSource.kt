package com.tma.romanova.data.feature.playlist.data_source.get_track.data_source

import com.tma.romanova.data.data_source.DataSource
import com.tma.romanova.data.data_source.DataStore
import com.tma.romanova.data.feature.playlist.entity.TrackEntity
import com.tma.romanova.domain.feature.playlist.entity.Track

class TrackDataSource : DataSource<TrackEntity, Track, Track>()