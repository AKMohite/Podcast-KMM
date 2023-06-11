package com.mak.pocketnotes.domain.exception

const val EXCEPTION_GENERIC_MSG = "Something went wrong. Please try again later"
enum class ExceptionType(val key: String, val message: String) {
    INVALID_TOKEN(
        "api.exception.authenticate",
        "You need to authenticate yourself."
    ),
    API_EXCEPTION("api.exception", EXCEPTION_GENERIC_MSG),
    UNKNOWN("exception.unknown", EXCEPTION_GENERIC_MSG),
    UNKNOWN_HOST("exception.unknown.host", "Please check your internet connection"),
    SOCKET_TIMEOUT("api.exception.timeout", EXCEPTION_GENERIC_MSG),
    NO_NETWORK("exception.no.network", "Please check your internet connection"),
    AUTO_AUTHENTICATE("exception.splash.authenticate", "Please login")
}