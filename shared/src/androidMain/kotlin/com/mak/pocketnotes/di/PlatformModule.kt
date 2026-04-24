package com.mak.pocketnotes.di

import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.Serializer
import androidx.datastore.tink.AeadSerializer
import app.cash.sqldelight.db.SqlDriver
import com.google.crypto.tink.Aead
import com.google.crypto.tink.KeyTemplate
import com.google.crypto.tink.RegistryConfiguration
import com.google.crypto.tink.aead.PredefinedAeadParameters
import com.google.crypto.tink.integration.android.AndroidKeysetManager
import com.mak.pocketnotes.data.repository.DataStoreSettingsRepository
import com.mak.pocketnotes.data.repository.SettingsRepository
import com.mak.pocketnotes.data.util.Dispatcher
import com.mak.pocketnotes.domain.models.AppSettings
import com.mak.pocketnotes.local.database.AndroidDatabaseDriverFactory
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import java.io.File
import java.io.InputStream
import java.io.OutputStream

actual fun platformModule() = module {
    single<SqlDriver> { AndroidDatabaseDriverFactory(androidContext()).createDriver() }

    single<DataStore<AppSettings>> {
        val keysetHandle =
            AndroidKeysetManager.Builder()
                .withSharedPref(androidContext(), "keyset", "keyset_prefs")
                .withKeyTemplate(KeyTemplate.createFrom(PredefinedAeadParameters.AES256_GCM))
                .withMasterKeyUri("android-keystore://master_key")
                .build()
                .keysetHandle

        val aeadSerializer = AeadSerializer(
            aead =
                keysetHandle.getPrimitive(
                    RegistryConfiguration.get(),
                    Aead::class.java,
                ),
            wrappedSerializer = AppSettingsSerializer(get<Dispatcher>()),
            associatedData = "settings.json".encodeToByteArray(),
        )

        val dataStore = DataStoreFactory.create(serializer = aeadSerializer) {
            File(androidContext().filesDir, "settings.json")
        }

//        val scope = CoroutineScope(get<Dispatcher>().io + Job())
//
//        val d = DataStore.Builder<Preferences>(
//            storage = ,
//            context = get<Dispatcher>().io
//        ).build()
//        val dataStore = dataStore(
//            fileName = "settings.json",
//            serializer = aeadSerializer,
//            scope = scope,
//        )
        dataStore
    }
    single<SettingsRepository> { DataStoreSettingsRepository(dataStore = get(), dispatcher = get()) }
}

class AppSettingsSerializer(
    private val dispatcher: Dispatcher
): Serializer<AppSettings> {
    override val defaultValue: AppSettings
        get() = AppSettings()

    override suspend fun readFrom(input: InputStream): AppSettings {
        val bytes = withContext(dispatcher.io) {
            input.use {
                it.readBytes()
            }
        }
        val json = bytes.decodeToString()
        return Json.decodeFromString(json)
    }

    override suspend fun writeTo(
        t: AppSettings,
        output: OutputStream
    ) {
        val json = Json.encodeToString(t)
        val bytes = json.toByteArray()
        withContext(dispatcher.io) {
            output.use {
                it.write(bytes)
            }
        }
    }

}