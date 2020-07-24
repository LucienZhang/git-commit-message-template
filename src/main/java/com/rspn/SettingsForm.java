package com.rspn;

import javax.swing.*;

public class SettingsForm {
    private JRadioButton ticketAndDescriptionRadioButton;
    private JRadioButton prefixTicketAndDescriptionRadioButton;
    private JRadioButton customRadioButton;
    private JTextField customRegexTextField;
    private JTextField issueSuffixTextField;
    private JTextField branchNameTextFieldPreview;
    private JTextField resultingCommitMessageTemplatePreview;
    private JPanel rootPanel;

    private JButton runPreviewButton;

    private JLabel errorLabel;

    public JLabel getErrorLabel() {
        return errorLabel;
    }

    public JPanel getRootPanel = rootPanel;

    public JButton getRunPreviewButton() {
        return runPreviewButton;
    }

    public JRadioButton getTicketAndDescriptionRadioButton() {
        return ticketAndDescriptionRadioButton;
    }

    public JRadioButton getPrefixTicketAndDescriptionRadioButton() {
        return prefixTicketAndDescriptionRadioButton;
    }

    public JRadioButton getCustomRadioButton() {
        return customRadioButton;
    }

    public JTextField getCustomRegexTextField() {
        return customRegexTextField;
    }

    public JTextField getIssueSuffixTextField() {
        return issueSuffixTextField;
    }

    public JTextField getBranchNameTextFieldPreview() {
        return branchNameTextFieldPreview;
    }

    public JTextField getResultingCommitMessageTemplatePreview() {
        return resultingCommitMessageTemplatePreview;
    }

    public JPanel getRootPanel() {
        return rootPanel;
    }

    public JPanel getGetRootPanel() {
        return getRootPanel;
    }
}
