package com.mak.pocketnotes.core.feature.data.utils

import com.mak.pocketnotes.core.common.models.DomainException
import com.mak.pocketnotes.core.common.models.ErrorType
import org.mobilenativefoundation.store.store5.StoreReadResponse

fun StoreReadResponse.Error.exception(): DomainException {
    if (this is StoreReadResponse.Error.Exception && error is DomainException) {
        return error as DomainException
    }
    return DomainException(ErrorType.UNKNOWN)
}