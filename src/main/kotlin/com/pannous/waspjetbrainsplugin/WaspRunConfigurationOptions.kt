package com.pannous.waspjetbrainsplugin

import com.intellij.execution.configurations.RunConfigurationOptions

class WaspRunConfigurationOptions : RunConfigurationOptions() {
    private val myScriptPath = string("").provideDelegate(this, "scriptPath")
    
    var scriptPath: String?
        get() = myScriptPath.getValue(this)
        set(value) = myScriptPath.setValue(this, value)
}