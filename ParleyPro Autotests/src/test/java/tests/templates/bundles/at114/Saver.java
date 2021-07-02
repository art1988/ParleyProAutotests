package tests.templates.bundles.at114;

import java.util.List;

// Cache that stores order of templates on Bundle form
public class Saver
{
    private static List<String> templates;

    public Saver(List<String> templates)
    {
        this.templates = templates;
    }

    public static List<String> getTemplates()
    {
        return templates;
    }
}
