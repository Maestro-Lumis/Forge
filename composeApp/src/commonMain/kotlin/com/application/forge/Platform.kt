package com.application.forge

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform