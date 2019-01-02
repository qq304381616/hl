package com.hl.greendao.generator;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class DaoGenerator {

    private Pattern patternKeepIncludes;
    private Pattern patternKeepFields;
    private Pattern patternKeepMethods;

    private Template templateDao;
    private Template templateEntity;
    private Template templateDaoMaster;
    private Template templateDaoSession;

    public DaoGenerator() throws IOException {
        patternKeepIncludes = compilePattern("INCLUDES");
        patternKeepFields = compilePattern("FIELDS");
        patternKeepMethods = compilePattern("METHODS");

        Configuration config = getConfiguration("dao.ftl");
        templateDao = config.getTemplate("dao.ftl");
        templateEntity = config.getTemplate("entity.ftl");
        templateDaoMaster = config.getTemplate("dao-master.ftl");
        templateDaoSession = config.getTemplate("dao-session.ftl");
    }

    public static void main(String[] args) throws IOException {
        System.out.println("gradle exec main");

//        Schema schema = new Schema(1, "com.hl.greendao.generator.demo");
//        Entity addressEntity = schema.addEntity("Addresse");
//        Property idProperty = addressEntity.addIdProperty().getProperty();
//        addressEntity.addIntProperty("count").index();
//        addressEntity.addIntProperty("dummy").notNull();
//
//        DaoGenerator daoGenerator = new DaoGenerator();
//        daoGenerator.generateAll(schema, ".\\greendao_copy\\src\\main\\java");
    }

    private Pattern compilePattern(String sectionName) {
        int flags = Pattern.DOTALL | Pattern.MULTILINE;
        return Pattern.compile(".*^\\s*?//\\s*?KEEP " + sectionName + ".*?\n(.*?)^\\s*// KEEP " + sectionName
                + " END.*?\n", flags);
    }

    private Configuration getConfiguration(String probingTemplate) throws IOException {
        Configuration config = new Configuration(Configuration.VERSION_2_3_23);
        config.setDirectoryForTemplateLoading(new File("F:\\code\\hl\\greendao_copy\\src\\main\\assets")); // com.hl.greendao.generator.DaoGenerator
        config.getTemplate(probingTemplate);
        return config;
    }

    public void generateAll(Schema schema, String outDir) {
        generateAll(schema, outDir, null, null);
    }

    public void generateAll(Schema schema, String outDir, String outDirEntity, String outDirTest) {

        File outDirFile = new File(outDir);
        if (!outDirFile.exists()) {
            outDirFile.mkdirs();
        }

        schema.init2ndPass();
        schema.init3rdPass();

        List<Entity> entities = schema.getEntities();
        for (Entity entity : entities) {
            generate(templateDao, outDirFile, entity.getJavaPackage(), entity.getClassNameDao(), schema, entity);
            generate(templateEntity, outDirFile, entity.getJavaPackage(), entity.getClassName(), schema, entity);
        }
        generate(templateDaoMaster, outDirFile, schema.getDefaultJavaPackage(),
                schema.getPrefix() + "DaoMaster", schema, null);
        generate(templateDaoSession, outDirFile, schema.getDefaultJavaPackage(),
                schema.getPrefix() + "DaoSession", schema, null);
    }

    private void generate(Template template, File outDirFile, String javaPackage, String javaClassName, Schema schema, Entity entity) {
        Map<String, Object> root = new HashMap<>();
        root.put("schema", schema);
        root.put("entity", entity);

        File file = toJavaFilename(outDirFile, javaPackage, javaClassName);
        file.getParentFile().mkdirs();
        if (entity != null && entity.getHasKeepSections()) {
            checkKeepSections(file, root);
        }

        try {
            Writer writer = new FileWriter(file);
            template.process(root, writer);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TemplateException e) {
            e.printStackTrace();
        }


    }

    private void checkKeepSections(File file, Map<String, Object> root) {
        if (file.exists()) {
            String contents = null;
            try {
                contents = new String(DaoUtil.readAllBytes(file));
            } catch (IOException e) {
                e.printStackTrace();
            }
            Matcher matcher;
            matcher = patternKeepIncludes.matcher(contents);
            if (matcher.matches()) {
                root.put("keepIncludes", matcher.group(1));
            }
            matcher = patternKeepFields.matcher(contents);
            if (matcher.matches()) {
                root.put("keepFields", matcher.group(1));
            }
            matcher = patternKeepMethods.matcher(contents);
            if (matcher.matches()) {
                root.put("keepMethods", matcher.group(1));
            }
        }
    }

    private File toJavaFilename(File outDirFile, String javaPackage, String javaClassName) {
        String packageSubPath = javaPackage.replace(".", "/");
        File packagePath = new File(outDirFile, packageSubPath);
        return new File(packagePath, javaClassName + ".java");
    }
}