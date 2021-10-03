package com.tma.romanova.data.feature.now_playing_track.cache

import com.tma.romanova.data.data_source.cache.CacheStorage
import com.tma.romanova.domain.feature.playlist.entity.Track

class NowPlayingTrackCacheStorage : CacheStorage<Track>(clazz = Track::class)