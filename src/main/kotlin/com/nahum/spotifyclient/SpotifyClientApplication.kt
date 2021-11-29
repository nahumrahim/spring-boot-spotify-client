package com.nahum.spotifyclient

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SpotifyClientApplication

fun main(args: Array<String>) {
	runApplication<SpotifyClientApplication>(*args)
}
