package com.parley.testing.aspose;

import com.aspose.words.*;
import com.google.common.io.Files;

import java.io.File;
import java.util.Date;

import static org.testng.AssertJUnit.assertEquals;


public class WordChangesValidator {

    public void assertRevisions(File template, File result) throws Exception {
        Document asposeTemplate = new Document(Files.asByteSource(template).openStream());
        Document asposeResult = new Document(Files.asByteSource(result).openStream());

        asposeTemplate.acceptAllRevisions();
        asposeResult.acceptAllRevisions();

        asposeTemplate.compare(asposeResult, "Test", new Date());
        File output = new File(getClass().getClassLoader().getResource("classic/output.docx").getFile());
        asposeTemplate.save(output.getPath(), SaveFormat.DOCX);

        Document asposeOut = new Document(Files.asByteSource(output).openStream());
        assertEquals(0, asposeOut.getRevisions().getCount());
    }

    public Integer getRevisionsCount(File file) throws Exception {
        Document document = new Document(Files.asByteSource(file).openStream());
        return document.getRevisions().getCount();
    }


}
