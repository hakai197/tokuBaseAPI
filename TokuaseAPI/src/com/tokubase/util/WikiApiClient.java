package com.tokubase.util;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tokubase.dto.wiki.WikiPageDetail;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
@Slf4j
@Component
@RequiredArgsConstructor
public class WikiApiClient {
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    @Value("${tokubase.wiki.request-delay-ms:300}")
    private long requestDelayMs;
    @Value("${tokubase.wiki.thumbnail-size:600}")
    private int thumbnailSize;
    private static final Pattern YEAR_PATTERN =
            Pattern.compile("\\b(197[1-9]|19[89]\\d|20[0-3]\\d)\\b");
    public List<String> getCategoryMembers(String apiUrl, String category, int max) {
        List<String> titles = new ArrayList<>();
        String continueToken = null;
        do {
            UriComponentsBuilder ub = UriComponentsBuilder.fromHttpUrl(apiUrl)
                    .queryParam("action", "query")
                    .queryParam("list", "categorymembers")
                    .queryParam("cmtitle", "Category:" + category)
                    .queryParam("cmlimit", "50")
                    .queryParam("cmtype", "page")
                    .queryParam("cmnamespace", "0")
                    .queryParam("format", "json");
            if (continueToken != null) ub.queryParam("cmcontinue", continueToken);
            String url = ub.build().toUriString();
            log.debug("Wiki category query: {}", url);
            try {
                String json = restTemplate.getForObject(url, String.class);
                JsonNode root = objectMapper.readTree(json);
                JsonNode members = root.path("query").path("categorymembers");
                for (JsonNode m : members) {
                    String title = m.path("title").asText();
                    if (title != null && !title.isBlank()) titles.add(title);
                    if (titles.size() >= max) return titles;
                }
                JsonNode cont = root.path("continue").path("cmcontinue");
                continueToken = cont.isMissingNode() ? null : cont.asText();
            } catch (Exception e) {
                log.warn("Category fetch failed [{}]: {}", category, e.getMessage());
                break;
            }
            sleep();
        } while (continueToken != null && titles.size() < max);
        log.info("Category {} returned {} titles", category, titles.size());
        return titles;
    }
    public WikiPageDetail getPageDetail(String apiUrl, String title) {
        String url = UriComponentsBuilder.fromHttpUrl(apiUrl)
                .queryParam("action", "query")
                .queryParam("titles", title)
                .queryParam("prop", "extracts|pageimages")
                .queryParam("exintro", "true")
                .queryParam("explaintext", "true")
                .queryParam("exsectionformat", "plain")
                .queryParam("pithumbsize", thumbnailSize)
                .queryParam("format", "json")
                .build().toUriString();
        log.debug("Wiki page detail: {}", url);
        try {
            String json = restTemplate.getForObject(url, String.class);
            JsonNode root = objectMapper.readTree(json);
            JsonNode pages = root.path("query").path("pages");
            for (JsonNode page : pages) {
                String pageIdStr = page.path("pageid").asText("-1");
                if ("-1".equals(pageIdStr)) { log.warn("Wiki page not found: {}", title); return null; }
                String extract      = page.path("extract").asText("").trim();
                String thumbnailUrl = page.path("thumbnail").path("source").asText(null);
                String imgFile      = page.path("pageimage").asText(null);
                String imageUrl     = buildFullImageUrl(apiUrl, imgFile);
                int[] years         = parseYears(extract);
                String wikiBase     = apiUrl.replace("/api.php", "");
                String wikiUrl      = wikiBase + "/wiki/" + title.replace(" ", "_");
                return WikiPageDetail.builder()
                        .pageId(Long.parseLong(pageIdStr))
                        .title(title).extract(truncate(extract, 2000))
                        .thumbnailUrl(thumbnailUrl).imageUrl(imageUrl)
                        .yearStart(years[0]).yearEnd(years[1]).wikiUrl(wikiUrl)
                        .build();
            }
        } catch (Exception e) { log.warn("Page fetch failed for {}: {}", title, e.getMessage()); }
        return null;
    }
    public List<String> getSeriesSubcategoryMembers(String apiUrl, String seriesTitle, String suffix, int max) {
        return getCategoryMembers(apiUrl, seriesTitle.replace(" ", "_") + suffix, max);
    }
    private int[] parseYears(String text) {
        if (text == null || text.isBlank()) return new int[]{0, 0};
        Matcher m = YEAR_PATTERN.matcher(text);
        List<Integer> found = new ArrayList<>();
        while (m.find()) {
            int y = Integer.parseInt(m.group(1));
            if (!found.contains(y)) found.add(y);
        }
        if (found.isEmpty()) return new int[]{0, 0};
        int start = found.get(0);
        int end   = found.size() > 1 ? found.get(found.size() - 1) : 0;
        if (end < start) end = 0;
        return new int[]{start, end};
    }
    private String buildFullImageUrl(String apiUrl, String f) {
        if (f == null) return null;
        return apiUrl.replace("/api.php", "") + "/wiki/Special:FilePath/" + f.replace(" ", "_");
    }
    private String truncate(String t, int max) {
        if (t == null || t.length() <= max) return t;
        return t.substring(0, max - 3) + "...";
    }
    private void sleep() {
        if (requestDelayMs > 0) { try { Thread.sleep(requestDelayMs); } catch (InterruptedException e) { Thread.currentThread().interrupt(); } }
    }
}
