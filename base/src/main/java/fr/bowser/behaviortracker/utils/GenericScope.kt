package fr.bowser.behaviortracker.utils

import javax.inject.Scope
import kotlin.reflect.KClass

@Scope
annotation class GenericScope(val component: KClass<*>)
