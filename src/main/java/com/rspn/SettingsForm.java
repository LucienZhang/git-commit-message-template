package com.rspn;

import javax.swing.*;

public class SettingsForm {
    private JRadioButton ticketAndDescriptionRadioButton;
    private JRadioButton prefixTicketAndDescriptionRadioButton;
    private JRadioButton customRadioButton;
    private JTextField customRegexTextField;
    private JTextField issueSuffixTextField;
    private JTextField issuePrefixTextField;
    private JTextField branchNameTextFieldPreview;
    private JTextField resultingCommitMessageTemplatePreview;
    private JPanel rootPanel;

    private JButton runPreviewButton;

    private JLabel errorLabel;
    private JRadioButton staticComponentsRadioButton;
    private JRadioButton regexGroupsAndBackreferencesRadioButton;
    private JTextField regexGroupBackreferenceTextField;

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

    public JRadioButton getBranchRegexCustomRadioButton() {
        return customRadioButton;
    }

    public JTextField getCustomRegexTextField() {
        return customRegexTextField;
    }

    public JTextField getIssueSuffixTextField() {
        return issueSuffixTextField;
    }

    public JTextField getIssuePrefixTextField() {
        return issuePrefixTextField;
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

    public JRadioButton getStaticComponentsRadioButton() {
        return staticComponentsRadioButton;
    }

    public JRadioButton getRegexGroupsAndBackreferencesRadioButton() {
        return regexGroupsAndBackreferencesRadioButton;
    }

    public JTextField getRegexGroupBackreferenceTextField() {
        return regexGroupBackreferenceTextField;
    }
}
