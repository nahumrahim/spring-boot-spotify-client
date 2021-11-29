package com.nahum.spotifyclient.service

import org.apache.hc.core5.http.ParseException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import se.michaelthelin.spotify.SpotifyApi
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException
import se.michaelthelin.spotify.model_objects.specification.Track
import java.io.IOException
import javax.annotation.PostConstruct

@Service
class SpotifyClientService {

    private var clientId: String? = null
    private var clientSecret: String? = null
    private var spotifyApi: SpotifyApi? = null

    @Autowired
    constructor(
        @Value("\${spotify.client-id}") pClientId:String,
        @Value("\${spotify.client-secret}") pClientSecret: String
    ) {
        this.clientId = pClientId
        this.clientSecret = pClientSecret
    }

    @PostConstruct
    private fun postConstructInit() {
        this.spotifyApi = SpotifyApi.Builder()
            .setClientId(clientId)
            .setClientSecret(clientSecret)
            .build()
    }

    /**
     * Get remote Spotify credentials and set access token
     *
     * @return
     */
    private fun clientCredentialsSync() {
        try {
            val clientCredentialsRequest = this.spotifyApi?.clientCredentials()
                ?.build()
            val clientCredentials = clientCredentialsRequest?.execute()
            this.spotifyApi?.accessToken = clientCredentials?.accessToken
            println("Expires in: " + clientCredentials?.expiresIn)
        } catch (e: IOException) {
            println("Error: " + e.message)
        } catch (e: SpotifyWebApiException) {
            println("Error: " + e.message)
        } catch (e: ParseException) {
            println("Error: " + e.message)
        }
    }

    /**
     * Retrieve the first track that matches to a query on Spotify
     *
     * @return a remote Spotify Track or null
     */
    fun searchTracksSync(q: String): Track? {

        // Call everytime and set a new token
        clientCredentialsSync();

        try {
            val searchTracksRequest = this.spotifyApi?.searchTracks("isrc:$q")
                ?.build()
            val trackPaging = searchTracksRequest?.execute()
            if (trackPaging != null && trackPaging.total > 0) {
                println("Total: " + trackPaging.total)
                return trackPaging.items[0]
            } else {
                return null;
            }
        } catch (e: IOException) {
            println("Error: " + e.message)
        } catch (e: SpotifyWebApiException) {
            println("Error: " + e.message)
        } catch (e: ParseException) {
            println("Error: " + e.message)
        }
        return null;
    }

}