package com.nahum.spotifyclient.rest

import com.nahum.spotifyclient.service.SpotifyClientService
import com.nahum.spotifyclient.model.Track
import com.nahum.spotifyclient.repository.TrackRepository
import org.apache.hc.core5.http.HttpStatus
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@RestController
class CodingChallengeController @Autowired constructor(
    private val trackRepository: TrackRepository,
    private val spotifyClientService: SpotifyClientService
){

    /**
     * Get all track list.
     *
     * @return the list
     */
    @GetMapping("/tracks")
    fun getAllTracks(): MutableIterable<Track>? {
        return trackRepository.findAll()
    }

    /**
     * Get track by isrc
     *
     * @param isrc an identifier for searching in db
     *
     * @return a track or not found exception
     */
    @GetMapping("/codingchallenge/getTrack")
    fun getTrack(@RequestParam("isrc") isrc: String): Track? {
        val localTrack = trackRepository.findByIsrc(isrc)
            ?: throw ResponseStatusException(
                HttpStatus.SC_NOT_FOUND, "Track not found.", null
            )

        return localTrack
    }

    /**
     * Create track based on isrc
     *
     * @param isrc an identifier for the tracks in spotify
     *
     * @return the local track record
     */
    @PostMapping("/codingchallenge/createTrack")
    fun createTrack(@RequestParam("isrc") isrc: String): Track? {
        val localTrack = trackRepository.findByIsrc(isrc)
        if (localTrack != null) {
            throw ResponseStatusException(
                HttpStatus.SC_CONFLICT, "Record exists!", null
            )
        }

        val remoteSpotifyTrack = spotifyClientService.searchTracksSync(isrc)
            ?: throw ResponseStatusException(
                HttpStatus.SC_NOT_FOUND, "Remote record not found.", null
            );

        var result = Track(isrc, remoteSpotifyTrack.name, remoteSpotifyTrack.durationMs, remoteSpotifyTrack.isExplicit);
        return trackRepository.save<Track>(result)
    }
}