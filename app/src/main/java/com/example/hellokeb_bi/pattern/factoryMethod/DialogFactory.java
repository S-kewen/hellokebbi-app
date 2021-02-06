package com.example.hellokeb_bi.pattern.factoryMethod;

import androidx.appcompat.app.AlertDialog;

public interface DialogFactory {
    AlertDialog.Builder createSimpleDialog();

    AlertDialog.Builder createSuccDialog();

    AlertDialog.Builder createWarnDialog();

    AlertDialog.Builder createErrorDialog();
}
