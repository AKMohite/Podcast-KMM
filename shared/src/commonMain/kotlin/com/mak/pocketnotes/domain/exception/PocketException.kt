package com.mak.pocketnotes.domain.exception


abstract class PocketException(errorMsg: String, throwable: Throwable? = null) : Throwable(errorMsg, throwable)

open class APIException(val code: Int = 0, val errorMsg: String, throwable: Throwable? = null) : PocketException(errorMsg, throwable)

/**
 * @param firstLaunch is passed from SharedPreferences
 * @param exceptionType to differentiate business exceptions
 * @return Exception thrown if first launch or user is not authenticated in previous launch
 */
class AutoAuthenticateException(
    private val firstLaunch: Boolean,
    exceptionType: ExceptionType = ExceptionType.AUTO_AUTHENTICATE,
    errorMsg: String = exceptionType.message,
    throwable: Throwable? = null
) : PocketException(errorMsg, throwable)

class PocketAPIException(
    code: Int,
    exceptionType: ExceptionType = ExceptionType.API_EXCEPTION,
    errorMsg: String = exceptionType.message,
    throwable: Throwable? = null
) : APIException(code, errorMsg, throwable)
/**
 * Generic exception
 */
class UnknownAPIException(
    exceptionType: ExceptionType = ExceptionType.UNKNOWN,
    errorMsg: String = exceptionType.message,
    throwable: Throwable? = null
) : APIException(errorMsg = errorMsg, throwable = throwable)

/**
 * For socket timeout due to request read taking too long
 */
class RequestTimeoutException(
    exceptionType: ExceptionType = ExceptionType.SOCKET_TIMEOUT,
    errorMsg: String = exceptionType.message,
    throwable: Throwable? = null
) : APIException(errorMsg = errorMsg, throwable = throwable)

/**
 * when user is offline or not connected to network
 */
class NoNetworkException(
    exceptionType: ExceptionType = ExceptionType.NO_NETWORK,
    errorMsg: String = exceptionType.message,
    throwable: Throwable? = null
) : APIException(errorMsg = errorMsg, throwable = throwable)
