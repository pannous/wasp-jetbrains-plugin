package wasp

import com.intellij.openapi.fileChooser.FileChooserDescriptor
import com.intellij.openapi.options.SettingsEditor
import com.intellij.openapi.ui.TextFieldWithBrowseButton
import com.intellij.ui.components.JBLabel
import com.intellij.util.ui.FormBuilder
import javax.swing.JComponent
import javax.swing.JPanel

class WaspRunConfigurationEditor : SettingsEditor<WaspRunConfiguration>() {
    private val scriptPathField = TextFieldWithBrowseButton()
    
    init {
        val fileChooser = FileChooserDescriptor(true, false, false, false, false, false)
        fileChooser.withFileFilter { it.extension == "wasp" }
        scriptPathField.addBrowseFolderListener("Select Wasp File", "", null, fileChooser)
    }
    
    override fun createEditor(): JComponent {
        return FormBuilder.createFormBuilder()
            .addLabeledComponent(JBLabel("Wasp file:"), scriptPathField, 1, false)
            .addComponentFillVertically(JPanel(), 0)
            .panel
    }
    
    override fun resetEditorFrom(configuration: WaspRunConfiguration) {
        scriptPathField.text = configuration.scriptPath
    }
    
    override fun applyEditorTo(configuration: WaspRunConfiguration) {
        configuration.scriptPath = scriptPathField.text
    }
}