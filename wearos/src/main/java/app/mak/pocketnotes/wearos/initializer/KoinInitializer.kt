package app.mak.pocketnotes.wearos.initializer

import android.content.Context
import androidx.startup.Initializer
import app.mak.pocketnotes.wearos.di.viewmodelModule
import com.mak.pocketnotes.core.common.di.commonModule
import com.mak.pocketnotes.core.database.di.localModule
import com.mak.pocketnotes.core.feature.data.di.coreDataModule
import com.mak.pocketnotes.core.remote.di.ktorModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin

class KoinInitializer : Initializer<KoinApplication> {
    override fun create(context: Context): KoinApplication {
        return startKoin {
            androidContext(context)
            modules(
                listOf(
                    viewmodelModule,
                    commonModule,
                    ktorModule(),
                    localModule,
                    coreDataModule,
                )
            )
        }
    }

    override fun dependencies(): List<Class<out Initializer<*>?>?> = emptyList()
}