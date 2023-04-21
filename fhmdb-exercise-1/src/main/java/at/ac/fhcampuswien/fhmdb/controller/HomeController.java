package at.ac.fhcampuswien.fhmdb.controller;

import at.ac.fhcampuswien.fhmdb.models.Genre;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import at.ac.fhcampuswien.fhmdb.models.MovieAPI;
import at.ac.fhcampuswien.fhmdb.ui.MovieCell;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXListView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

import java.net.URL;
import java.time.Year;
import java.util.*;
import java.util.stream.Collectors;

public class HomeController implements Initializable {
    @FXML
    public JFXButton searchBtn;

    @FXML
    public TextField searchField;

    @FXML
    public JFXListView<Movie> movieListView;

    @FXML
    public JFXComboBox<Genre> genreComboBox;

    @FXML
    public JFXComboBox<Integer> yearComboBox;

    @FXML
    public JFXComboBox<Double> ratingComboBox;

    @FXML
    public JFXButton sortBtn;
    @FXML
    public JFXButton deleteBtn;
    public List<Movie> allMovies;
    public Set<Genre> allGenres = new HashSet<>();
    public Set<Double> allRatings = new HashSet<>();
    public Set<Integer> allReleaseYears = new HashSet<>();

    private final MovieAPI movieAPI = new MovieAPI();

    private static final String SORT_ASC = "Sort ↑";
    private static final String SORT_DESC = "Sort ↓";
    private boolean sortAsc = false;

    private final ObservableList<Movie> observableMovies = FXCollections.observableArrayList();   // automatically updates corresponding UI elements when underlying data changes

    private void initializeData() {
        allMovies = movieAPI.fetchMovies();
        allMovies.forEach(movie -> {
            allGenres.addAll(movie.getGenres());
            allRatings.add(movie.getRating());
            allReleaseYears.add(movie.getReleaseYear());
        });
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeData();
        observableMovies.addAll(allMovies);         // add dummy data to observable list
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(getMostPopularActor(observableMovies));
        alert.showAndWait();

        // initialize UI stuff
        movieListView.setItems(observableMovies);   // set data of observable list to list view
        movieListView.setCellFactory(movieListView -> new MovieCell()); // use custom cell factory to display data

        genreComboBox.setPromptText("Filter by Genre");
        genreComboBox.getItems().addAll(allGenres);
        FXCollections.sort(genreComboBox.getItems(), Comparator.comparing(Genre::name));

        yearComboBox.setPromptText("Filter by Release Year");
        yearComboBox.getItems().addAll(allReleaseYears);
        FXCollections.sort(yearComboBox.getItems(), Comparator.comparing(Integer::intValue));

        ratingComboBox.setPromptText("Filter by Rating");
        ratingComboBox.getItems().addAll(allRatings);
        FXCollections.sort(ratingComboBox.getItems(), Comparator.comparing(Double::doubleValue));

        searchBtn.setOnAction(actionEvent ->{
            movieListView.setItems(filterMovies(searchField.getText().toUpperCase(),
                    genreComboBox.getSelectionModel().getSelectedItem(),
                    ratingComboBox.getSelectionModel().getSelectedItem(),
                    yearComboBox.getSelectionModel().getSelectedItem()));
            movieListView.refresh();
            deleteBtn.setDisable(false);
        });

        FXCollections.sort(movieListView.getItems(), Comparator.comparing(Movie::getTitle));
        sortBtn.setOnAction(actionEvent -> {
            sortBtn.setText(sortMovies(movieListView.getItems()));
        });

        deleteBtn.setDisable(true);
        deleteBtn.setOnAction(actionEvent -> {
            searchField.clear();
            genreComboBox.getSelectionModel().clearSelection();
            yearComboBox.getSelectionModel().clearSelection();
            ratingComboBox.getSelectionModel().clearSelection();
            deleteBtn.setDisable(true);
            movieListView.setItems(observableMovies);
        });
    }

    public String sortMovies(ObservableList<Movie> movies) {
        if(sortAsc) {
            // Sort observableMovies in ascending order by title
            FXCollections.sort(movies, Comparator.comparing(Movie::getTitle));
            sortAsc = false;
            return SORT_DESC;
        } else {
            // Sort observableMovies in descending order by title
            FXCollections.sort(movies, Comparator.comparing(Movie::getTitle).reversed());
            sortAsc = true;
            return SORT_ASC;
        }
    }

    public ObservableList<Movie> filterMovies(String search, Genre genre, Double rating, Integer releaseYear) {
        ObservableList<Movie> movies = FXCollections.observableArrayList();
        Map<String, String> parameters = new HashMap<>();
        if (search != null && !search.isEmpty())
            parameters.put("query", search);
        if (genre != null)
            parameters.put("genre", genre.name());
        if (rating != null)
            parameters.put("ratingFrom", rating.toString());
        if (releaseYear != null)
            parameters.put("releaseYear", releaseYear.toString());

        movies.addAll(movieAPI.searchMovies(parameters));
        return movies;
    }

    // Director Count method
    public static String getMostPopularActor(List<Movie> movies) {
        Map<String, Long> actorCount = movies.stream()
                .flatMap(movie -> movie.getMainCast().stream())
                .collect(Collectors.groupingBy(actor -> actor, Collectors.counting()));
        return actorCount.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("");
    }

    public static int getLongestMovieTitle(List<Movie> movies) {
        Optional<Movie> longestTitleMovie = movies.stream()
                .max(Comparator.comparing(movie -> movie.getTitle().length()));

        return longestTitleMovie.map(movie -> movie.getTitle().length()).orElse(0);
    }

    public static long countMoviesFrom(List<Movie> movies, String director) {
        return movies.stream()
                .filter(movie -> movie.getDirectors().contains(director))
                .count();
    }

    public static List<Movie> getMoviesBetweenYears(List<Movie> movies, int startYear, int endYear) {
        return movies.stream()
                .filter(movie -> movie.getReleaseYear() >= startYear && movie.getReleaseYear() <= endYear)
                .collect(Collectors.toList());
    }
}