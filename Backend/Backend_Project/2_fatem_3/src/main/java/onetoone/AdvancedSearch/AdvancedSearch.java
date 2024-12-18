package onetoone.AdvancedSearch;

import lombok.Getter;
import lombok.Setter;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Setter
@Getter
@Service
public class AdvancedSearch {
    private boolean include_adult;
    private boolean include_video;
    private String language;
    private int page;
    private String primary_release_date_gte;
    private String primary_release_date_lte;
    private String release_date_gte;
    private String release_date_lte;
    private String sort_by;
    private float vote_average_gte;
    private float vote_average_lte;
    private String with_cast;
    private String with_genres;
    private String with_keywords;
    private int with_runtime_gte;
    private int with_runtime_lte;
    private int year;

    public AdvancedSearch(){
        this.include_adult = false;
        this.include_video = false;
        this.language = "en-US";
        this.page = 1;
        this.primary_release_date_gte = "";
        this.primary_release_date_lte = "";
        this.release_date_gte = "";
        this.release_date_lte = "";
        this.sort_by = "popularity.desc";
        this.vote_average_gte = -1;
        this.vote_average_lte = -1;
        this.with_cast = "";
        this.with_genres = "";
        this.with_keywords = "";
        this.with_runtime_gte = -1;
        this.with_runtime_lte = -1;
        this.year = -1;

    }

    public AdvancedSearch(boolean include_adult, boolean include_video, String language,
                          int page, String primary_release_date_gte, String primary_release_date_lte,
                          String release_date_gte, String release_date_lte, String sort_by,
                          float vote_average_gte, float vote_average_lte, String with_cast,
                          String with_genres, String with_keywords, int with_runtime_gte,
                          int with_runtime_lte, int year) {
        this.include_adult = include_adult;
        this.include_video = include_video;
        this.language = language;
        this.page = page;
        this.primary_release_date_gte = primary_release_date_gte;
        this.primary_release_date_lte = primary_release_date_lte;
        this.release_date_gte = release_date_gte;
        this.release_date_lte = release_date_lte;
        this.sort_by = sort_by;
        this.vote_average_gte = vote_average_gte;
        this.vote_average_lte = vote_average_lte;
        this.with_cast = with_cast;
        this.with_genres = with_genres;
        this.with_keywords = with_keywords;
        this.with_runtime_gte = with_runtime_gte;
        this.with_runtime_lte = with_runtime_lte;
        this.year = year;
    }

    public Map<String, Object> advancedSearchMovies(AdvancedSearch advancedSearch) {
        String url = "https://api.themoviedb.org/3/discover/movie?api_key=6d50549abc8f2adc5e2b30beefa50f42";

        String include_adult_string = "&include_adult="+advancedSearch.include_adult;
        url += include_adult_string;

        String include_video_string = "&include_video="+advancedSearch.include_video;
        url += include_video_string;

        String language_string = "&language="+advancedSearch.language;
        url += language_string;

        String page_string = "&page="+advancedSearch.page;
        url += page_string;

        String primary_release_date_gte_string;
        if(advancedSearch.primary_release_date_gte.isEmpty()){
            primary_release_date_gte_string = "";
        }
        else{
            primary_release_date_gte_string = "&primary_release_date.gte="+advancedSearch.primary_release_date_gte;
        }
        url += primary_release_date_gte_string;

        String primary_release_date_lte_string;
        if(advancedSearch.primary_release_date_lte.isEmpty()){
            primary_release_date_lte_string = "";
        }
        else{
            primary_release_date_lte_string = "&primary_release_date.lte="+advancedSearch.primary_release_date_lte;
        }
        url += primary_release_date_lte_string;

        String release_date_gte_string;
        if(advancedSearch.release_date_gte.isEmpty()){
            release_date_gte_string = "";
        }
        else{
            release_date_gte_string = "&release_date.gte="+advancedSearch.release_date_gte;
        }
        url += release_date_gte_string;

        String release_date_lte_string;
        if(advancedSearch.release_date_lte.isEmpty()){
            release_date_lte_string = "";
        }
        else{
            release_date_lte_string = "&release_date.lte="+advancedSearch.release_date_lte;
        }
        url += release_date_lte_string;

        String sort_by_string;
        if(advancedSearch.sort_by.isEmpty()){
            sort_by_string = "";
        }
        else{
            sort_by_string = "&sort_by=" + advancedSearch.sort_by;
        }
        url += sort_by_string;

        String vote_average_gte_string;
        if(advancedSearch.vote_average_gte == -1){
            vote_average_gte_string = "";
        }
        else{
            vote_average_gte_string = "&vote_average.gte="+advancedSearch.vote_average_gte;
        }
        url += vote_average_gte_string;

        String vote_average_lte_string;
        if(advancedSearch.vote_average_lte == -1){
            vote_average_lte_string = "";
        }
        else{
            vote_average_lte_string = "&vote_average.lte="+advancedSearch.vote_average_lte;
        }
        url += vote_average_lte_string;

        String with_cast_string;
        if(advancedSearch.with_cast.isEmpty()){
            with_cast_string = "";
        }
        else{
            with_cast_string = "&with_cast="+advancedSearch.with_cast;
        }
        url += with_cast_string;

        String with_genres_string;
        if(advancedSearch.with_genres.isEmpty()){
            with_genres_string = "";
        }
        else{
            with_genres_string = "&with_genres="+advancedSearch.with_genres;
        }
        url += with_genres_string;

        String with_keywords_string;
        if(advancedSearch.with_keywords.isEmpty()){
            with_keywords_string = "";
        }
        else{
            with_keywords_string = "&with_keywords="+advancedSearch.with_keywords;
        }
        url += with_keywords_string;

        String with_runtime_gte_string;
        if(advancedSearch.with_runtime_gte == -1){
            with_runtime_gte_string = "";
        }
        else{
            with_runtime_gte_string = "&with_runtime.gte="+advancedSearch.with_runtime_gte;
        }
        url += with_runtime_gte_string;

        String with_runtime_lte_string;
        if(advancedSearch.with_runtime_lte == -1){
            with_runtime_lte_string = "";
        }
        else{
            with_runtime_lte_string = "&with_runtime.lte="+advancedSearch.with_runtime_lte;
        }
        url += with_runtime_lte_string;

        String year_string;
        if(advancedSearch.year == -1){
            year_string = "";
        }
        else{
            year_string = "&year="+advancedSearch.year;
        }
        url += year_string;



        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject(url, HashMap.class);
    }
}