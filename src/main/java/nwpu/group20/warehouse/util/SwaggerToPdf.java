package nwpu.group20.warehouse.util;
import io.github.swagger2markup.Swagger2MarkupConverter;
import nwpu.group20.warehouse.util.SwaggerDownloader;
import org.asciidoctor.Asciidoctor;
import org.asciidoctor.Options;
import org.asciidoctor.OptionsBuilder;
import org.asciidoctor.SafeMode;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class SwaggerToPdf {

    public static void main(String[] args) {
        String swaggerUrl = "http://localhost:8080/api-docs";
        String swaggerInputPath = "src/main/resources/swagger.json";
        String asciidocOutputPath = "src/main/resources/swagger.adoc";
        String pdfOutputPath = "src/main/resources/swagger.pdf";

        try {
            // 下载 Swagger 文件
            SwaggerDownloader.downloadSwaggerJson(swaggerUrl, swaggerInputPath);

            // 检查下载文件是否存在
            if (!Paths.get(swaggerInputPath).toFile().exists()) {
                throw new IllegalArgumentException("Swagger file does not exist: " + swaggerInputPath);
            }

            // 输出下载文件内容检查
            String swaggerContent = new String(Files.readAllBytes(Paths.get(swaggerInputPath)));
            System.out.println("Swagger file content: " + swaggerContent);

            // 将 Swagger 文件转换为 AsciiDoc
            Swagger2MarkupConverter.from(Paths.get(swaggerInputPath))
                    .build()
                    .toFile(Paths.get(asciidocOutputPath));

            // 将 AsciiDoc 文件转换为 PDF
            Asciidoctor asciidoctor = Asciidoctor.Factory.create();
            Options options = OptionsBuilder.options()
                    .backend("pdf")
                    .safe(SafeMode.UNSAFE)
                    .toFile(Paths.get(pdfOutputPath).toFile())
                    .get();
            asciidoctor.convertFile(Paths.get(asciidocOutputPath).toFile(), options);

            System.out.println("PDF generated at: " + pdfOutputPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
