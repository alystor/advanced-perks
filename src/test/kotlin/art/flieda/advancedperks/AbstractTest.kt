package art.flieda.advancedperks

import org.koin.core.context.startKoin
import org.koin.core.module.Module

abstract class AbstractTest(
    vararg modules: Module
) {

    init {
        startKoin {
            modules(*modules)
        }
    }

}
