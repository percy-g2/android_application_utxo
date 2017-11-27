package com.androidevlinux.percy.UTXO.processor;

import java.io.IOException;
import java.io.Writer;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import javax.tools.JavaFileObject;

@SupportedAnnotationTypes("com.androidevlinux.percy.UTXO.processor.customAnnotation")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class customAnnotationProcessor extends AbstractProcessor {
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        StringBuilder builder = new StringBuilder()
                .append("package com.androidevlinux.percy.UTXO.generated; \n\n")
                .append("public class GeneratedClass {\n\n") // open class
                .append("public String getMessage() {\n") // open method
                .append("return \"Hello\";");
        
        builder
                .append("  }\n") // close method
                .append("}\n"); // close class



        try { // write the file
            JavaFileObject source = processingEnv.getFiler().createSourceFile("com.androidevlinux.percy.UTXO.generated.GeneratedClass");


            Writer writer = source.openWriter();
            writer.write(builder.toString());
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
            // Note: calling e.printStackTrace() will print IO errors
            // that occur from the file already existing after its first run, this is normal
        }


        return true;
    }

}
