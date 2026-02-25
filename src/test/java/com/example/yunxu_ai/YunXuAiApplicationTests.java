package com.example.yunxu_ai;

import com.example.yunxu_ai.util.VectorDistanceUtils;
import org.junit.jupiter.api.Test;
import org.springframework.ai.document.Document;
import org.springframework.ai.openai.OpenAiEmbeddingModel;
import org.springframework.ai.reader.ExtractedTextFormatter;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.reader.pdf.config.PdfDocumentReaderConfig;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import java.util.Arrays;
import java.util.List;

@SpringBootTest
class YunXuAiApplicationTests {

    @Test
    void contextLoads() {
    }

    // 自动注入向量模型
    @Autowired
    private OpenAiEmbeddingModel embeddingModel;

    @Autowired
    private VectorStore vectorStore;
    /**
     * 欧氏距离与余弦距离的区别说明
     * 
     * 欧氏距离（Euclidean Distance）：
     * - 衡量两个向量在空间中的绝对距离
     * - 数值越小表示向量越接近
     * - 对向量的 magnitude（模长）敏感
     * - 适用于需要考虑向量大小的场景
     * 
     * 余弦距离（Cosine Distance）：
     * - 衡量两个向量方向的夹角差异
     * - 数值越小表示向量方向越相似
     * - 对向量的 magnitude 不敏感，只关注方向
     * - 适用于文本相似度、推荐系统等方向性分析场景
     * 
     * 关键区别：欧氏距离关注"位置相近"，余弦距离关注"方向一致"
     */

    @Test
    public void testEmbedding() {
        // 1.测试数据
        // 1.1.用来查询的文本，国际冲突
        String query = "global conflicts";

        // 1.2.用来做比较的文本
        String[] texts = new String[]{
                "哈马斯称加沙下阶段停火谈判仍在进行 以方尚未做出承诺",
                "土耳其、芬兰、瑞典与北约代表将继续就瑞典“入约”问题进行谈判",
                "日本航空基地水井中检测出有机氟化物超标",
                "国家游泳中心（水立方）：恢复游泳、嬉水乐园等水上项目运营",
                "我国首次在空间站开展舱外辐射生物学暴露实验",
        };
        // 2.向量化
        // 2.1.先将查询文本向量化
        float[] queryVector = embeddingModel.embed(query);

        // 2.2.再将比较文本向量化，放到一个数组
        List<float[]> textVectors = embeddingModel.embed(Arrays.asList(texts));

        // 3.比较欧氏距离
        // 3.1.把查询文本自己与自己比较，肯定是相似度最高的
        System.out.println(VectorDistanceUtils.euclideanDistance(queryVector, queryVector));
        // 3.2.把查询文本与其它文本比较
        for (float[] textVector : textVectors) {
            System.out.println(VectorDistanceUtils.euclideanDistance(queryVector, textVector));
        }
        System.out.println("------------------");

        // 4.比较余弦距离
        // 4.1.把查询文本自己与自己比较，肯定是相似度最高的
        System.out.println(VectorDistanceUtils.cosineDistance(queryVector, queryVector));
        // 4.2.把查询文本与其它文本比较
        for (float[] textVector : textVectors) {
            System.out.println(VectorDistanceUtils.cosineDistance(queryVector, textVector));
        }
    }

    @Test
    public void testVectorStore(){
        Resource resource = new FileSystemResource("中二知识笔记.pdf");
        // 1.创建PDF的读取器
        PagePdfDocumentReader reader = new PagePdfDocumentReader(
                resource, // 文件源
                PdfDocumentReaderConfig.builder()
                        .withPageExtractedTextFormatter(ExtractedTextFormatter.defaults())
                        .withPagesPerDocument(1) // 每1页PDF作为一个Document
                        .build()
        );
        // 2.读取PDF文档，拆分为Document
        List<Document> documents = reader.read();
        // 3.写入向量库
        vectorStore.add(documents);
        // 4.搜索
        SearchRequest request = SearchRequest.builder()
                .query("论语中教育的目的是什么")
                .topK(1)
                .similarityThreshold(0.6)
                .filterExpression("file_name == '中二知识笔记.pdf'")
                .build();
        List<Document> docs = vectorStore.similaritySearch(request);
        if (docs == null) {
            System.out.println("没有搜索到任何内容");
            return;
        }
        for (Document doc : docs) {
            System.out.println(doc.getId());
            System.out.println(doc.getScore());
            System.out.println(doc.getText());
        }
    }

}
