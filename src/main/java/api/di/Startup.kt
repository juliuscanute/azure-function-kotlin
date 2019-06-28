package api.di

import api.data.repository.MockRepository
import api.data.repository.RepositoryInterface
import org.koin.core.module.Module
import org.koin.dsl.module

object Startup {
    val dictionary: Module = module {
        single { MockRepository() as RepositoryInterface }
    }
}