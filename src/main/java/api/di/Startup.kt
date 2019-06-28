package api.di

import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.module

object Startup {
    var dictionary: Module = module(override = true) {
        single { api.data.repository.DictionaryRepository() as api.data.repository.RepositoryInterface }
        single {
            com.microsoft.azure.documentdb.DocumentClient(java.lang.System.getenv("HOST"),
                    System.getenv("MASTER_KEY"),
                    com.microsoft.azure.documentdb.ConnectionPolicy.GetDefault(),
                    com.microsoft.azure.documentdb.ConsistencyLevel.Session)
        }
    }

    init {
        injectDependencies()
    }
}


fun injectDependencies() {
    startKoin { modules(Startup.dictionary) }
}