package com.app.honey.data.remote.model.response.configuration

data class ConfigurationResponse(
    val message: String?,
    val responseCode: Int?,
    val data: ConfigData?
)

data class ConfigData(
    val appSettings: AppSettings?,
    val commonText: CommonText?,
    val privacySettings: PrivacySettings?
)

data class AppSettings(
    val appName: String?,
    val version: String?,
    val currency: Currency?,
    val supportedLanguages: List<String>?,
    val defaultLanguage: String?
)

data class Currency(
    val defaultCurrency: String?,
    val currencySymbol: String?,
    val availableCurrencies: List<String>?
)

data class CommonText(
    val welcomeMessage: String?,
    val loadingText: String?,
    val errorText: String?,
    val retryText: String?,
    val discountLabel: String?,
    val priceLabel: String?,
    val totalLabel: String?
)

data class PrivacySettings(
    val termsAndConditionsUrl: String?,
    val privacyPolicyUrl: String?
)
