package wasp

import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory
import com.intellij.openapi.options.SettingsEditor
import com.intellij.openapi.ui.TextFieldWithBrowseButton
import com.intellij.ui.components.JBLabel
import com.intellij.util.ui.FormBuilder
import javax.swing.JComponent
import javax.swing.JPanel

class WasmRunConfigurationEditor : SettingsEditor<WasmRunConfiguration>() {
    private val scriptPathField = TextFieldWithBrowseButton()
    private val panel: JPanel

    init {
        scriptPathField.addBrowseFolderListener(
            "Select WASM File",
            "Choose the WASM file to run",
            null,
            FileChooserDescriptorFactory.createSingleFileDescriptor("wasm")
        )

        panel = FormBuilder.createFormBuilder()
            .addLabeledComponent(JBLabel("WASM file:"), scriptPathField, 1, false)
            .addComponentFillVertically(JPanel(), 0)
            .panel
    }

    override fun resetEditorFrom(config: WasmRunConfiguration) {
        scriptPathField.text = config.scriptPath
    }

    override fun applyEditorTo(config: WasmRunConfiguration) {
        config.scriptPath = scriptPathField.text
    }

    override fun createEditor(): JComponent = panel
}
