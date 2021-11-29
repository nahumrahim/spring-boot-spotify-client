package com.nahum.spotifyclient.repository

import com.nahum.spotifyclient.model.Track
import org.springframework.data.repository.CrudRepository

interface TrackRepository : CrudRepository<Track, Long> {
    fun findByIsrc(isrc: String): Track?
}