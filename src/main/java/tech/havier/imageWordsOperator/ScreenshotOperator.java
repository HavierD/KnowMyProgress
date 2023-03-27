package tech.havier.imageWordsOperator;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.File;
import java.io.IOException;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import net.sourceforge.tess4j.util.LoadLibs;
import tech.havier.stringBlockDelegate.TxtFileAppender;

public class ScreenshotOperator{

    public static void main(String[] args) {
        var string = extractText();
        TxtFileAppender.append(string);
    }

    private static String extractText() {
        BufferedImage image = null;
        ITesseract instance = new Tesseract();
        Transferable transferable = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null);
        if (transferable != null && transferable.isDataFlavorSupported(DataFlavor.imageFlavor)) {
            try {
                image = (BufferedImage) transferable.getTransferData(DataFlavor.imageFlavor);
            } catch (UnsupportedFlavorException e) {
                // handle this exception
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        File tessDataFolder = LoadLibs.extractTessResources("tessdata");
        instance.setDatapath(tessDataFolder.getAbsolutePath());
        instance.setLanguage("eng");

        try {
            String text = instance.doOCR(image);
            return text;
        } catch (TesseractException e) {
            throw new RuntimeException(e);
        }
    }
}

