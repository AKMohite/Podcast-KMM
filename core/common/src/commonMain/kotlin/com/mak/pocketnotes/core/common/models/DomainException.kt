package com.mak.pocketnotes.core.common.models

data class DomainException(val type: ErrorType) : Exception("Exception caused due to $type")