package com.daajeh.wizardworldapp.data.network.exceptions

// Custom exceptions for different error scenarios
class DeviceOfflineException(message: String? = null) : Exception(message)
class AuthenticationException(message: String) : Exception(message)
class AuthorizationException(message: String) : Exception(message)
class NotFoundException(message: String) : Exception(message)
class ServerException(message: String) : Exception(message)
class NetworkException(message: String, cause: Throwable? = null) : Exception(message, cause)
class TimeoutException(message: String, cause: Throwable? = null) : Exception(message, cause)
