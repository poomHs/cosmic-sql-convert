package com.poom.cosmic.sqllog.ui;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.ui.Messages;

/**
 * @author kingdee
 */
public class ReplaceSqlParametersAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        Editor editor = (Editor) e.getDataContext().getData("editor");
        if (editor == null) {
            return;
        }

        String selectedText = editor.getSelectionModel().getSelectedText();
        if (selectedText == null || selectedText.isEmpty()) {
            Messages.showInfoMessage("Please select a SQL statement first.", "Info");
            return;
        }

        String[] parts = selectedText.split("\n\n");
        if (parts.length != 2) {
            Messages.showInfoMessage("Please separate SQL statement and parameters with an empty line.", "Info");
            return;
        }

        String sql = parts[0];
        String[] parameterValues = parts[1].split(",");

        StringBuilder replacedSql = new StringBuilder();
        for (String parameter : parameterValues) {
            String replaced = sql.replaceAll("\\?", parameterValues[0].trim());
            replacedSql.append(replaced).append(",");
        }

        Messages.showInfoMessage("Replaced SQL: " + replacedSql.toString(), "Info");
    }
}
