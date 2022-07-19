package com.cranked.androidfileconverter.utils.rxjava

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject

class RxBus {
    private val publisher = PublishSubject.create<Any>()
    fun send(event: Any) {
        publisher.onNext(event)
    }

    fun <T> listen(evetType: Class<T>): Observable<T> = publisher.ofType(evetType)
    fun toObservable(): PublishSubject<Any> = publisher
}