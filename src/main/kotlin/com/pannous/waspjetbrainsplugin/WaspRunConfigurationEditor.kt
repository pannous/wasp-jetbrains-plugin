package com.pannous.waspjetbrainsplugin

import com.intellij.execution.configuration.EnvironmentVariablesComponent
import com.intellij.openapi.fileChooser.FileChooserDescriptor
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory
import com.intellij.openapi.options.SettingsEditor
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.TextFieldWithBrowseButton
import com.intellij.ui.components.JBLabel
import com.intellij.util.ui.FormBuilder
import javax.swing.JComponent
import javax.swing.JPanel

class WaspRunConfigurationEditor(private val project: Project) : SettingsEditor<WaspRunConfiguration>() {

    private val scriptPathField = TextFieldWithBrowseButton()
    private val envVarsComponent = EnvironmentVariablesComponent()

    init {
        val descriptor = FileChooserDescriptor(true, false, false, false, false, false)
            .withFileFilter { it.extension == "wasp" }
        scriptPathField.addBrowseFolderListener(
            "Choose Wasp File",
            "Select the Wasp file to run",
            project,
            descriptor
        )
    }

    override fun createEditor(): JComponent {
        return FormBuilder.createFormBuilder()
            .addLabeledComponent(JBLabel("Wasp file:"), scriptPathField, 1, false)
            .addComponent(envVarsComponent, 5)
            .addComponentFillVertically(JPanel(), 0)
            .panel
    }

    override fun resetEditorFrom(configuration: WaspRunConfiguration) {
        scriptPathField.text = configuration.getScriptPath()
        envVarsComponent.envs = configuration.getEnvVars().toMutableMap()
    }

    override fun applyEditorTo(configuration: WaspRunConfiguration) {
        configuration.setScriptPath(scriptPathField.text)
        configuration.setEnvVars(envVarsComponent.envs)
    }
}