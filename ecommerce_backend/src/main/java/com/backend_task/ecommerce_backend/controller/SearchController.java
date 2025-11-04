package com.backend_task.ecommerce_backend.controller;

import com.backend_task.ecommerce_backend.search.ProductSearch;
import co.elastic.clients.json.JsonData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000")
public class SearchController {

    @Autowired
    private ElasticsearchOperations elasticsearchOperations;

    @GetMapping("/search")
    public List<ProductSearch> searchProducts(@RequestParam String q) {
        final String queryText = q.toLowerCase(); // ✅ use this instead of reassigning q
        var queryBuilder = NativeQuery.builder();

        try {
            if (queryText.contains("under")) {
                double price = extractFirstNumber(queryText);
                queryBuilder.withQuery(qb -> qb
                    .bool(b -> b
                        .must(m -> m
                            .match(mt -> mt
                                .field("name")
                                .query(extractProductKeyword(queryText, "under"))
                                .fuzziness("AUTO")
                            )
                        )
                        .filter(f -> f
                            .range(r -> r
                                .field("discountedPrice")
                                .lte(JsonData.of(price))
                            )
                        )
                    )
                );

            } else if (queryText.contains("above")) {
                double price = extractFirstNumber(queryText);
                queryBuilder.withQuery(qb -> qb
                    .bool(b -> b
                        .must(m -> m
                            .match(mt -> mt
                                .field("name")
                                .query(extractProductKeyword(queryText, "above"))
                                .fuzziness("AUTO")
                            )
                        )
                        .filter(f -> f
                            .range(r -> r
                                .field("discountedPrice")
                                .gte(JsonData.of(price))
                            )
                        )
                    )
                );

            } else if (queryText.contains("between")) {
                double[] range = extractTwoNumbers(queryText);
                queryBuilder.withQuery(qb -> qb
                    .bool(b -> b
                        .must(m -> m
                            .match(mt -> mt
                                .field("name")
                                .query(extractProductKeyword(queryText, "between"))
                                .fuzziness("AUTO")
                            )
                        )
                        .filter(f -> f
                            .range(r -> r
                                .field("discountedPrice")
                                .gte(JsonData.of(range[0]))
                                .lte(JsonData.of(range[1]))
                            )
                        )
                    )
                );

            } else if (queryText.contains("in")) {
                double price = extractFirstNumber(queryText);
                queryBuilder.withQuery(qb -> qb
                    .bool(b -> b
                        .must(m -> m
                            .match(mt -> mt
                                .field("name")
                                .query(extractProductKeyword(queryText, "in"))
                                .fuzziness("AUTO")
                            )
                        )
                        .filter(f -> f
                            .range(r -> r
                                .field("discountedPrice")
                                .gte(JsonData.of(price - 2000))
                                .lte(JsonData.of(price + 2000))
                            )
                        )
                    )
                );

            } else {
                // ✅ Normal fuzzy search
                queryBuilder.withQuery(qb -> qb
                    .match(m -> m
                        .field("name")
                        .query(queryText)
                        .fuzziness("AUTO")
                    )
                );
            }

            var searchQuery = queryBuilder.build();
            var searchHits = elasticsearchOperations.search(searchQuery, ProductSearch.class);

            

            return searchHits.stream()
                    .map(SearchHit::getContent)
                    .map(p -> {
                        ProductSearch dto = new ProductSearch();
                        dto.setName(p.getName());
                        dto.setMrp(p.getMrp());
                        dto.setDiscountedPrice(p.getDiscountedPrice());
                        dto.setQuantity(p.getQuantity());
                        return dto;
                    })
                    .collect(Collectors.toList());

        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

    // ✅ Extract first number
    private double extractFirstNumber(String text) {
        var matcher = java.util.regex.Pattern.compile("(\\d+(?:\\.\\d+)?)").matcher(text);
        return matcher.find() ? Double.parseDouble(matcher.group(1)) : 0.0;
    }

    // ✅ Extract two numbers
    private double[] extractTwoNumbers(String text) {
        var matcher = java.util.regex.Pattern.compile("(\\d+(?:\\.\\d+)?)").matcher(text);
        double[] nums = new double[2];
        int i = 0;
        while (matcher.find() && i < 2) {
            nums[i++] = Double.parseDouble(matcher.group(1));
        }
        if (i == 1) nums[1] = nums[0] + 5000;
        return nums;
    }

    // ✅ Clean query keyword
    private String extractProductKeyword(String q, String keyword) {
        return q.replace(keyword, "")
                .replaceAll("\\d+", "")
                .replaceAll("and", "")
                .trim();
    }
}
