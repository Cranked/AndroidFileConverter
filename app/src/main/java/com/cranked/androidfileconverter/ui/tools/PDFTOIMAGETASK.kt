package com.cranked.androidfileconverter.ui.tools

class PDFTOIMAGETASK() : ITool() {
    constructor(list: List<String>) : this() {
        this.list = list
    }

    lateinit var list: List<String>
    override fun doTask() {
        list.forEach {

        }
    }
}