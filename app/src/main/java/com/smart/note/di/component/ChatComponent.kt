package com.smart.note.di.component

import android.content.Context
import com.smart.note.di.module.ChatModule
import com.smart.note.di.module.EditModule
import com.smart.note.di.scope.ChatScope
import com.smart.note.di.scope.EditScope
import com.smart.note.module.chat.ChatFragment
import com.smart.note.module.chat.ChatViewModel
import com.smart.note.module.detail.DetailFragment
import com.smart.note.module.detail.DetailViewModel
import com.smart.note.module.edit.EditFragment
import com.smart.note.module.edit.EditViewModel
import dagger.BindsInstance
import dagger.Subcomponent

/**
 * 1.定义子组件，将注入方法定义在该接口中
 * 2. 定义Factory接口，暴露创建子组件的方法
 * 3. 在SubComponentModule定义子组件和主组件的依赖关系
 * 4. 在主组件中定义fun editComponent(): EditComponent.Factory方法
 * 5. 在App中创建AppComponent的实例；
 *
 */
@ChatScope
@Subcomponent(modules = [ChatModule::class])
interface ChatComponent {
    @Subcomponent.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): ChatComponent
    }

    fun inject(chatFragment: ChatFragment)
    fun inject(chatViewModel: ChatViewModel)

}