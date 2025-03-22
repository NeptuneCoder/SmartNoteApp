package com.smart.note.dagger2.edit

import com.smart.note.module.edit.EditFragment
import dagger.Subcomponent

@EditScope
@Subcomponent(modules = [EditModule::class])
interface EditComponent {
    @Subcomponent.Factory
    interface Factory {
        fun create(): EditComponent
    }

    fun inject(editFragment: EditFragment)

}