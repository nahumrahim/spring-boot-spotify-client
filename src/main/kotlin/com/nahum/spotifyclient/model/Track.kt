package com.nahum.spotifyclient.model

import javax.persistence.*

@Entity
class Track(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "serial")
    var id: Long?,
    var isrc: String,
    var name: String,
    var duration: Int,
    var explicit: Boolean
) {

    constructor(isrc: String, name: String, duration: Int, explicit: Boolean) : this(
        null,
        isrc,
        name,
        duration,
        explicit
    )

}