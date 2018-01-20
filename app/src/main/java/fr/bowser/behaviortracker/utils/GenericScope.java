package fr.bowser.behaviortracker.utils;

import javax.inject.Scope;

@Scope
public @interface GenericScope {
    Class component();
}
